package com.othmanek.guidomiacars.presentation.ui.home.list

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.othmanek.guidomiacars.R
import com.othmanek.guidomiacars.common.getPriceToDisplay
import com.othmanek.guidomiacars.databinding.ItemLayoutBinding
import com.othmanek.guidomiacars.domain.entity.Car

class CarListAdapter : RecyclerView.Adapter<CarListAdapter.ViewHolder>() {

    private var data: List<Car> = emptyList()

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
            with(data[position]) {
                binding.itemCarPrice.text = marketPrice.getPriceToDisplay()
                binding.itemCarTitle.text = model
                if (rating != 0) {
                    val list = getRatingDrawables(context, rating)
                    list.forEach {
                        binding.itemRatingsContainer.addView(it)
                    }
                }
                image?.let {
                    binding.itemImageView.setImageDrawable(ContextCompat.getDrawable(context, it))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateList(data: List<Car>) {
        this.data = data
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


}