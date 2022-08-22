package br.com.marchiro.supertrainingapp.ui.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import br.com.marchiro.supertrainingapp.R
import br.com.marchiro.supertrainingapp.data.local.AppDatabase
import br.com.marchiro.supertrainingapp.databinding.ActivityExercisesListBinding
import br.com.marchiro.supertrainingapp.model.TrainExerciseCrossRef
import br.com.marchiro.supertrainingapp.ui.adapters.ExerciseListAdapter
import kotlinx.coroutines.launch

class ExercisesListActivity : AppCompatActivity() {

    private val idsSelecteds = mutableListOf<String>()

    private var trainId: String? = null

    private val adapter = ExerciseListAdapter { id ->
        if (idsSelecteds.contains(id)) {
            idsSelecteds.remove(id)
        } else {
            idsSelecteds.add(id)
        }
        binding.exerciseListBtn.isEnabled = idsSelecteds.isNotEmpty()
    }

    private val binding by lazy {
        ActivityExercisesListBinding.inflate(layoutInflater)
    }

    private val exerciseDao by lazy {
        AppDatabase.instance(this).exerciseDao()
    }

    private val trainExerciseDAO by lazy {
        AppDatabase.instance(this).trainExerciseDao()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Adicionar exercícios ao treino"
        binding.exerciseListRv.adapter = adapter
        getTrainId()
        configBtn()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            adapter.update(exerciseDao.getAllExercises())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.exercises_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exercise_list_menu_add -> {
                goToExerciseForm()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configBtn() {
        trainId?.let { id ->
            binding.exerciseListBtn.isEnabled = false
            binding.exerciseListBtn.setOnClickListener {
                lifecycleScope.launch {
                    val position =
                        trainExerciseDAO.getExercisesfromTrainLength(id)
                    idsSelecteds.forEachIndexed { index, exerciseId ->
                        trainExerciseDAO.insert(
                            TrainExerciseCrossRef(
                                trainId = id,
                                trainExerciseId = exerciseId,
                                position = position + index
                            )
                        )
                    }
                    finish()
                }
            }
        }
    }

    private fun goToExerciseForm() {
        Intent(this, ExerciseFormActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun getTrainId() {
        trainId = intent.getStringExtra(TRAIN_ID_KEY)
        if (trainId == null) finish()
    }


}
