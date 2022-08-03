package com.pedrogomez.hnmob.view.comments.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pedrogomez.hnmob.view.comments.CommentsActivity

class AuthViewModel(
    private var auth: FirebaseAuth
) : ViewModel() {

    val isAutenticated = MutableLiveData<Boolean>()

    fun checkLogin(){
        auth = Firebase.auth
        if (auth.currentUser == null) {
            isAutenticated.postValue(false)
            return
        }
        isAutenticated.postValue(true)
    }

    fun getUserName(): String {
        val user = auth.currentUser
        return if (user != null) {
            user.displayName?: CommentsActivity.ANONYMOUS
        } else CommentsActivity.ANONYMOUS
    }

}