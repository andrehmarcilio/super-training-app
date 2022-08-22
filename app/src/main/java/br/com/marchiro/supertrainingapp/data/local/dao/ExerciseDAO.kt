package br.com.marchiro.supertrainingapp.data.local.dao

import androidx.room.*
import br.com.marchiro.supertrainingapp.model.Exercise

@Dao
interface ExerciseDAO {

    @Query("SELECT * FROM Exercise")
    suspend fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM Exercise WHERE exerciseId = :id")
    suspend fun getExerciseById(id: String): Exercise?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: Exercise)

    @Query("UPDATE Exercise SET disable = 1 WHERE exerciseId = :id")
    suspend fun disable(id: String)

    @Update
    suspend fun update(exercise: Exercise)
}