package com.example.fitcore.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val phone: String?,
    val cpf: String?,
    val birthDate: String?,
    val height: Int?, // CORRIGIDO: Tipo Int?
    val weight: Int?, // CORRIGIDO: Tipo Int?
    val plan: String?,
    val photoUrl: String?
) : Parcelable