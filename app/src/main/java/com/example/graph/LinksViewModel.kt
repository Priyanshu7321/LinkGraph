package com.example.graph
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LinksViewModel : ViewModel() {

    private val _recentLinks = MutableLiveData<ArrayList<layout_item>>()
    val recentLinks: LiveData<ArrayList<layout_item>> get() = _recentLinks

    private val _topLinks = MutableLiveData<ArrayList<layout_item>>()
    val topLinks: LiveData<ArrayList<layout_item>> get() = _topLinks

    fun setRecentLinks(links: ArrayList<layout_item>) {
        _recentLinks.value = links
    }

    fun setTopLinks(links: ArrayList<layout_item>) {
        _topLinks.value = links
    }


}
