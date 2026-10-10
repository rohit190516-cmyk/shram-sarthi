package com.example.shramsarthi.data.firebase

import com.example.shramsarthi.data.repository.JobApplicationRepository
import com.example.shramsarthi.model.ApplicationStatus
import com.example.shramsarthi.model.JobApplication
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreJobApplicationRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : JobApplicationRepository {

    private val appsCollection = firestore.collection("job_applications")

    override suspend fun applyForJob(application: JobApplication): Result<String> = runCatching {
        val docId = if (application.applicationId.isNotBlank()) {
            application.applicationId
        } else {
            appsCollection.document().id
        }
        val finalApp = application.copy(applicationId = docId)
        val data = appToMap(finalApp)
        appsCollection.document(docId).set(data).awaitTask()
        docId
    }

    override suspend fun getApplicationsByJob(jobId: String): Result<List<JobApplication>> = runCatching {
        val snapshot = appsCollection
            .whereEqualTo("jobId", jobId)
            .get()
            .awaitTask()
        snapshot.documents.mapNotNull { doc -> snapshotToApp(doc.id, doc.data ?: emptyMap()) }
    }

    override suspend fun getApplicationsByWorker(workerId: String): Result<List<JobApplication>> = runCatching {
        val snapshot = appsCollection
            .whereEqualTo("workerId", workerId)
            .get()
            .awaitTask()
        snapshot.documents.mapNotNull { doc -> snapshotToApp(doc.id, doc.data ?: emptyMap()) }
    }

    override suspend fun updateApplicationStatus(
        applicationId: String,
        status: ApplicationStatus
    ): Result<Unit> = runCatching {
        appsCollection.document(applicationId).update("status", status.name).awaitTask()
    }

    override fun observeApplicationsByJob(jobId: String): Flow<List<JobApplication>> = callbackFlow {
        val listenerRegistration = appsCollection
            .whereEqualTo("jobId", jobId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val apps = snapshot?.documents?.mapNotNull { doc ->
                    snapshotToApp(doc.id, doc.data ?: emptyMap())
                } ?: emptyList()
                trySend(apps)
            }
        awaitClose { listenerRegistration.remove() }
    }

    override fun observeApplicationsByWorker(workerId: String): Flow<List<JobApplication>> = callbackFlow {
        val listenerRegistration = appsCollection
            .whereEqualTo("workerId", workerId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val apps = snapshot?.documents?.mapNotNull { doc ->
                    snapshotToApp(doc.id, doc.data ?: emptyMap())
                } ?: emptyList()
                trySend(apps)
            }
        awaitClose { listenerRegistration.remove() }
    }

    private fun appToMap(application: JobApplication): Map<String, Any?> = mapOf(
        "applicationId" to application.applicationId,
        "jobId" to application.jobId,
        "workerId" to application.workerId,
        "thekedarId" to application.thekedarId,
        "status" to application.status.name,
        "createdAt" to application.createdAt
    )

    private fun snapshotToApp(id: String, map: Map<String, Any?>): JobApplication {
        val statusStr = map["status"] as? String ?: ApplicationStatus.PENDING.name
        val status = runCatching { ApplicationStatus.valueOf(statusStr) }.getOrDefault(ApplicationStatus.PENDING)

        return JobApplication(
            applicationId = (map["applicationId"] as? String) ?: id,
            jobId = map["jobId"] as? String ?: "",
            workerId = map["workerId"] as? String ?: "",
            thekedarId = map["thekedarId"] as? String ?: "",
            status = status,
            createdAt = (map["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
        )
    }
}
