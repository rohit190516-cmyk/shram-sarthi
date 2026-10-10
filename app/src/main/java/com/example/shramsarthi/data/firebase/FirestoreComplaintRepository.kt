package com.example.shramsarthi.data.firebase

import com.example.shramsarthi.data.repository.ComplaintRepository
import com.example.shramsarthi.model.Complaint
import com.example.shramsarthi.model.ComplaintCategory
import com.example.shramsarthi.model.ComplaintStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Concrete Cloud Firestore implementation for persisting genuine complaints and drafts.
 * Scoped strictly to the authenticated worker UID.
 */
class FirestoreComplaintRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ComplaintRepository {

    private val complaintsCollection = firestore.collection("complaints")

    override suspend fun saveComplaint(complaint: Complaint): Result<String> = runCatching {
        val docId = if (complaint.complaintId.isNotBlank()) {
            complaint.complaintId
        } else {
            complaintsCollection.document().id
        }
        val finalComplaint = complaint.copy(
            complaintId = docId,
            updatedAt = System.currentTimeMillis()
        )
        val data = complaintToMap(finalComplaint)
        complaintsCollection.document(docId).set(data).awaitTask()
        docId
    }

    override suspend fun updateComplaint(complaint: Complaint): Result<Unit> = runCatching {
        val updated = complaint.copy(updatedAt = System.currentTimeMillis())
        complaintsCollection.document(complaint.complaintId).set(complaintToMap(updated)).awaitTask()
    }

    override suspend fun getComplaintsByWorker(workerUid: String): Result<List<Complaint>> = runCatching {
        if (workerUid.isBlank()) return@runCatching emptyList()
        val snapshot = complaintsCollection
            .whereEqualTo("ownerUid", workerUid)
            .get()
            .awaitTask()
        snapshot.documents.mapNotNull { doc -> snapshotToComplaint(doc.id, doc.data ?: emptyMap()) }
            .sortedByDescending { it.createdAt }
    }

    override fun observeComplaintsByWorker(workerUid: String): Flow<List<Complaint>> = callbackFlow {
        if (workerUid.isBlank()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listener = complaintsCollection
            .whereEqualTo("ownerUid", workerUid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val complaints = snapshot?.documents?.mapNotNull { doc ->
                    snapshotToComplaint(doc.id, doc.data ?: emptyMap())
                }?.sortedByDescending { it.createdAt } ?: emptyList()
                trySend(complaints)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun deleteComplaint(complaintId: String): Result<Unit> = runCatching {
        complaintsCollection.document(complaintId).delete().awaitTask()
    }

    private fun complaintToMap(complaint: Complaint): Map<String, Any?> = mapOf(
        "complaintId" to complaint.complaintId,
        "ownerUid" to complaint.ownerUid,
        "category" to complaint.category.name,
        "originalDescription" to complaint.originalDescription,
        "language" to complaint.language,
        "employerName" to complaint.employerName,
        "workLocation" to complaint.workLocation,
        "workDates" to complaint.workDates,
        "wageAgreed" to complaint.wageAgreed,
        "amountPaid" to complaint.amountPaid,
        "amountClaimed" to complaint.amountClaimed,
        "requestedResolution" to complaint.requestedResolution,
        "draftContent" to complaint.draftContent,
        "evidenceReferences" to complaint.evidenceReferences,
        "applicableLegalAct" to complaint.applicableLegalAct,
        "officialAuthority" to complaint.officialAuthority,
        "officialAuthorityUrl" to complaint.officialAuthorityUrl,
        "status" to complaint.status.name,
        "submissionDestination" to complaint.submissionDestination,
        "createdAt" to complaint.createdAt,
        "updatedAt" to complaint.updatedAt
    )

    @Suppress("UNCHECKED_CAST")
    private fun snapshotToComplaint(id: String, map: Map<String, Any?>): Complaint {
        val catStr = map["category"] as? String ?: ComplaintCategory.UNPAID_WAGES.name
        val category = runCatching { ComplaintCategory.valueOf(catStr) }.getOrDefault(ComplaintCategory.UNPAID_WAGES)

        val statusStr = map["status"] as? String ?: ComplaintStatus.DRAFT.name
        val status = runCatching { ComplaintStatus.valueOf(statusStr) }.getOrDefault(ComplaintStatus.DRAFT)

        val evidenceList = (map["evidenceReferences"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()

        return Complaint(
            complaintId = (map["complaintId"] as? String) ?: id,
            ownerUid = map["ownerUid"] as? String ?: "",
            category = category,
            originalDescription = map["originalDescription"] as? String ?: "",
            language = map["language"] as? String ?: "hi",
            employerName = map["employerName"] as? String ?: "",
            workLocation = map["workLocation"] as? String ?: "",
            workDates = map["workDates"] as? String ?: "",
            wageAgreed = map["wageAgreed"] as? String ?: "",
            amountPaid = map["amountPaid"] as? String ?: "",
            amountClaimed = map["amountClaimed"] as? String ?: "",
            requestedResolution = map["requestedResolution"] as? String ?: "",
            draftContent = map["draftContent"] as? String ?: "",
            evidenceReferences = evidenceList,
            applicableLegalAct = map["applicableLegalAct"] as? String ?: "Code on Wages, 2019",
            officialAuthority = map["officialAuthority"] as? String ?: "Office of District Labour Commissioner",
            officialAuthorityUrl = map["officialAuthorityUrl"] as? String ?: "https://samadhan.labour.gov.in",
            status = status,
            submissionDestination = map["submissionDestination"] as? String,
            createdAt = (map["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
            updatedAt = (map["updatedAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
        )
    }
}
