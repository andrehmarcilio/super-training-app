package br.com.marchiro.supertrainingapp.ui.activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import br.com.marchiro.supertrainingapp.R
import br.com.marchiro.supertrainingapp.data.local.AppDatabase
import br.com.marchiro.supertrainingapp.databinding.ActivityTrainDetailsBinding
import br.com.marchiro.supertrainingapp.extensions.timeFormaterMinutes
import br.com.marchiro.supertrainingapp.extensions.tryToLoad
import br.com.marchiro.supertrainingapp.model.Train
import br.com.marchiro.supertrainingapp.model.TrainExercise
import br.com.marchiro.supertrainingapp.ui.adapters.TrainExercisesListAdapter
import br.com.marchiro.supertrainingapp.ui.reorder.OnStartDragListener
import br.com.marchiro.supertrainingapp.ui.reorder.ReorderHelperCallback
import kotlinx.coroutines.launch

class TrainDetailsActivity : AppCompatActivity(), OnStartDragListener {
    private lateinit var binding: ActivityTrainDetailsBinding

    private var mItemTouchHelper: ItemTouchHelper? = null

    private val trainDao by lazy {
        AppDatabase.instance(this).trainDao()
    }

    private val trainExerciseDao by lazy {
        AppDatabase.instance(this).trainExerciseDao()
    }

    private var trainId: String? = null

    private lateinit var train: Train

    private lateinit var exercises: List<TrainExercise>

    private val adapter by lazy {
        TrainExercisesListAdapter(
            onExerciseClick = {},
            dragStartListener = this,
            onSwipeEnd = { crossRefId ->
                lifecycleScope.launch {
                    trainExerciseDao.delete(crossRefId)
                }
            })
        { position, crossRefId ->
            Log.i("DetalhesLegais", "position: index $position")
            lifecycleScope.launch {
                trainExerciseDao.updateExercisePosition(
                    crossRefId = crossRefId,
                    newPositiion = position
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trainId = intent.getStringExtra(TRAIN_ID_KEY)

        if (trainId == null) {
            Log.i("DetailsActivityLegal", "onCreate: Fechando Activity")
            finish()
        }
        configBtn()
        configRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        trainId?.let {
            getTrain(it)
            getExercises(it)
        } ?: finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.train_details_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.train_details_menu_delete -> {
                lifecycleScope.launch {
                    trainDao.disable(train.trainId)
                    finish()
                }
            }
            R.id.train_details_menu_edit -> {
                Intent(this, TrainFormActivity::class.java).apply {
                    putExtra(TRAIN_ID_KEY, trainId)
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configRecyclerView() {
        binding.trainDetailsRv.adapter = adapter
        val callback: ItemTouchHelper.Callback = ReorderHelperCallback(adapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper?.attachToRecyclerView(binding.trainDetailsRv)
    }


    private fun configBtn() {
        binding.trainDetailsBtn.setOnClickListener {
            Intent(this, ExercisesListActivity::class.java).apply {
                putExtra(TRAIN_ID_KEY, trainId)
                startActivity(this)
            }
        }
    }

    private fun getTrain(trainId: String) {
        lifecycleScope.launch {
            trainDao.getTrainById(trainId)?.let {
                train = it
                bindTrainInfo(it)
            } ?: finish()
        }
    }

    private fun getExercises(trainId: String) {
        lifecycleScope.launch {
            trainExerciseDao.getExercisesfromTrain(trainId)?.let { exercises ->
                this@TrainDetailsActivity.exercises = exercises
                adapter.update(exercises.sortedBy { it.position })
            }
        }
    }

    private fun bindTrainInfo(train: Train) {
        with(binding) {
            trainDetailsIv.tryToLoad(train.img)
            trainDetailsTvTitle.text = train.title
            trainDetailsTvDescription.text = train.description
            trainDetailsTvDuration.text =
                train.duration.timeFormaterMinutes()
        }
    }


    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        viewHolder?.let {
            mItemTouchHelper?.startDrag(it)
        }
    }
}