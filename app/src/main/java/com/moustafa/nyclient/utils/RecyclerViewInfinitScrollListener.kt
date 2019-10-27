package com.moustafa.nyclient.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author moustafasamhoury
 * created on Sunday, 27 Oct, 2019
 */


class RecyclerViewInfinitScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val threshold: Int = 10,
    private val onEndReached: (Int) -> Any
) :
    RecyclerView.OnScrollListener() {
    private var currentPage = 1
    var loadInProgress = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        if (layoutManager.findLastVisibleItemPosition() >= layoutManager.itemCount - 1 - threshold) {
            if (!loadInProgress) {
                currentPage++
                onEndReached(currentPage)
            }
        }

        super.onScrolled(recyclerView, dx, dy)
    }

}