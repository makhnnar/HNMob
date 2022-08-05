package com.pedrogomez.hnmob.integrations.view.comments.viewmodel

import android.content.Context
import android.os.Build
import android.os.Looper.getMainLooper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import org.robolectric.shadows.ShadowLooper
import java.io.IOException
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
@LooperMode(LooperMode.Mode.PAUSED)
class CommentsViewModelIntegrationTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var SUT : CommentsViewModel

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        FirebaseApp.initializeApp(
            context
        )
        Firebase.database.useEmulator("localhost", 9000)
        SUT = CommentsViewModel(
            Firebase.database
        )
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
    }

    @Test
    fun getCommentsWithId(){
        runBlocking {
            val result = SUT.getCommentsInFlow("32318301")
            shadowOf(getMainLooper()).idle()
            result.collect {
                assert(it.isEmpty())
            }
        }
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        //if we don't stop koin you only could get one test work at time
        stopKoin()
        shadowOf(getMainLooper()).runToEndOfTasks()
    }

}