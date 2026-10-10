package com.example.shramsarthi.data.firebase

import com.example.shramsarthi.data.repository.JobRepository
import com.example.shramsarthi.model.Job
import com.example.shramsarthi.model.JobStatus
import com.example.shramsarthi.model.RateType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreJobRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : JobRepository {

    private val jobsCollection = firestore.collection("jobs")

    override suspend fun createJob(job: Job): Result<String> = runCatching {
        val docId = if (job.jobId.isNotBlank()) job.jobId else jobsCollection.document().id
        val finalJob = job.copy(jobId = docId)
        val data = jobToMap(finalJob)
        jobsCollection.document(docId).set(data).awaitTask()
        docId
    }

    override suspend fun getJobById(jobId: String): Result<Job?> = runCatching {
        val snapshot = jobsCollection.document(jobId).get().awaitTask()
        if (snapshot.exists()) {
            snapshotToJob(snapshot.id, snapshot.data ?: emptyMap())
        } else {
            null
        }
    }

    override suspend fun getOpenJobs(): Result<List<Job>> = runCatching {
        val snapshot = jobsCollection
            .whereEqualTo("status", JobStatus.OPEN.name)
            .get()
            .awaitTask()
        snapshot.documents.mapNotNull { doc -> snapshotToJob(doc.id, doc.data ?: emptyMap()) }
    }

    override suspend fun getJobsByThekedar(thekedarId: String): Result<List<Job>> = runCatching {
        val snapshot = jobsCollection
            .whereEqualTo("thekedarId", thekedarId)
            .get()
            .awaitTask()
        snapshot.documents.mapNotNull { doc -> snapshotToJob(doc.id, doc.data ?: emptyMap()) }
    }

    override suspend fun updateJobStatus(jobId: String, status: JobStatus): Result<Unit> = runCatching {
        jobsCollection.document(jobId).update("status", status.name).awaitTask()
    }

    override fun observeOpenJobs(): Flow<List<Job>> = callbackFlow {
        val listenerRegistration = jobsCollection
            .whereEqualTo("status", JobStatus.OPEN.name)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val jobs = snapshot?.documents?.mapNotNull { doc ->
                    snapshotToJob(doc.id, doc.data ?: emptyMap())
                } ?: emptyList()
                trySend(jobs)
            }
        awaitClose { listenerRegistration.remove() }
    }

    override fun observeJobsByThekedar(thekedarId: String): Flow<List<Job>> = callbackFlow {
        val listenerRegistration = jobsCollection
            .whereEqualTo("thekedarId", thekedarId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val jobs = snapshot?.documents?.mapNotNull { doc ->
                    snapshotToJob(doc.id, doc.data ?: emptyMap())
                } ?: emptyList()
                trySend(jobs)
            }
        awaitClose { listenerRegistration.remove() }
    }

    private fun jobToMap(job: Job): Map<String, Any?> = mapOf(
        "jobId" to job.jobId,
        "thekedarId" to job.thekedarId,
        "title" to job.title,
        "workerType" to job.workerType,
        "description" to job.description,
        "rate" to job.rate,
        "rateType" to job.rateType.name,
        "workersRequired" to job.workersRequired,
        "date" to job.date,
        "time" to job.time,
        "location" to job.location,
        "status" to job.status.name,
        "createdAt" to job.createdAt
    )

    private fun snapshotToJob(id: String, map: Map<String, Any?>): Job {
        val rateTypeStr = map["rateType"] as? String ?: RateType.DAILY.name
        val rateType = runCatching { RateType.valueOf(rateTypeStr) }.getOrDefault(RateType.DAILY)
        val statusStr = map["status"] as? String ?: JobStatus.OPEN.name
        val status = runCatching { JobStatus.valueOf(statusStr) }.getOrDefault(JobStatus.OPEN)

        return Job(
            jobId = (map["jobId"] as? String) ?: id,
            thekedarId = map["thekedarId"] as? String ?: "",
            title = map["title"] as? String ?: "",
            workerType = map["workerType"] as? String ?: "",
            description = map["description"] as? String ?: "",
            rate = (map["rate"] as? Number)?.toDouble() ?: 0.0,
            rateType = rateType,
            workersRequired = (map["workersRequired"] as? Number)?.toInt() ?: 1,
            date = map["date"] as? String ?: "",
            time = map["time"] as? String ?: "",
            location = map["location"] as? String ?: "",
            status = status,
            createdAt = (map["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
        )
    }
}
