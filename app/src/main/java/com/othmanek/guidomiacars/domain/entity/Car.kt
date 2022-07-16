package com.othmanek.guidomiacars.domain.entity

import com.othmanek.guidomiacars.R
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Car(
    val make: String,
    val model: String,
    val customerPrice: Double,
    val marketPrice: Double,
    val rating: Int,
    val prosList: List<String>,
    val consList: List<String>,
    @Transient
    var image: Int? = null,
    var isExpanded: Boolean = false
) {
    fun setImage() {
        when (model) {
            "GLE coupe" -> image = R.drawable.mercedes_benz_glc
            "3300i" -> image = R.drawable.bmw_3300i
            "Roadster" -> image = R.drawable.alpine_roadster
            "Range Rover" -> image = R.drawable.range_rover
        }
    }
}
