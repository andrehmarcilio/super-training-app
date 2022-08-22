package br.com.marchiro.supertrainingapp.data.local.dao

import androidx.room.*
import br.com.marchiro.supertrainingapp.model.TrainExercise
import br.com.marchiro.supertrainingapp.model.TrainExerciseCrossRef

@Dao
interface TrainExcerciseDAO {

    @Query("SELECT * FROM TrainExercise WHERE trainId = :trainId")
    suspend fun getExercisesfromTrain(trainId: String): List<TrainExercise>?

    @Query("SELECT COUNT(*) FROM TrainExerciseCrossRef WHERE trainId = :id")
    suspend fun getExercisesfromTrainLength(id: String): Int

    @Query("UPDATE TrainExerciseCrossRef SET position = :newPositiion WHERE id = :crossRefId")
    suspend fun updateExercisePosition(crossRefId: String, newPositiion: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trainExerciseCrossRef: TrainExerciseCrossRef)

    @Query("DELETE FROM TrainExerciseCrossRef WHERE id = :crossRefId")
    suspend fun delete(crossRefId: String)

    @Update
    suspend fun update(trainExerciseCrossRef: TrainExerciseCrossRef)

}