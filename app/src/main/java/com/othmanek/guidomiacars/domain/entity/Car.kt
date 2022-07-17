package com.othmanek.guidomiacars.domain.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.othmanek.guidomiacars.R
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class Car(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val make: String,
    val model: String,
    val customerPrice: Double,
    val marketPrice: Double,
    val rating: Int,
    val prosList: List<String>,
    val consList: List<String>,


    ) {
    fun setImage() {
        when (model) {
            "GLE coupe" -> image = R.drawable.mercedes_benz_glc
            "3300i" -> image = R.drawable.bmw_3300i
            "Roadster" -> image = R.drawable.alpine_roadster
            "Range Rover" -> image = R.drawable.range_rover
        }
    }

    @Ignore
    @Transient
    var expanded: Boolean = false

    @Ignore
    @Transient
    var image: Int? = null
}
