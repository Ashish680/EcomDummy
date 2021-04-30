package com.ashish.e_comdummy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Ashish Tiwari on 30,April,2021
 */
@Entity(tableName = "Product")
data class ProductItem(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: String="",

    @ColumnInfo(name = "name")
    val name: String? = "",

    @ColumnInfo(name = "price")
    val price: String? = "",

    @ColumnInfo(name = "detail")
    val detail: String? = "",

    @ColumnInfo(name = "image")
    val image: String? = ""
)
