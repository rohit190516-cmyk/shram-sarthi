package com.example.shramsarthi.data.repository

import com.example.shramsarthi.model.Job
import com.example.shramsarthi.model.JobStatus
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing Job requirements and postings.
 */
interface JobRepository {
    suspend fun createJob(job: Job): Result<String>
    suspend fun getJobById(jobId: String): Result<Job?>
    suspend fun getOpenJobs(): Result<List<Job>>
    suspend fun getJobsByThekedar(thekedarId: String): Result<List<Job>>
    suspend fun updateJobStatus(jobId: String, status: JobStatus): Result<Unit>
    fun observeOpenJobs(): Flow<List<Job>>
    fun observeJobsByThekedar(thekedarId: String): Flow<List<Job>>
}
