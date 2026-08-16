package com.example.taskflow.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        TaskEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(
    PriorityConverters::class
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        @Volatile
        private var INSTANCE: TaskDatabase? = null

        private val MIGRATION_1_2 =
            object : Migration(1, 2) {
                override fun migrate(
                    database: SupportSQLiteDatabase
                ) {
                    database.execSQL(
                        """
                        ALTER TABLE tasks
                        ADD COLUMN createdAtMillis
                        INTEGER NOT NULL DEFAULT 0
                        """.trimIndent()
                    )

                    database.execSQL(
                        """
                        ALTER TABLE tasks
                        ADD COLUMN completedAtMillis
                        INTEGER
                        """.trimIndent()
                    )
                    database.execSQL(
                        """
                        UPDATE tasks
                        SET createdAtMillis = dueDateMillis
                        WHERE createdAtMillis = 0
                        """.trimIndent()
                    )
                    database.execSQL(
                        """
                        UPDATE tasks
                        SET completedAtMillis = dueDateMillis
                        WHERE completed = 1
                        AND completedAtMillis IS NULL
                        """.trimIndent()
                    )
                }
            }
        private val MIGRATION_2_3 =
            object : Migration(2, 3) {
                override fun migrate(
                    database: SupportSQLiteDatabase
                ) {
                    database.execSQL(
                        """
                        ALTER TABLE tasks
                        ADD COLUMN dueTime
                        TEXT NOT NULL DEFAULT ''
                        """.trimIndent()
                    )

                    database.execSQL(
                        """
                        ALTER TABLE tasks
                        ADD COLUMN dueDateTimeMillis
                        INTEGER NOT NULL DEFAULT 0
                        """.trimIndent()
                    )

                    database.execSQL(
                        """
                        ALTER TABLE tasks
                        ADD COLUMN reminderEnabled
                        INTEGER NOT NULL DEFAULT 0
                        """.trimIndent()
                    )

                    database.execSQL(
                        """
                        ALTER TABLE tasks
                        ADD COLUMN reminderMinutesBefore
                        INTEGER NOT NULL DEFAULT 30
                        """.trimIndent()
                    )

                    database.execSQL(
                        """
                        UPDATE tasks
                        SET dueDateTimeMillis = dueDateMillis
                        WHERE dueDateTimeMillis = 0
                        """.trimIndent()
                    )
                }
            }

        fun getInstance(
            context: Context
        ): TaskDatabase {
            return INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: Room.databaseBuilder(
                            context.applicationContext,
                            TaskDatabase::class.java,
                            "taskflow_database"
                        )
                            .addMigrations(
                                MIGRATION_1_2,
                                MIGRATION_2_3
                            )
                            .build()
                            .also {
                                INSTANCE = it
                            }
                }
        }
    }
}