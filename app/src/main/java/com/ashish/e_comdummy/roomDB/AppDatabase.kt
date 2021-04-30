package com.ashish.e_comdummy.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ashish.e_comdummy.model.ProductItem

/**
 * Created by Ashish Tiwari on 30,April,2021
 */
@Database(entities = [ProductItem::class], version = 1, exportSchema = false)
 abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "product-list.db"
        )
            .build()
    }
}