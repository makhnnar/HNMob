package com.pedrogomez.hnmob.view.viewmodel

import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.*
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*
import org.mockito.ArgumentCaptor.*
import org.mockito.Mockito
import org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner::class)
class SharedHitsViewModelTest {

    private lateinit var SUT: SharedHitsViewModel

    var reposotoryMock : SharedHitsViewModel.Repository = Mockito.mock(SharedHitsViewModel.Repository::class.java)

    @Before
    fun setUp() {
        SUT = SharedHitsViewModel(
            reposotoryMock
        )
    }

}