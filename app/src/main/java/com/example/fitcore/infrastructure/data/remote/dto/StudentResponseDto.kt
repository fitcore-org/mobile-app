package com.example.fitcore.infrastructure.data.remote.dto

import com.google.gson.annotations.SerializedName

// DTO 100% corrigido para corresponder à resposta real da API
data class StudentResponseDto(
    val id: String,
    val name: String,
    val email: String,
    val cpf: String?,
    val phone: String?,
    val birthDate: String?,
    val height: Int?, // CORRIGIDO: É um Int (ex: 175)
    val weight: Int?, // CORRIGIDO: É um Int (ex: 75)
    
    @SerializedName("planType") // Mapeia "planType" do JSON
    val plan: String?,
    
    @SerializedName("profileUrl") // Mapeia "profileUrl" do JSON
    val photoUrl: String?,

    // Campos extras que podem ser úteis no futuro
    val planDescription: String?,
    val bmi: Double?,
    val active: Boolean?
)