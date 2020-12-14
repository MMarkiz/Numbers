package com.sii.numbers.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sii.numbers.data.Model
import com.sii.numbers.R
import com.sii.numbers.databinding.ListItemBinding
import com.squareup.picasso.Picasso


class DataListAdapter(private val onItemSelected: (Model) -> Unit) :
    ListAdapter<Model, DataListAdapter.ViewHolder>(Callback) {

    private var selectedItem: Model? = null

    companion object {
        val Callback = object : DiffUtil.ItemCallback<Model>() {
            override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: Model,
                newItem: Model
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun updateSelectedItem(model: Model?) {
        selectedItem = model
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Model) {
            Picasso.get().load(model.image).into(binding.listItemImage)
            binding.listItemTitle.text = model.name

            if (selectedItem?.name == model.name) {
                setItemBackgroundColor(R.color.colorItemSelected)
                setTextColor(android.R.color.white)
            } else {
                setItemBackgroundColor(android.R.color.transparent)
                setTextColor(android.R.color.black)
            }

            setupListeners(model)
            binding.executePendingBindings()
        }


        private fun setupListeners(model: Model) {
            binding.listItemLayout.run {

                if (selectedItem?.name == model.name) {
                    setOnClickListener(null)
                    setOnTouchListener(null)
                    onFocusChangeListener = null
                } else {
                    setOnClickListener { onItemSelected(model) }

                    setOnFocusChangeListener { v, hasFocus ->
                        if (!hasFocus) {
                            setItemBackgroundColor(android.R.color.transparent)
                            setTextColor(android.R.color.black)
                        }
                    }

                    setOnTouchListener(object : View.OnTouchListener {
                        @SuppressLint("ClickableViewAccessibility")
                        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                            when (event?.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    setItemBackgroundColor(R.color.colorItemTouched)
                                    setTextColor(android.R.color.white)
                                    return false
                                }
                                MotionEvent.ACTION_UP -> {
                                    setItemBackgroundColor(R.color.colorItemFocused)
                                    return false
                                }
                                MotionEvent.ACTION_MOVE -> {
                                    setItemBackgroundColor(android.R.color.transparent)
                                    setTextColor(android.R.color.black)
                                    return true
                                }
                            }
                            return false
                        }
                    })
                }
            }
        }

        private fun setItemBackgroundColor(@ColorRes colorId: Int) {
            binding.listItemLayout.setBackgroundColor(
                ContextCompat.getColor(
                    binding.listItemLayout.context,
                    colorId
                )
            )
        }

        private fun setTextColor(@ColorRes colorId: Int) {
            binding.listItemTitle.setTextColor(
                ContextCompat.getColor(
                    binding.listItemLayout.context,
                    colorId
                )
            )
        }
    }
}