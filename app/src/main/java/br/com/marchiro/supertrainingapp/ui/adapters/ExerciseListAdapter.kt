package br.com.marchiro.supertrainingapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.marchiro.supertrainingapp.databinding.ExerciseRvItemBinding
import br.com.marchiro.supertrainingapp.extensions.tryToLoad
import br.com.marchiro.supertrainingapp.model.Exercise

class ExerciseListAdapter(exercises: List<Exercise> = listOf(), private val exerciseClick: (String) -> Unit) :
    RecyclerView.Adapter<ExerciseListAdapter.ExerciseListViewHolder>() {

    private val exercises: MutableList<Exercise> = exercises.toMutableList()

    inner class ExerciseListViewHolder(private val binding: ExerciseRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) {
            itemView.setOnClickListener {
                binding.exerciseRvItemCheckBox.isChecked = !binding.exerciseRvItemCheckBox.isChecked
                exerciseClick(exercise.exerciseId)
            }
            with(binding) {
                exerciseRvItemIv.tryToLoad(exercise.img)
                exerciseRvItemTitle.text = exercise.name
                exerciseRvItemSubtitle.text = exercise.description
                exerciseRvItemCheckBox.setOnClickListener {
                    exerciseClick(exercise.exerciseId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseListViewHolder {
        return ExerciseListViewHolder(
            ExerciseRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseListViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount(): Int = exercises.size


    fun update(allExercises: List<Exercise>) {
        exercises.clear()
        exercises.addAll(allExercises)
        notifyDataSetChanged()
    }

}
