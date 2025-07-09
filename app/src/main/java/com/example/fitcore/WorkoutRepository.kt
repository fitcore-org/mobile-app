package com.example.fitcore
class WorkoutRepository {
    suspend fun getWorkoutsForUser(userId: Int): List<Post> {
        return try { RetrofitInstance.api.getPostsForUser(userId) } catch (e: Exception) { emptyList() }
    }
}