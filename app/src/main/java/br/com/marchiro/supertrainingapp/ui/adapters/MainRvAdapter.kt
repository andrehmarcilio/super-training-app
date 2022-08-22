package br.com.marchiro.supertrainingapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.marchiro.supertrainingapp.databinding.MainRvItemBinding
import br.com.marchiro.supertrainingapp.extensions.timeFormaterMinutes
import br.com.marchiro.supertrainingapp.extensions.tryToLoad
import br.com.marchiro.supertrainingapp.model.Train

class MainRvAdapter(
    val context: Context,
    trains: List<Train> = listOf(),
    val navigate: (String) -> Unit
) : RecyclerView.Adapter<MainRvAdapter.ViewHolder>() {
    private val trains : MutableList<Train> = trains.toMutableList()

    inner class ViewHolder(val binding: MainRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(train: Train) {
            with(binding) {
                mainRvItemTitle.text = train.title
                mainRvItemSubtitle.text = train.description
                mainRvItemDuration.text = train.duration.timeFormaterMinutes()
                mainRvItemIv.tryToLoad(train.img)
                root.setOnClickListener {
                    navigate(train.trainId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MainRvItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trains[position])
    }

    override fun getItemCount(): Int = trains.size

    fun update(newList: List<Train>) {
        trains.clear()
        trains.addAll(newList)
        notifyDataSetChanged()
    }

}
