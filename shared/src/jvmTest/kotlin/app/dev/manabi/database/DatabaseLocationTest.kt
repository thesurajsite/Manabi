package app.dev.manabi.database

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import java.io.File
import java.io.IOException
import kotlin.io.path.createTempDirectory
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Assume.assumeFalse

class DatabaseLocationTest {
    private val root = createTempDirectory("manabi-database-location").toFile()
    private val legacyDirectory = File(root, "tmp").apply { mkdirs() }
    private val dataDirectory = File(root, "home/Manabi")
    private val legacy = File(legacyDirectory, "manabi.db")
    private val database = File(dataDirectory, "manabi.db")

    @AfterTest
    fun cleanUp() {
        root.deleteRecursively()
    }

    private fun File.sidecar(suffix: String) = File(path + suffix)

    private fun prepare() = prepareDatabaseFile(dataDirectory, legacyDirectory)

    @Test
    fun usesApplicationSupportOnMac() {
        assertEquals(
            File("/Users/sam/Library/Application Support/Manabi"),
            appDataDirectory("Mac OS X", "/Users/sam") { null },
        )
    }

    @Test
    fun usesAppDataOnWindows() {
        val environment = mapOf("APPDATA" to "/sam/Roaming")
        assertEquals(
            File("/sam/Roaming/Manabi"),
            appDataDirectory("Windows 11", "/sam", environment::get),
        )
    }

    @Test
    fun fallsBackToRoamingFolderWhenAppDataIsUnset() {
        assertEquals(
            File("/sam/AppData/Roaming/Manabi"),
            appDataDirectory("Windows 11", "/sam") { null },
        )
    }

    @Test
    fun usesXdgDataHomeOnLinux() {
        val environment = mapOf("XDG_DATA_HOME" to "/sam/data")
        assertEquals(
            File("/sam/data/Manabi"),
            appDataDirectory("Linux", "/sam", environment::get),
        )
    }

    @Test
    fun usesLocalShareOnLinuxWhenXdgDataHomeIsUnsetOrInvalid() {
        val expected = File("/sam/.local/share/Manabi")
        listOf(null, "", "relative/path").forEach { value ->
            assertEquals(expected, appDataDirectory("Linux", "/sam") { value })
        }
    }

    @Test
    fun movesTheDatabaseAndItsWalFilesAcross() {
        legacy.writeText("database")
        legacy.sidecar("-wal").writeText("wal")
        legacy.sidecar("-shm").writeText("shm")

        assertEquals(database, prepare())

        assertEquals("database", database.readText())
        assertEquals("wal", database.sidecar("-wal").readText())
        assertEquals("shm", database.sidecar("-shm").readText())
        assertEquals(listOf("manabi.db", "manabi.db-shm", "manabi.db-wal"), dataDirectory.list()?.sorted())
        assertEquals(emptyList(), legacyDirectory.list()?.toList())
    }

    @Test
    fun keepsCommitsThatOnlyExistInTheWal() {
        val driver = BundledSQLiteDriver()
        val running = File(root, "running").apply { mkdirs() }
        // Copying while the connection is open gives the files a killed app leaves behind.
        driver.open(File(running, "manabi.db").path).use { connection ->
            connection.execSQL("PRAGMA journal_mode = WAL")
            connection.execSQL("PRAGMA wal_autocheckpoint = 0")
            connection.execSQL("CREATE TABLE attendance (subject TEXT)")
            connection.execSQL("INSERT INTO attendance VALUES ('Maths')")
            running.copyRecursively(legacyDirectory, overwrite = true)
        }
        assertTrue(legacy.sidecar("-wal").length() > 0)

        val moved = prepare()

        driver.open(moved.path).use { connection ->
            connection.prepare("SELECT subject FROM attendance").use { statement ->
                assertTrue(statement.step())
                assertEquals("Maths", statement.getText(0))
            }
        }
    }

    @Test
    fun neverOverwritesADatabaseAlreadyInTheNewFolder() {
        dataDirectory.mkdirs()
        database.writeText("current")
        database.sidecar("-wal").writeText("current wal")
        legacy.writeText("old")
        legacy.sidecar("-wal").writeText("old wal")

        assertEquals(database, prepare())

        assertEquals("current", database.readText())
        assertEquals("current wal", database.sidecar("-wal").readText())
        assertEquals("old", legacy.readText())
        assertEquals("old wal", legacy.sidecar("-wal").readText())
    }

    @Test
    fun movesOnlyOnce() {
        legacy.writeText("database")
        prepare()
        database.writeText("edited after the move")
        legacy.writeText("old copy that turned up again")

        assertEquals(database, prepare())

        assertEquals("edited after the move", database.readText())
    }

    @Test
    fun createsTheFolderWhenThereIsNothingToMove() {
        assertEquals(database, prepare())

        assertTrue(dataDirectory.isDirectory)
        assertFalse(database.exists())
    }

    @Test
    fun ignoresWalFilesWhoseDatabaseIsGone() {
        legacy.sidecar("-wal").writeText("orphaned wal")
        legacy.sidecar("-shm").writeText("orphaned shm")

        assertEquals(database, prepare())

        assertEquals(emptyList(), dataDirectory.list()?.toList())
    }

    @Test
    fun clearsLeftoversOfAnInterruptedMove() {
        dataDirectory.mkdirs()
        database.sidecar("-wal").writeText("stale wal")
        database.sidecar("-shm").writeText("stale shm")
        database.sidecar("-wal.moving").writeText("stale copy")
        legacy.writeText("database")

        assertEquals(database, prepare())

        assertEquals("database", database.readText())
        assertEquals(listOf("manabi.db"), dataDirectory.list()?.toList())
    }

    @Test
    fun keepsUsingTheOldDatabaseWhenTheMoveFails() {
        dataDirectory.parentFile.writeText("a file where the folder should be")
        legacy.writeText("database")
        legacy.sidecar("-wal").writeText("wal")

        assertEquals(legacy, prepare())

        assertEquals("database", legacy.readText())
        assertEquals("wal", legacy.sidecar("-wal").readText())
    }

    @Test
    fun aMoveThatFailsPartWayRunsAgainOnTheNextLaunch() {
        legacy.writeText("database")
        legacy.sidecar("-wal").writeText("wal")
        // A folder with something in it can't be replaced by a file, so the -wal can't land.
        val blocker = File(database.sidecar("-wal"), "blocker")
        blocker.parentFile.mkdirs()
        blocker.writeText("")

        assertEquals(legacy, prepare())

        assertFalse(database.exists())
        assertEquals("database", legacy.readText())
        assertEquals("wal", legacy.sidecar("-wal").readText())

        blocker.parentFile.deleteRecursively()

        assertEquals(database, prepare())

        assertEquals("database", database.readText())
        assertEquals("wal", database.sidecar("-wal").readText())
        assertEquals(listOf("manabi.db", "manabi.db-wal"), dataDirectory.list()?.sorted())
    }

    @Test
    fun failsWhenTheFolderCannotBeCreatedAndThereIsNoOldDatabase() {
        dataDirectory.parentFile.writeText("a file where the folder should be")

        assertFailsWith<IOException> { prepare() }
    }

    @Test
    fun leavesAnotherUsersDatabaseAlone() {
        legacy.writeText("someone else's data")
        legacy.setWritable(false)
        assumeFalse("File permissions don't apply to this user", legacy.canWrite())

        assertEquals(database, prepare())

        assertEquals("someone else's data", legacy.readText())
        assertFalse(database.exists())
    }
}
