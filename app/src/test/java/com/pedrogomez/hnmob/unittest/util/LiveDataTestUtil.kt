package com.pedrogomez.hnmob.unittest.util

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.delay
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Gets the value of a [LiveData] or waits for it to have one, with a timeout.
 *
 * Use this extension from host-side (JVM) tests. It's recommended to use it alongside
 * `InstantTaskExecutorRule` or a similar mechanism to execute tasks synchronously.
 */
fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 10,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    afterObserve.invoke()

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

fun <T> LiveData<T>.getOrAwaitValueWithThreadSleep(
        afterObserve: (T) -> Unit = {}
){
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            o?.let {
                afterObserve.invoke(it)
            }
            Thread.sleep(2000)
            this@getOrAwaitValueWithThreadSleep.removeObserver(this)
        }
    }
    this.observeForever(observer)
    Thread.sleep(10000)
}

/**
 * Observes a [LiveData] until the `block` is done executing.
 */
fun <T> LiveData<T>.observeForTesting(block: (T) -> Unit) {
    val observer = Observer<T> {
        block(it)
    }
    try {
        observeForever(observer)
    } finally {
        removeObserver(observer)
    }
}