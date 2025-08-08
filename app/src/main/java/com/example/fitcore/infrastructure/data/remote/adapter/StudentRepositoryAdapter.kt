package com.example.fitcore.infrastructure.data.remote.adapter

import com.example.fitcore.domain.repository.StudentRepositoryPort
import com.example.fitcore.infrastructure.data.remote.api.RetrofitInstance
import com.example.fitcore.infrastructure.data.remote.dto.StudentResponseDto
import android.util.Log
import java.io.File
import android.os.Environment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody

class StudentRepositoryAdapter : StudentRepositoryPort {
    private fun logToFile(message: String) {
        try {
            val baseDir = Environment.getExternalStorageDirectory()
            val logFile = File(baseDir, "fitcore_debug.txt")
            logFile.appendText("${System.currentTimeMillis()}: $message\n")
        } catch (e: Exception) {
            Log.e("StudentRepositoryAdapter", "Erro ao salvar log", e)
        }
    }
    private val studentApi = RetrofitInstance.studentApi

    override suspend fun getStudentByEmail(email: String): StudentResponseDto? {
        return try {
            val response = studentApi.getStudentByEmail(email)
            if (response.isSuccessful) {
                response.body()
            } else {
                // Logar o erro da API pode ajudar a debugar
                Log.e("StudentRepositoryAdapter", "Error getting student: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("StudentRepositoryAdapter", "Exception getting student", e)
            null
        }
    }

    // A função duplicada foi removida. Esta é a única implementação.
    override suspend fun updateStudent(id: String, updates: Map<String, String>): StudentResponseDto? {
        return try {
            logToFile("=== INÍCIO DA ATUALIZAÇÃO ===")
            logToFile("ID do estudante: $id")
            logToFile("Dados para atualizar: $updates")
            
            val response = studentApi.updateStudent(id, updates)
            logToFile("Código de resposta: ${response.code()}")
            
            if (response.isSuccessful) {
                val responseBody = response.body()
                logToFile("Atualização bem sucedida. Resposta: $responseBody")
                responseBody
            } else {
                val errorBody = response.errorBody()?.string()
                logToFile("ERRO NA ATUALIZAÇÃO:")
                logToFile("Código: ${response.code()}")
                logToFile("Mensagem: ${response.message()}")
                logToFile("Corpo do erro: $errorBody")
                logToFile("=== FIM DO ERRO ===")
                throw Exception("Falha ao atualizar: ${response.code()} - $errorBody")
            }
        } catch (e: Exception) {
            logToFile("EXCEÇÃO AO ATUALIZAR:")
            logToFile("Tipo: ${e.javaClass.simpleName}")
            logToFile("Mensagem: ${e.message}")
            logToFile("StackTrace: ${e.stackTraceToString()}")
            logToFile("=== FIM DA EXCEÇÃO ===")
            throw e
        }
    }

    override suspend fun uploadProfilePhoto(studentId: String, photoFile: File): StudentResponseDto? {
        return try {
            logToFile("=== INÍCIO DO UPLOAD DE FOTO ===")
            logToFile("ID do estudante: $studentId")
            logToFile("Arquivo: ${photoFile.absolutePath}")

            // Cria o MultipartBody.Part com o arquivo da foto
            val requestFile = photoFile.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", photoFile.name, requestFile)

            val response = studentApi.uploadProfilePhoto(studentId, body)
            logToFile("Código de resposta: ${response.code()}")

            if (response.isSuccessful) {
                val responseBody = response.body()
                logToFile("Upload bem sucedido. Resposta: $responseBody")
                responseBody
            } else {
                val errorBody = response.errorBody()?.string()
                logToFile("ERRO NO UPLOAD:")
                logToFile("Código: ${response.code()}")
                logToFile("Mensagem: ${response.message()}")
                logToFile("Corpo do erro: $errorBody")
                logToFile("=== FIM DO ERRO ===")
                throw Exception("Falha ao fazer upload: ${response.code()} - $errorBody")
            }
        } catch (e: Exception) {
            logToFile("EXCEÇÃO NO UPLOAD:")
            logToFile("Tipo: ${e.javaClass.simpleName}")
            logToFile("Mensagem: ${e.message}")
            logToFile("StackTrace: ${e.stackTraceToString()}")
            logToFile("=== FIM DA EXCEÇÃO ===")
            throw e
        }
    }

    override suspend fun updateProfilePhoto(studentId: String, photoFile: File): StudentResponseDto? {
        return try {
            logToFile("=== INÍCIO DA ATUALIZAÇÃO DE FOTO ===")
            logToFile("ID do estudante: $studentId")
            logToFile("Arquivo: ${photoFile.absolutePath}")

            // Cria o MultipartBody.Part com o arquivo da foto
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), photoFile)
            val body = MultipartBody.Part.createFormData("file", photoFile.name, requestFile)

            val response = studentApi.updateProfilePhoto(studentId, body)
            logToFile("Código de resposta: ${response.code()}")

            if (response.isSuccessful) {
                val responseBody = response.body()
                logToFile("Atualização bem sucedida. Resposta: $responseBody")
                responseBody
            } else {
                val errorBody = response.errorBody()?.string()
                logToFile("ERRO NA ATUALIZAÇÃO:")
                logToFile("Código: ${response.code()}")
                logToFile("Mensagem: ${response.message()}")
                logToFile("Corpo do erro: $errorBody")
                logToFile("=== FIM DO ERRO ===")
                throw Exception("Falha ao atualizar: ${response.code()} - $errorBody")
            }
        } catch (e: Exception) {
            logToFile("EXCEÇÃO AO ATUALIZAR:")
            logToFile("Tipo: ${e.javaClass.simpleName}")
            logToFile("Mensagem: ${e.message}")
            logToFile("StackTrace: ${e.stackTraceToString()}")
            logToFile("=== FIM DA EXCEÇÃO ===")
            throw e
        }
    }
}
