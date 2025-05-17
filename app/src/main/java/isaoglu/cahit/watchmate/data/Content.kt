package isaoglu.cahit.watchmate.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contents")
data class Content(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val rating: Float = 0.0f,
    val createdAt: Long = System.currentTimeMillis(),
    val isWatched: Boolean = false
)