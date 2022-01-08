package com.taskphase.git.models

data class Commit(
    val comment_count: Int,
    val committer: Committer,
    val message: String,
)