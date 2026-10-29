package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [AppContentEntity::class, FeedbackEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NaihatiDatabase : RoomDatabase() {
    abstract fun dao(): NaihatiDao

    companion object {
        /**
         * Adds the restaurant catalog without dropping the feedback queue, so unsynced
         * community reports survive the upgrade.
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE app_content ADD COLUMN restaurantsJson TEXT"
                )
            }
        }

        @Volatile
        private var INSTANCE: NaihatiDatabase? = null

        fun getInstance(context: Context): NaihatiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NaihatiDatabase::class.java,
                    "naihati_nights.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
