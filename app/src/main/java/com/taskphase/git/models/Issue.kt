package com.taskphase.git.models

data class Issue(
    val comments_url: String,
    val events_url: String,
    val html_url: String,
    val id: Int,
    val labels_url: String,
    val node_id: String,
    val number: Int,
    val repository_url: String,
    val title: String,
    val url: String
)