package com.example.shramsarthi.data.repository

import com.example.shramsarthi.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing User profiles.
 */
interface UserRepository {
    suspend fun getUser(uid: String): Result<User?>
    suspend fun saveUser(user: User): Result<Unit>
    suspend fun updateUser(user: User): Result<Unit>
    suspend fun updateRole(uid: String, role: com.example.shramsarthi.model.UserRole, primarySkill: String? = null): Result<Unit>
    fun observeUser(uid: String): Flow<User?>
}
