package com.pedrogomez.hnmob

import android.content.Context
import android.os.Looper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pedrogomez.hnmob.view.comments.viewmodel.CommentsViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
class CommentsViewModelIntegrationTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var SUT : CommentsViewModel

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        FirebaseApp.initializeApp(context)
        //this url is only for use testing on android emulator
        Firebase.database.useEmulator("10.0.2.2", 9000)
        SUT = CommentsViewModel(
            Firebase.database
        )
    }

    @Test
    fun getCommentsWithId(){
        runBlocking {
            SUT.getCommentsInLD("32318301")
            assert(
                SUT.comments.getOrAwaitValue().isNotEmpty()
            )
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

}