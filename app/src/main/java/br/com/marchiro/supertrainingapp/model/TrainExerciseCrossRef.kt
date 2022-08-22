package br.com.marchiro.supertrainingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class TrainExerciseCrossRef(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val trainId: String,
    val trainExerciseId: String,
    val position: Int
)