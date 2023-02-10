package com.pedrogomez.hnmob.view.comments.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pedrogomez.hnmob.view.comments.CommentsActivity
import com.pedrogomez.hnmob.view.comments.model.FriendlyMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private var db: FirebaseDatabase,
) : ViewModel() {

    val coments = MutableLiveData<List<FriendlyMessage>>()

    private lateinit var messagesRef : DatabaseReference

    fun getComments(idPost:String): FirebaseRecyclerOptions<FriendlyMessage>{
        db = Firebase.database

        messagesRef = db.getReference(CommentsActivity.COMMENTS).child(idPost)

        val options = FirebaseRecyclerOptions.Builder<FriendlyMessage>()
            .setQuery(messagesRef, FriendlyMessage::class.java)
            .build()
        return options
    }

    fun getCommentsInLD(idPost:String){
        var list : MutableList<FriendlyMessage> = mutableListOf()
        db = Firebase.database
        messagesRef = db.getReference(CommentsActivity.COMMENTS).child(idPost)
        try{
            messagesRef.get().addOnSuccessListener {
                it.children.forEach {
                    val value = it.getValue(FriendlyMessage::class.java)
                    value?.let {
                        list.add(
                            it
                        )
                    }
                }
                coments.postValue(list)
            }.addOnFailureListener{
                Log.e("error","exception: ${it.message}",it)
                coments.postValue(list)
            }.addOnCanceledListener {
                Log.e("error","cancelled",)
            }
        }catch (e:Exception){
            Log.e("error","exception: ${e.message}",e)
        }
    }

    suspend fun getCommentsInFlow(idPost:String): Flow<List<FriendlyMessage>> = callbackFlow {
        var list : MutableList<FriendlyMessage> = mutableListOf()
        db = Firebase.database
        messagesRef = db.getReference(CommentsActivity.COMMENTS).child(idPost)
        messagesRef.get().addOnSuccessListener {
            it.children.forEach {
                val value = it.getValue(FriendlyMessage::class.java)
                value?.let {
                    list.add(
                        it
                    )
                }
            }
            runBlocking {
                channel.send(list)
            }
        }.addOnFailureListener{
            runBlocking {
                channel.send(list)
            }
        }
        awaitClose { close() }
    }

    fun publishComment(
        msg:String,
        name:String
    ){
        val friendlyMessage = FriendlyMessage(
            msg,
            name,
        )
        messagesRef.push().setValue(friendlyMessage)
    }

}