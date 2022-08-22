package br.com.marchiro.supertrainingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise(
    @PrimaryKey
    val exerciseId: String,
    val name: String,
    val description: String,
    val img: String?,
    val disable: Int = 0
)