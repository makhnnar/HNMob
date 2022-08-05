package com.pedrogomez.hnmob

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.pedrogomez.hnmob.repository.preferences.PreferenceStoreManager
import com.pedrogomez.hnmob.repository.preferences.PreferenceStoreManager.Companion.IS_REQUIRED
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class PreferenceDataStoreTestIntegration {

    lateinit var SUT : PreferenceStoreManager

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        SUT = PreferenceStoreManager(
            context
        )
    }

    @Test
    fun setAndGetIsRequiredValue(){
        runBlocking {
            SUT.setValue(IS_REQUIRED,false)
            val result = SUT.getValue(IS_REQUIRED,false)
            assert(result)
        }
    }

}