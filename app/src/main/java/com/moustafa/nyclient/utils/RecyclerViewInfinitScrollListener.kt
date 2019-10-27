package com.moustafa.nyclient.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author moustafasamhoury
 * created on Sunday, 27 Oct, 2019
 */


class RecyclerViewInfinitScrollListener(
    private val scope: CoroutineScope,
    private val threshold: Int = 10,
    private val rateLimitIgnoreWithin: Long? = null,
    private val onEndReached: (Int) -> Any
) :
    RecyclerView.OnScrollListener() {
    private var rateLimitFlag: AtomicBoolean = AtomicBoolean(true)

    private var currentPage = 1
    private var loadInProgress = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        when (val layoutManager = recyclerView.layoutManager) {
            is LinearLayoutManager -> {
                if (layoutManager.findLastVisibleItemPosition() >= layoutManager.itemCount - 1 - threshold) {
                    invokeEndReached()
                }
            }
            is GridLayoutManager -> {
                if (layoutManager.findLastVisibleItemPosition() >= layoutManager.itemCount - 1 - threshold) {
                    invokeEndReached()
                }
            }
            is StaggeredGridLayoutManager -> {

            }
        }



        super.onScrolled(recyclerView, dx, dy)
    }

    private fun invokeEndReached() {
        if (!loadInProgress && rateLimitFlag.get()) {
            onEndReached(currentPage)
            if (rateLimitIgnoreWithin != null) {
                resetRateLimiter()
            }
        }
    }

    private fun resetRateLimiter() {
        scope.launch { rateLimiter() }
    }

    fun onSuccessIncrementCurrentPage() {
        currentPage++
    }

    fun setLoadInProgress(loadInProgress: Boolean) {
        this.loadInProgress = loadInProgress
    }


    private suspend fun rateLimiter() {
        rateLimitFlag.getAndSet(false)
        delay(rateLimitIgnoreWithin ?: DEFAULT_RATE_LIMIT_VAL)
        rateLimitFlag.getAndSet(true)
    }

    companion object {
        private const val DEFAULT_RATE_LIMIT_VAL: Long = 2_000L
    }
}
