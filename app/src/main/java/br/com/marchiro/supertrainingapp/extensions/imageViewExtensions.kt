package br.com.marchiro.supertrainingapp.extensions

import android.widget.ImageView
import br.com.marchiro.supertrainingapp.R
import coil.load

fun ImageView.tryToLoad(url: String?) {
    load(url) {
        placeholder(R.drawable.place_holder)
        fallback(R.drawable.imagem_padrao)
        error(R.drawable.erro)
    }
}