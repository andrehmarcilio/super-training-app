package br.com.marchiro.supertrainingapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.marchiro.supertrainingapp.data.local.dao.ExerciseDAO
import br.com.marchiro.supertrainingapp.data.local.dao.TrainDAO
import br.com.marchiro.supertrainingapp.data.local.dao.TrainExcerciseDAO
import br.com.marchiro.supertrainingapp.model.Exercise
import br.com.marchiro.supertrainingapp.model.Train
import br.com.marchiro.supertrainingapp.model.TrainExercise
import br.com.marchiro.supertrainingapp.model.TrainExerciseCrossRef

@Database(
    entities = [Train::class, Exercise::class, TrainExerciseCrossRef::class],
    version = 1,
    exportSchema = false,
    views = [TrainExercise::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trainDao(): TrainDAO
    abstract fun exerciseDao(): ExerciseDAO
    abstract fun trainExerciseDao(): TrainExcerciseDAO


    companion object {
        private lateinit var database: AppDatabase

        fun instance(context: Context): AppDatabase {
            if (Companion::database.isInitialized) {
                return database
            }
            database = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "super-training.db"
            ).build()

            return database
        }
    }
}