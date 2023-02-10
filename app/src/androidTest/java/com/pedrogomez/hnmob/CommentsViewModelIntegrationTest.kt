package com.pedrogomez.hnmob

import android.content.Context
import android.service.autofill.Validators.not
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SmallTest
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.pedrogomez.hnmob.datahelpers.MessageManagerDataHelper
import com.pedrogomez.hnmob.view.comments.CommentsActivity
import com.pedrogomez.hnmob.view.comments.model.FriendlyMessage
import com.pedrogomez.hnmob.view.comments.viewmodel.CommentsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@LargeTest
class CommentsViewModelIntegrationTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var SUT : CommentsViewModel

    @Before
    fun setup(){
        Dispatchers.setMain(dispatcher)
        val context = ApplicationProvider.getApplicationContext<Context>()
        FirebaseApp.initializeApp(context)
        //this url is only for use testing on android emulator
        Firebase.database.useEmulator("10.0.2.2", 9000)
        SUT = CommentsViewModel(
            Firebase.database
        )
        initMessage()
    }

    @Test
    fun getCommentsWithId(){
        runBlocking {
            SUT.getCommentsInLD("32318301")
            SUT.coments.getOrAwaitValue {
                assert(
                    SUT.coments.getOrAwaitValue().isNotEmpty()
                )
            }
        }
    }

    //buscar como testar el flow
    @Test
    fun getCommentsWithIdWithFlow(){
        runBlocking {
            val result = SUT.getCommentsInFlow("32318301")
            result.collect {
                assert(it.isNotEmpty())
            }
        }
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        //if we don't stop koin you only could get one test work at time
        stopKoin()
    }

    fun initMessage(){
        /*apparantly populate the db during the tests making lost the db connection
        val ref = Firebase.database.getReference(CommentsActivity.COMMENTS).child("32318301")
        ref.push().setValue(
            FriendlyMessage("Hellowis","Anonymouse")
        )*/
    }

}