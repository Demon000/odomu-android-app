package com.demon000.odomu.models

data class AreaAddData(
    val name: String,
    val category: String,
    val location: String,
    val locationPoint: List<Float>,
)
