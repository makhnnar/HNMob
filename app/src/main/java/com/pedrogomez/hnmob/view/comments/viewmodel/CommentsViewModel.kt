package com.pedrogomez.hnmob.view.comments.viewmodel

import androidx.lifecycle.ViewModel
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pedrogomez.hnmob.view.comments.CommentsActivity
import com.pedrogomez.hnmob.view.comments.model.FriendlyMessage

class CommentsViewModel(
    private var db: FirebaseDatabase,
) : ViewModel() {

    private lateinit var messagesRef : DatabaseReference

    fun getComments(idPost:String): FirebaseRecyclerOptions<FriendlyMessage>{
        db = Firebase.database

        messagesRef = db.getReference(CommentsActivity.COMMENTS).child(idPost)

        val options = FirebaseRecyclerOptions.Builder<FriendlyMessage>()
            .setQuery(messagesRef, FriendlyMessage::class.java)
            .build()
        return options
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