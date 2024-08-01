package ksc.campus.tech.kakao.map.data.entities

import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ksc.campus.tech.kakao.map.data.entities.SearchKeywordEntity.Companion.COLUMN_KEYWORD
import ksc.campus.tech.kakao.map.data.entities.SearchKeywordEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME, indices = [Index(value = [COLUMN_KEYWORD], unique = true)])
data class SearchKeywordEntity(
    @ColumnInfo(name = BaseColumns._ID)
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = COLUMN_KEYWORD)
    val keyword: String?
) {

    companion object {
        const val TABLE_NAME = "SEARCH_KEYWORD"
        const val COLUMN_KEYWORD = "keyword"
    }
}