package com.example.graph

class layout_item(
    var uri1: String = "",
    var appName1: String = "",
    var date1: String = "",
    var totalClicks1: Int ,
    var link1: String = "") {
    fun getUri(): String = uri1
    fun getAppName(): String = appName1
    fun getDate(): String = date1
    fun getTotalClicks(): Int = totalClicks1
    fun getLink(): String = link1

    fun setUri(uri: String) {
        this.uri1 = uri
    }
    fun setAppName(appName: String) {
        this.appName1= appName
    }
    fun setDate(date: String) {
        this.date1 = date
    }
    fun setTotalClicks(totalClicks: Int) {
        this.totalClicks1 = totalClicks
    }
    fun setLink(link: String) {
        this.link1 = link
    }
}