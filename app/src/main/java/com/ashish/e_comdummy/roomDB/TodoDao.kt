package com.ashish.e_comdummy.roomDB

import androidx.room.*
import com.ashish.e_comdummy.model.ProductItem

/**
 * Created by Ashish Tiwari on 30,April,2021
 */
@Dao
interface TodoDao {
    @Query("SELECT * FROM product")
    fun getAll(): List<ProductItem>


    @Query("SELECT * FROM product WHERE id LIKE :id")
    fun findById(id: String): List<ProductItem>


    @Insert
    fun insert(todo: ProductItem)

    @Delete
    fun delete(todo: ProductItem)

    @Update
    fun updateTodo(vararg todo: ProductItem)
}