package com.github.mateo762.myapplication.room

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.BLOB
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "caption") val caption: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "date_posted") val datePosted: String?,
    // This could also be java.sql.Date
    @ColumnInfo(typeAffinity = BLOB) val image: ByteArray?
)

// ByteArray requires hashcode and equals methods

{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PostEntity

        if (uid != other.uid) return false
        if (datePosted != other.datePosted) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid
        result = 31 * result + (datePosted?.hashCode() ?: 0)
        return result
    }
}
