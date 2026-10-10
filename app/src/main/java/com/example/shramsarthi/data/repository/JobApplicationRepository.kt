package com.example.shramsarthi.data.repository

import com.example.shramsarthi.model.ApplicationStatus
import com.example.shramsarthi.model.JobApplication
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing Job Applications submitted by Workers.
 */
interface JobApplicationRepository {
    suspend fun applyForJob(application: JobApplication): Result<String>
    suspend fun getApplicationsByJob(jobId: String): Result<List<JobApplication>>
    suspend fun getApplicationsByWorker(workerId: String): Result<List<JobApplication>>
    suspend fun updateApplicationStatus(applicationId: String, status: ApplicationStatus): Result<Unit>
    fun observeApplicationsByJob(jobId: String): Flow<List<JobApplication>>
    fun observeApplicationsByWorker(workerId: String): Flow<List<JobApplication>>
}
