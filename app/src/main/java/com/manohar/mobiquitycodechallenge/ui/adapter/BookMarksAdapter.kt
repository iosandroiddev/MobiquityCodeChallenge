package com.manohar.mobiquitycodechallenge.ui.adapter

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manohar.mobiquitycodechallenge.R
import com.manohar.mobiquitycodechallenge.databinding.ItemBookmarkBinding
import com.manohar.mobiquitycodechallenge.model.BookmarkLocationModel
import com.manohar.mobiquitycodechallenge.utils.getContextDrawable
import com.manohar.mobiquitycodechallenge.utils.hide
import com.manohar.mobiquitycodechallenge.utils.show

class BookMarksAdapter(private val context: Context, val bookMarkListener: IBookMarkClicked) :
    RecyclerView.Adapter<BookMarksAdapter.BookMarksViewHolder>() {

    private var selectedIndex = -1
    private var selectedItems = SparseBooleanArray()
    private var _pinnedLocationsArray = ArrayList<BookmarkLocationModel>()

    interface IBookMarkClicked {
        fun onBookMarkClicked(pos: Int, bookMarkLocationModel: BookmarkLocationModel)
        fun onBookMarkLongLicked(pos: Int, bookMarkLocationModel: BookmarkLocationModel)
    }

    fun addItems(array: ArrayList<BookmarkLocationModel>) {
        _pinnedLocationsArray = array
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return _pinnedLocationsArray.size
    }

    fun getItem(position: Int): BookmarkLocationModel {
        return _pinnedLocationsArray[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarksViewHolder {
        return BookMarksViewHolder(
            ItemBookmarkBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BookMarksViewHolder, position: Int) {
        val bookMarkedItem = getItem(position)
        holder.binding.dataModel = bookMarkedItem
        toggleSelectionIndicator(holder, position)
    }

    private fun toggleSelectionIndicator(holder: BookMarksViewHolder, position: Int) {
        if (selectedItems.get(position, false)) {
            holder.binding.layoutBookMarkItem.background =
                context.getContextDrawable(R.drawable.bg_select_item)
            holder.binding.ivTick.show()
            if (selectedIndex == position)
                selectedIndex = -1
        } else {
            holder.binding.ivTick.hide()
            holder.binding.layoutBookMarkItem.background =
                context.getContextDrawable(R.drawable.bg_bookmark_item)
            if (selectedIndex == position)
                selectedIndex = -1
        }
    }

    inner class BookMarksViewHolder(var binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.layoutBookMarkItem.setOnClickListener {
                bookMarkListener.onBookMarkClicked(adapterPosition, getItem(adapterPosition))
            }
            binding.layoutBookMarkItem.setOnLongClickListener {
                bookMarkListener.onBookMarkLongLicked(adapterPosition, getItem(adapterPosition))
                return@setOnLongClickListener true
            }
        }
    }

    fun removeAllSelections() {
        clearSelection()
        selectedIndex = -1
    }

    fun toggleSelection(position: Int) {
        selectedIndex = position
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position)
        } else {
            selectedItems.put(position, true)
        }
        notifyItemChanged(position)
    }

    fun selectedItemCount(): Int {
        return selectedItems.size()
    }

    fun getSelectedItems(): ArrayList<Int> {
        val items: ArrayList<Int> = ArrayList(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(selectedItems.keyAt(i))
        }
        return items
    }

    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged()
    }
}