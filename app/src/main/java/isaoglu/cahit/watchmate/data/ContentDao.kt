package isaoglu.cahit.watchmate.data

import androidx.room.*

@Dao
interface ContentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContent(content: Content)

    @Update
    suspend fun updateContent(content: Content)

    @Delete
    suspend fun deleteContent(content: Content)

    @Query("SELECT * FROM contents WHERE rating = 0.0")
    suspend fun getWatchLater(): List<Content>

    @Query("SELECT * FROM contents WHERE rating > 0.0")
    suspend fun getRatedContents(): List<Content>

    @Query("SELECT * FROM contents WHERE id = :contentId LIMIT 1")
    suspend fun getContentById(contentId: Int): Content
}

