package com.xpayworld.payment.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import com.xpayworld.payment.util.DATABASE_NAME
import com.xpayworld.payment.util.HashMapTypeConverter



@Database(entities = [Transaction::class ],version = 12,exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun transactionDao(): TransactionDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        private var INSTANCE: AppDatabase? = null


        fun getDatabase(context: Context): AppDatabase? {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                ).fallbackToDestructiveMigration()
                        .allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}