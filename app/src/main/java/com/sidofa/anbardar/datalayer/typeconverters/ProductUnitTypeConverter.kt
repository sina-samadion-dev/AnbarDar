package com.sidofa.anbardar.datalayer.typeconverters

import androidx.room.TypeConverter
import com.sidofa.anbardar.utils.enums.ProductUnitTypes

class ProductUnitTypeConverter {
    @TypeConverter
    fun fromProductUnitTypes(value: ProductUnitTypes): Int {
        return value.intValue
    }

    @TypeConverter
    fun toProductUnitTypes(value: Int): ProductUnitTypes {
        return ProductUnitTypes.entries.first { it.intValue == value }
    }
}
