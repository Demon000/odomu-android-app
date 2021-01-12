package com.demon000.odomu.models

data class Area(
    val name: String,
    val id: String,
    val owner: User,
    val category: Number,
    val location: String,
    val locationPoint: List<Float>,
    val image: String?,
    val updatedAtTimestamp: Number
)
