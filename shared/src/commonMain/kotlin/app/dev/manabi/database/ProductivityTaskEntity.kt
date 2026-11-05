package app.dev.manabi.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ProductivityTask",
    indices = [Index("date")],
)
data class ProductivityTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val text: String,
    val isDone: Boolean,
    val createdAt: String,
    val updatedAt: String,
)
