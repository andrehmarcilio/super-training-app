package br.com.marchiro.supertrainingapp.ui.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import br.com.marchiro.supertrainingapp.data.local.AppDatabase
import br.com.marchiro.supertrainingapp.databinding.ActivityTrainFormBinding
import br.com.marchiro.supertrainingapp.extensions.tryToLoad
import br.com.marchiro.supertrainingapp.model.Train
import br.com.marchiro.supertrainingapp.ui.dialogs.ImageTrainDialog
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import java.util.*

class TrainFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainFormBinding
    private var url: String? = null
    private var trainId: String? = null

    private val dao by lazy {
        AppDatabase.instance(this).trainDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainFormBinding.inflate(layoutInflater)
        title = "Criar novo Treino"
        setContentView(binding.root)
        getInfoIntent()
        configImage()
        configSaveBtn()
    }

    private fun getInfoIntent() {
        trainId = intent.getStringExtra(TRAIN_ID_KEY)
        trainId?.let {
            lifecycleScope.launch {
                bindElements(it)
            }
        }
    }

    private suspend fun bindElements(id: String) {
        dao.getTrainById(id)?.let { train ->
            with(binding) {
                trainFormEtTitle.setText(train.title)
                trainFormEtDescription.setText(train.description)
                trainFormEtDuration.setText(train.duration.toString())
                trainFormIv.tryToLoad(train.img)
                url = train.img
                title = "Alterar Treino"
            }
        }
    }

    private fun configSaveBtn() {
        binding.trainFormBtnSave.setOnClickListener {
            if (validateForm()) {
                val train: Train = createTrain()
                lifecycleScope.launch {
                    dao.insert(train)
                    finish()
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        var valid = true
        with(binding) {
            if (trainFormEtTitle.text.toString().isEmpty()) {
                trainFormTilTitle.error = "Preencha este campo"
                valid = false
            }

            if (trainFormEtDescription.text.toString().isEmpty()) {
                trainFormTilDescription.error = "Preencha este campo"
                valid = false
            }

            if (trainFormEtDuration.text.toString().isEmpty()) {
                trainFormTilDuration.error = "Preencha este campo"
                valid = false
            }

            try {
                trainFormEtDuration.text.toString().toInt()
            } catch (e: NumberFormatException) {
                trainFormTilDuration.error = "Número inválido!"
                valid = false
            }

            return valid

        }
    }

    private fun createTrain(): Train {
        with(binding) {
            return Train(
                trainId = trainId ?: UUID.randomUUID().toString(),
                title = trainFormEtTitle.text.toString(),
                description = trainFormEtDescription.text.toString(),
                duration = trainFormEtDuration.text.toString().toInt(),
                img = url
            )
        }
    }


    private fun configImage() {
        binding.trainFormIv.let { iv ->
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