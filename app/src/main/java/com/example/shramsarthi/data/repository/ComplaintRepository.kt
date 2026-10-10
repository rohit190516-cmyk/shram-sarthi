package com.example.shramsarthi.data.repository

import com.example.shramsarthi.model.Complaint
import com.example.shramsarthi.model.ComplaintStatus
import kotlinx.coroutines.flow.Flow

interface ComplaintRepository {
    /**
     * Persists a complaint or draft to Firestore for the authenticated user.
     */
    suspend fun saveComplaint(complaint: Complaint): Result<String>

    /**
     * Updates an existing complaint draft or status.
     */
    suspend fun updateComplaint(complaint: Complaint): Result<Unit>

    /**
     * Retrieves all genuine complaints owned by the authenticated worker.
     */
    suspend fun getComplaintsByWorker(workerUid: String): Result<List<Complaint>>

    /**
     * Observes real-time updates for complaints owned by the worker.
     */
    fun observeComplaintsByWorker(workerUid: String): Flow<List<Complaint>>

    /**
     * Deletes a draft complaint.
     */
    suspend fun deleteComplaint(complaintId: String): Result<Unit>
}
