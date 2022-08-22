package br.com.marchiro.supertrainingapp.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import br.com.marchiro.supertrainingapp.databinding.ImagemTrainDialogBinding
import br.com.marchiro.supertrainingapp.extensions.tryToLoad

class ImageTrainDialog(val context: Context) {

    fun show(url: String?, onSave: (String?) -> Unit) {
        val binding = ImagemTrainDialogBinding.inflate(LayoutInflater.from(context))
        binding.imagemTrainDialogIv.tryToLoad(url)
        binding.imagemTrainDialogEt.setText(url)
        binding.imagemTrainDialogBtnRefresh.setOnClickListener {
            binding.imagemTrainDialogIv.tryToLoad(binding.imagemTrainDialogEt.text.toString())
        }
        AlertDialog.Builder(context)
            .setView(binding.root)
            .setNegativeButton("Cancelar") {_, _ ->

            }
            .setPositiveButton("Salvar") {_, _ ->
                onSave(binding.imagemTrainDialogEt.text.toString())
            }.show()
    }
}