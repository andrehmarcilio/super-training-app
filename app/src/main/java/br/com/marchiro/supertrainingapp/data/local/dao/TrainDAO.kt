package br.com.marchiro.supertrainingapp.data.local.dao

import androidx.room.*
import br.com.marchiro.supertrainingapp.model.Train

@Dao
interface TrainDAO {

    @Query("SELECT * FROM train")
    suspend fun getAllTrain(): List<Train>

    @Query("SELECT * FROM train WHERE trainId = :id")
    suspend fun getTrainById(id: String): Train?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(train: Train)

    @Query("UPDATE Train SET disable = 1 WHERE trainId = :id")
    suspend fun disable(id: String)

    @Update
    suspend fun update(train: Train)

}