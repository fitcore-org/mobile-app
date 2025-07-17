package com.example.fitcore.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutItem(
    val id: String,
    val exercise: Exercise,
    val sets: String,
    val reps: String,
    val restSeconds: Int,
    val observation: String?,
    val order: Int
) : Parcelable
