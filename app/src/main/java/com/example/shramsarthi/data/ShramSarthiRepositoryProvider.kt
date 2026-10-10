package com.example.shramsarthi.data

import com.example.shramsarthi.data.firebase.FirebaseAuthRepository
import com.example.shramsarthi.data.firebase.FirestoreComplaintRepository
import com.example.shramsarthi.data.firebase.FirestoreJobApplicationRepository
import com.example.shramsarthi.data.firebase.FirestoreJobRepository
import com.example.shramsarthi.data.firebase.FirestoreUserRepository
import com.example.shramsarthi.data.repository.AuthRepository
import com.example.shramsarthi.data.repository.ComplaintRepository
import com.example.shramsarthi.data.repository.JobApplicationRepository
import com.example.shramsarthi.data.repository.JobRepository
import com.example.shramsarthi.data.repository.UserRepository

/**
 * Centralized service provider for Firebase-backed repository instances.
 */
object ShramSarthiRepositoryProvider {
    val authRepository: AuthRepository by lazy { FirebaseAuthRepository() }
    val userRepository: UserRepository by lazy { FirestoreUserRepository() }
    val jobRepository: JobRepository by lazy { FirestoreJobRepository() }
    val jobApplicationRepository: JobApplicationRepository by lazy { FirestoreJobApplicationRepository() }
    val complaintRepository: ComplaintRepository by lazy { FirestoreComplaintRepository() }
}
