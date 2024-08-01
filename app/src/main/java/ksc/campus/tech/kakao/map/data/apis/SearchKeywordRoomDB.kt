package ksc.campus.tech.kakao.map.data.apis

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
import ksc.campus.tech.kakao.map.data.entities.SearchKeywordEntity

@Dao
interface SearchKeywordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: SearchKeywordEntity)

    @Delete
    fun delete(entity: SearchKeywordEntity)

    @Query("SELECT * FROM ${SearchKeywordEntity.TABLE_NAME}")
    fun queryAllKeywords(): Flow<List<SearchKeywordEntity>>

    @Query("DELETE FROM ${SearchKeywordEntity.TABLE_NAME} WHERE ${SearchKeywordEntity.COLUMN_KEYWORD} = :name")
    fun deleteWhere(name: String)
}

@Database(entities = [SearchKeywordEntity::class], version = 4)
abstract class SearchKeywordDB : RoomDatabase() {
    abstract fun dao(): SearchKeywordDao
}
