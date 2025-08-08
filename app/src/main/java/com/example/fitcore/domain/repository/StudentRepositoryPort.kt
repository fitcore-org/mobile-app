package com.example.fitcore.domain.repository

import com.example.fitcore.infrastructure.data.remote.dto.StudentResponseDto
import java.io.File

interface StudentRepositoryPort {
    suspend fun getStudentByEmail(email: String): StudentResponseDto?
    suspend fun updateStudent(id: String, updates: Map<String, String>): StudentResponseDto?
    suspend fun uploadProfilePhoto(studentId: String, photoFile: File): StudentResponseDto?
    suspend fun updateProfilePhoto(studentId: String, photoFile: File): StudentResponseDto?
}