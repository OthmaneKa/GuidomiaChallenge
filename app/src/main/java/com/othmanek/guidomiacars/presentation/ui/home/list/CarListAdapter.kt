package com.othmanek.guidomiacars.presentation.ui.home.list

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.othmanek.guidomiacars.R
import com.othmanek.guidomiacars.common.getPriceToDisplay
import com.othmanek.guidomiacars.databinding.ItemLayoutBinding
import com.othmanek.guidomiacars.domain.entity.Car

class CarListAdapter : RecyclerView.Adapter<CarListAdapter.ViewHolder>() {

    private var data: List<Car> = emptyList()
    private var filteredData: List<Car> = emptyList()

    inner class ViewHolder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.binding.root.context
        with(holder) {
            with(filteredData[position]) {
                binding.itemCarPrice.text = marketPrice.getPriceToDisplay()
                binding.itemCarTitle.text = model
                binding.itemRatingsContainer.removeAllViews()
                if (rating != 0) {
                    val list = getRatingDrawables(context, rating)
                    list.forEach {
                        binding.itemRatingsContainer.addView(it)
                    }
                }
                image?.let {
                    binding.itemImageView.setImageDrawable(ContextCompat.getDrawable(context, it))
                }

                binding.subItemContainer.visibility = if (isExpanded) View.VISIBLE else View.GONE
                binding.mainContainer.setOnClickListener {
                    shouldExpandSection(position)
                }
                binding.subItemProsContainer.apply {
                    this.removeAllViews()
                    for (pros in prosList) {
                        this.addView(createBulletPoints(pros, context))
                    }
                }

                binding.subItemConsContainer.apply {
                    this.removeAllViews()
                    for (cons in consList) {
                        this.addView(createBulletPoints(cons, context))
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    fun updateList(data: List<Car>) {
        this.data = data
        filteredData = data
        notifyDataSetChanged()
    }

    fun filterList(filterValue: MutableList<String>) {
        filteredData = data
        if (filterValue[0].isNotEmpty() && filterValue[1].isEmpty()) {
            filteredData = filteredData.filter { it.make == filterValue[0] }
        } else if (filterValue[0].isEmpty() && filterValue[1].isNotEmpty()) {
            filteredData = filteredData.filter { it.model == filterValue[1] }
        }
        notifyDataSetChanged()
    }

    private fun getRatingDrawables(context: Context, number: Int): List<ImageView> {
        val drawables = mutableListOf<ImageView>()
        for (i in 1..number) {
            val imageView = ImageView(context)
            imageView.apply {
                setImageResource(R.drawable.ic_rating)

                imageTintList = ColorStateList.valueOf(
                    resources.getColor(
                        R.color.orange,
                        context.theme
                    )
                )

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            drawables.add(imageView)
        }
        return drawables
    }

    private fun createBulletPoints(content: String, context: Context): TextView {
        val textView = TextView(context)
        textView.apply {
            text = content
            textSize = 12f
            gravity = Gravity.CENTER_HORIZONTAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_bullet,
                0,
                0,
                0
            )
            setTextColor(resources.getColor(R.color.black, null))
        }
        return textView
    }

    private fun shouldExpandSection(position: Int) {
        data[position].isExpanded = !data[position].isExpanded
        data.map { it ->
            if (data.indexOf(it) != position) {
                it.isExpanded = false
            }
        }
        notifyDataSetChanged()
    }
}