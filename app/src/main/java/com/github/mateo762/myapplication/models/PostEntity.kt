package com.github.mateo762.myapplication.models

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.BLOB
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "caption") val caption: String = "",
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "date_posted") val datePosted: String = "",
    @ColumnInfo(name = "image_url") val imageUrl : String = "",
    @ColumnInfo(name = "username") val username : String = "",
    @ColumnInfo(name = "assoc_habit") val assocHabit : String = "",
    @ColumnInfo(name = "habit_image_entity_id") val habitImageEntityId : String = "",
    )

{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PostEntity

        if (uid != other.uid) return false
        if (datePosted != other.datePosted) return false
        if (!imageUrl.contentEquals(other.imageUrl)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid
        result = 31 * result + (datePosted?.hashCode() ?: 0)
        return result
    }
}
