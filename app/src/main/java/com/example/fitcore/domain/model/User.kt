package com.example.fitcore.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String? = null,
    val cpf: String? = null,
    val birthDate: String? = null,
    val height: Double? = null,
    val weight: Double? = null,
    val plan: String? = null,
    val profileImageUrl: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val isActive: Boolean = true,
    val photoUrl: String? = null 
) : Parcelable