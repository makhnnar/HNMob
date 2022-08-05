package com.pedrogomez.hnmob.integrations.preferences

import android.content.Context
import android.os.Build
import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pedrogomez.hnmob.repository.preferences.PreferenceStoreManager
import com.pedrogomez.hnmob.repository.preferences.PreferenceStoreManager.Companion.IS_REQUIRED
import com.pedrogomez.hnmob.view.comments.viewmodel.CommentsViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class PreferenceDataStoreTestIntegration {

    lateinit var SUT : PreferenceStoreManager

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        SUT = PreferenceStoreManager(
            context
        )
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
    }

    @Test
    fun setAndGetIsRequiredValue(){
        runBlocking {
            SUT.setValue(IS_REQUIRED,true)
            val result = SUT.getValue(IS_REQUIRED,false)
            assert(result)
        }
    }

    @After
    fun closeDb() {
        //if we don't stop koin you only could get one test work at time
        stopKoin()
        Shadows.shadowOf(Looper.getMainLooper()).runToEndOfTasks()
    }

}