package br.com.marchiro.supertrainingapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.marchiro.supertrainingapp.databinding.TrainExercisesRvItemBinding
import br.com.marchiro.supertrainingapp.extensions.tryToLoad
import br.com.marchiro.supertrainingapp.model.TrainExercise
import br.com.marchiro.supertrainingapp.ui.reorder.ItemTouchHelperAdapter
import br.com.marchiro.supertrainingapp.ui.reorder.OnStartDragListener
import java.util.*

class TrainExercisesListAdapter(
    exercises: List<TrainExercise> = listOf(),
    private val onExerciseClick: () -> Unit = {},
    private val dragStartListener : OnStartDragListener,
    private val onSwipeEnd : (String) -> Unit,
    private val updatePositions: (Int, String) -> Unit
) : RecyclerView.Adapter<TrainExercisesListAdapter.TrainExerciseViewHolder>(),
    ItemTouchHelperAdapter {

    private val exercises: MutableList<TrainExercise> = exercises.toMutableList()

    inner class TrainExerciseViewHolder(private val binding: TrainExercisesRvItemBinding,
                                        private val dragStartListener : OnStartDragListener? = null) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: TrainExercise) {
            itemView.setOnClickListener {
                onExerciseClick()
            }
            with(binding) {
                trainExerciseRvItemTitle.text = exercise.exerciseName
                trainExerciseRvItemIv.tryToLoad(exercise.exerciseImg)
                trainExerciseRvReorderIc.setOnGenericMotionListener { _, motionEvent ->
                    if (MotionEventCompat.isFromSource(motionEvent, MotionEvent.ACTION_DOWN)) {
                        dragStartListener?.onStartDrag(this@TrainExerciseViewHolder)
                    }
                    false
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainExerciseViewHolder {
        val binding =
            TrainExercisesRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrainExerciseViewHolder(binding, dragStartListener)
    }

    override fun onBindViewHolder(holder: TrainExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount(): Int = exercises.size


    fun update(list: List<TrainExercise>) {
        exercises.clear()
        exercises.addAll(list)
        notifyDataSetChanged()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(exercises, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }



    override fun onItemMoveEnd() {
        refreshPositions()
    }

    override fun onItemDismiss(position: Int) {
        Log.i("DetalhesLegais", "onItemDismiss: position $position")
        onSwipeEnd(exercises[position].crossRefId)
        exercises.removeAt(position)
        notifyItemRemoved(position)
        refreshPositions()
    }

    private fun refreshPositions() {
        exercises.forEachIndexed { index, trainExercise ->
            updatePositions(index, trainExercise.crossRefId)
        }
    }

}
