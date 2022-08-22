package br.com.marchiro.supertrainingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity()
data class Train(
    @PrimaryKey
    val trainId: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val duration: Int,
    val img: String? = null,
    val disable: Int = 0
)