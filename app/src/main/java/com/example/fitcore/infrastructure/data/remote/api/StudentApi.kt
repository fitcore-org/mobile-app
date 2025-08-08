package com.example.fitcore.infrastructure.data.remote.api

import com.example.fitcore.infrastructure.data.remote.dto.StudentResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface que define os endpoints da API relacionados ao estudante.
 * Utilizada pelo Retrofit para gerar as implementações das chamadas de rede.
 */
interface StudentApi {

    /**
     * Busca um estudante pelo seu email.
     */
    @GET("api/students/email/{email}")
    suspend fun getStudentByEmail(@Path("email") email: String): Response<StudentResponseDto>

    /**
     * Atualiza os dados de um estudante pelo seu id.
     */
    @PUT("api/students/{id}")
    suspend fun updateStudent(
        @Path("id") id: String,
        @Body updates: Map<String, String>
    ): Response<StudentResponseDto>

    /**
     * Faz upload da foto de perfil do estudante.
     */
    @Multipart
    @POST("api/students/{studentId}/profile")
    suspend fun uploadProfilePhoto(
        @Path("studentId") studentId: String,
        @Part file: MultipartBody.Part
    ): Response<StudentResponseDto>

    /**
     * Atualiza a foto de perfil do estudante.
     */
    @Multipart
    @PUT("api/students/{studentId}/profile")
    suspend fun updateProfilePhoto(
        @Path("studentId") studentId: String,
        @Part file: MultipartBody.Part
    ): Response<StudentResponseDto>
}
