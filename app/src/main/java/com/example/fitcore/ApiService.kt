package com.example.fitcore
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
data class Post(val userId: Int, val id: Int, val title: String, val body: String)
data class User(val id: Int, val name: String, val username: String, val email: String)
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val user: User)
interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") id: Int): User
    @GET("posts")
    suspend fun getPostsForUser(@Query("userId") userId: Int): List<Post>
}