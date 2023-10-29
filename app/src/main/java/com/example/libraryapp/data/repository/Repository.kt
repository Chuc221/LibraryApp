package com.example.libraryapp.data.repository

import android.app.Application
import android.preference.PreferenceManager
import androidx.core.net.toUri
import com.example.libraryapp.data.model.Book
import com.example.libraryapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class Repository(private val application: Application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    private val storageRef = Firebase.storage.reference

    fun getCurrentUser(returnUserCurrent: (User?) -> Unit) {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            database.child("users").child(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val currentUser = snapshot.getValue(User::class.java)
                        if (currentUser != null) {
                            returnUserCurrent(currentUser)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        returnUserCurrent(null)
                    }
                })
        }
    }

    fun loginUser(email: String, password: String, returnStatusLoginUser: (Boolean) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            returnStatusLoginUser(it.isSuccessful)
        }
    }

    suspend fun registerUser(
        name: String, email: String, password: String, returnStatusRegisterUser: (Boolean) -> Unit
    ) {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        result.user?.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(name).build()
        )?.addOnCompleteListener {
            if (it.isSuccessful) {
                val userId = result.user!!.uid
                val userCurrent = User(
                    id = result.user!!.uid,
                    name = result.user!!.displayName,
                    email = result.user!!.email
                )
                database.child("users").child(userId).setValue(userCurrent).addOnCompleteListener {addUser ->
                    returnStatusRegisterUser(addUser.isSuccessful)
                }
            } else {
                returnStatusRegisterUser(false)
            }
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun updateProfileCurrentUser(user: User, returnStatusUpdate: (Boolean) -> Unit) {
        val uriImage = user.imageUri?.toUri()
        val imageRef = storageRef.child("images/${uriImage?.lastPathSegment}")
        if (uriImage != null) {
            imageRef.putFile(uriImage).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.imageUri = task.result.toString()
                    database.child("users").child(user.id).setValue(user).addOnCompleteListener {
                        returnStatusUpdate(it.isSuccessful)
                    }
                } else {
                    database.child("users").child(user.id).setValue(user).addOnCompleteListener {
                        returnStatusUpdate(it.isSuccessful)
                    }
                }
            }.addOnFailureListener {
                database.child("users").child(user.id).setValue(user).addOnCompleteListener {
                    returnStatusUpdate(it.isSuccessful)
                }
            }
        } else {
            database.child("users").child(user.id).setValue(user).addOnCompleteListener {
                returnStatusUpdate(it.isSuccessful)
            }
        }
    }

    fun updateBook(book: Book, returnStatusUpdate: (Boolean) -> Unit) {
        val uriImage = book.bookImageUri?.toUri()
        val imageRef = storageRef.child("imagesBook/${uriImage?.lastPathSegment}")
        if (uriImage != null) {
            imageRef.putFile(uriImage).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    book.bookImageUri = task.result.toString()
                    database.child("books").child(book.bookID).setValue(book).addOnCompleteListener {
                        returnStatusUpdate(it.isSuccessful)
                    }
                } else {
                    database.child("books").child(book.bookID).setValue(book).addOnCompleteListener {
                        returnStatusUpdate(it.isSuccessful)
                    }
                }
            }.addOnFailureListener {
                database.child("books").child(book.bookID).setValue(book).addOnCompleteListener {
                    returnStatusUpdate(it.isSuccessful)
                }
            }
        } else {
            database.child("books").child(book.bookID).setValue(book).addOnCompleteListener {
                returnStatusUpdate(it.isSuccessful)
            }
        }
    }

    fun getBook(returnBook: (MutableList<Book>) -> Unit) {
        database.child("books").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listBooks = mutableListOf<Book>()
                for (dataSnapshot in snapshot.children) {
                    val book = dataSnapshot.getValue(Book::class.java)
                    if (book != null) {
                        listBooks.add(book)
                        returnBook(listBooks)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}