package app.dev.manabi.database

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption

private const val APP_FOLDER_NAME = "Manabi"
private const val DATABASE_FILE_NAME = "manabi.db"

// Recent commits may exist only in -wal, so it has to travel with the database. The database goes
// last because its arrival marks the move as finished: an interrupted move runs again next launch.
private val DATABASE_FILE_SUFFIXES = listOf("-wal", "-shm", "")

/** The folder this OS sets aside for the current user's app data. */
internal fun appDataDirectory(
    osName: String = System.getProperty("os.name"),
    userHome: String = System.getProperty("user.home"),
    environment: (String) -> String? = System::getenv,
): File {
    val os = osName.lowercase()
    val base = when {
        os.startsWith("mac") -> File(userHome, "Library/Application Support")
        os.startsWith("windows") ->
            environment("APPDATA")?.takeIf(String::isNotBlank)?.let(::File)
                ?: File(userHome, "AppData/Roaming")
        // The XDG spec says a relative path is invalid and must be ignored.
        else ->
            environment("XDG_DATA_HOME")?.let(::File)?.takeIf(File::isAbsolute)
                ?: File(userHome, ".local/share")
    }
    return File(base, APP_FOLDER_NAME)
}

/**
 * Returns the database file to open, creating [dataDirectory] if needed. The first time, a database
 * left in [legacyDirectory] by an earlier version is moved across; one already in [dataDirectory]
 * is never replaced.
 *
 * If the move fails the old file is returned instead. Starting empty would strand the old data,
 * because the next launch would find a database in [dataDirectory] and skip the move.
 */
internal fun prepareDatabaseFile(dataDirectory: File, legacyDirectory: File): File {
    val database = File(dataDirectory, DATABASE_FILE_NAME)
    val legacy = File(legacyDirectory, DATABASE_FILE_NAME)
    // The temp folder is shared on Linux, so a database this user can't write is someone else's.
    val hasLegacyData = legacy.isFile && legacy.canWrite()
    return try {
        Files.createDirectories(dataDirectory.toPath())
        if (hasLegacyData && !database.exists()) moveDatabase(legacy, database)
        database
    } catch (e: IOException) {
        if (!hasLegacyData) throw e
        System.err.println("Could not move the Manabi database to $dataDirectory: $e")
        legacy
    }
}

private fun moveDatabase(legacy: File, database: File) {
    val files = DATABASE_FILE_SUFFIXES.map { suffix ->
        DatabaseFileMove(File(legacy.path + suffix), File(database.path + suffix))
    }
    try {
        files.forEach(DatabaseFileMove::stage)
        files.forEach(DatabaseFileMove::commit)
    } finally {
        files.forEach { it.staged.delete() }
    }
    files.forEach { it.source.delete() }
}

/**
 * Moves one file by copying it beside [destination] and renaming it, so that a move between volumes
 * can't leave a half-written file, and the source stays intact until every file has landed.
 */
private class DatabaseFileMove(val source: File, val destination: File) {
    val staged = File(destination.path + ".moving")

    fun stage() {
        if (!source.exists()) return
        FileOutputStream(staged).use { output ->
            Files.copy(source.toPath(), output)
            output.fd.sync()
        }
    }

    fun commit() {
        if (source.exists()) {
            Files.move(staged.toPath(), destination.toPath(), StandardCopyOption.ATOMIC_MOVE)
        } else {
            // SQLite would replay a leftover -wal from an interrupted move into this database.
            destination.delete()
        }
    }
}
