package br.com.marchiro.supertrainingapp.ui.activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.marchiro.supertrainingapp.data.local.AppDatabase
import br.com.marchiro.supertrainingapp.databinding.ActivityExerciseFormBinding
import br.com.marchiro.supertrainingapp.extensions.tryToLoad
import br.com.marchiro.supertrainingapp.model.Exercise
import br.com.marchiro.supertrainingapp.ui.dialogs.ImageTrainDialog
import kotlinx.coroutines.launch
import java.util.*

class ExerciseFormActivity : AppCompatActivity() {

    private var url: String? = null
    private var exerciseId: String? = null

    private val binding by lazy {
        ActivityExerciseFormBinding.inflate(layoutInflater)
    }

    private val exerciseDao by lazy {
        AppDatabase.instance(this).exerciseDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Criar novo Exercício"
        setContentView(binding.root)
        getInfoIntent()
        configImage()
        configSaveBtn()
    }

    private fun getInfoIntent() {
        exerciseId = intent.getStringExtra(EXERCISE_ID_KEY)
        exerciseId?.let {
            bindElements(it)
        }
    }

    private fun bindElements(id: String) {
        lifecycleScope.launch {
            exerciseDao.getExerciseById(id)?.let { exercise ->
                with(binding) {
                    exerciseFormEtTitle.setText(exercise.name)
                    exerciseFormEtDescription.setText(exercise.description)
                    url = exercise.img
                    title = "Alterar Treino"
                }
            }
        }
    }

    private fun configSaveBtn() {
        binding.exerciseFormBtnSave.setOnClickListener {
            if (validateForm()) {
                val exercise: Exercise = createExercise()
                lifecycleScope.launch {
                    exerciseDao.insert(exercise)
                    finish()
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        var valid = true
        with(binding) {
            if (exerciseFormEtTitle.text.toString().isEmpty()) {
                exerciseFormTilTitle.error = "Preencha este campo"
                valid = false
            }

            if (exerciseFormEtDescription.text.toString().isEmpty()) {
                exerciseFormTilDescription.error = "Preencha este campo"
                valid = false
            }

            return valid

        }
    }

    private fun createExercise(): Exercise {
        with(binding) {
            return Exercise(
                exerciseId = exerciseId ?: UUID.randomUUID().toString(),
                name = exerciseFormEtTitle.text.toString(),
                description = exerciseFormEtDescription.text.toString(),
                img = url
            )
        }
    }


    private fun configImage() {
        binding.exerciseFormIv.let { iv ->
            iv.tryToLoad(url)
            iv.setOnClickListener {
                ImageTrainDialog(this).show(url) { newUrl ->
                    url = newUrl
                    iv.tryToLoad(url)
                }
            }
        }

    }
}