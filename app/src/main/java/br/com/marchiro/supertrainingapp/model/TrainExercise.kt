package br.com.marchiro.supertrainingapp.model

import androidx.room.DatabaseView


@DatabaseView("""SELECT Train.trainId AS trainId, Exercise.exerciseId AS exerciseId, Exercise.img AS exerciseImg, Exercise.name 
    as exerciseName, TrainExerciseCrossRef.position, TrainExerciseCrossRef.id AS crossRefId FROM Train JOIN Exercise, TrainExerciseCrossRef ON 
TrainExerciseCrossRef.trainId = Train.trainId AND TrainExerciseCrossRef.trainExerciseId = Exercise.exerciseId""")
data class TrainExercise(
    val crossRefId: String,
    val trainId : String,
    val exerciseId: String,
    val exerciseName: String,
    val exerciseImg: String?,
    val position: Int
)