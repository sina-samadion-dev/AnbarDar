package com.sidofa.anbardar.datalayer.typeconverters

import androidx.room.TypeConverter
import java.time.Instant

class InstantTypeConverter {
    @TypeConverter
    fun fromEpochMillis(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun toEpochMillis(value: Instant?): Long? {
        return value?.toEpochMilli()
    }
}