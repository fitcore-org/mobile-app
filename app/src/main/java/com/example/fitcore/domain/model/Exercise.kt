package com.example.fitcore.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exercise(
    val id: Int,
    val name: String,
    val series: String,
    val repetitions: String,
    val restTime: String,
    val mediaUrl: String?
) : Parcelable