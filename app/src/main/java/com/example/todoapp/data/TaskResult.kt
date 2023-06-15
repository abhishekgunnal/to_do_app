package com.example.todoapp.data

sealed class TaskResult<out T> {
    data class Success<out T>(val data: T) : TaskResult<T>()
    data class Error(val message: String) : TaskResult<Nothing>()
}
