package br.com.marchiro.supertrainingapp.ui.reorder

interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    fun onItemDismiss(position: Int)

    fun onItemMoveEnd()
}