package isaoglu.cahit.watchmate.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contents")
data class Content(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var rating: Float
)