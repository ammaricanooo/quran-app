package com.ammaricano.quran.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Firebase helper - handles Auth and Firestore operations
 * Mirrors the web app's SettingsContext and firebase.ts
 */
object FirebaseHelper {

    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    /**
     * Observe auth state changes
     */
    fun observeAuthState(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    /**
     * Observe user data from Firestore
     */
    fun observeUserData(uid: String): Flow<Map<String, Any>?> = callbackFlow {
        val docRef = db.collection("users").document(uid)
        val registration: ListenerRegistration = docRef.addSnapshotListener { snapshot, _ ->
            if (snapshot != null && snapshot.exists()) {
                trySend(snapshot.data)
            } else {
                trySend(null)
            }
        }
        awaitClose { registration.remove() }
    }

    /**
     * Save last read to Firestore
     */
    suspend fun saveLastRead(surahNo: Int, surahName: String, ayatNo: Int) {
        val user = currentUser ?: return
        val lastRead = mapOf(
            "surahNo" to surahNo,
            "surahName" to surahName,
            "ayatNo" to ayatNo,
            "updatedAt" to com.google.firebase.Timestamp.now()
        )
        db.collection("users").document(user.uid)
            .set(mapOf("lastRead" to lastRead), com.google.firebase.firestore.SetOptions.merge())
            .await()
    }

    /**
     * Toggle a bookmark in Firestore
     */
    suspend fun toggleBookmark(bookmark: Map<String, Any?>): Boolean {
        val user = currentUser ?: return false
        val userRef = db.collection("users").document(user.uid)
        val doc = userRef.get().await()

        @Suppress("UNCHECKED_CAST")
        val bookmarks = (doc.get("bookmarks") as? List<Map<String, Any?>>)?.toMutableList()
            ?: mutableListOf()

        val bookmarkId = bookmark["id"] as? String ?: return false
        val existingIndex = bookmarks.indexOfFirst { (it["id"] as? String) == bookmarkId }

        return if (existingIndex >= 0) {
            bookmarks.removeAt(existingIndex)
            userRef.update("bookmarks", bookmarks).await()
            false // removed
        } else {
            bookmarks.add(0, bookmark)
            userRef.set(mapOf("bookmarks" to bookmarks), com.google.firebase.firestore.SetOptions.merge()).await()
            true // added
        }
    }

    /**
     * Sign out
     */
    fun signOut() {
        auth.signOut()
    }

    /**
     * Update display name
     */
    suspend fun updateDisplayName(name: String) {
        val user = currentUser ?: return
        val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()
        user.updateProfile(profileUpdates).await()
        // Also update in Firestore
        db.collection("users").document(user.uid)
            .set(mapOf("displayName" to name), com.google.firebase.firestore.SetOptions.merge())
            .await()
    }

    /**
     * Update user settings in Firestore
     */
    suspend fun updateSettings(settings: Map<String, Any>) {
        val user = currentUser ?: return
        db.collection("users").document(user.uid)
            .set(mapOf("settings" to settings), com.google.firebase.firestore.SetOptions.merge())
            .await()
    }
}
