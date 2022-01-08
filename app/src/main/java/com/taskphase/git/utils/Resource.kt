package com.taskphase.git.utils

sealed class Resource<T>(
    val data: T? = null,
    val message: String = "",
){
    class Success<T>(data: T, message: String = ""): Resource<T>(data = data, message = message)
    class Error<T>(error: String): Resource<T>(message = error)
    class Loading<T>: Resource<T>()
}
