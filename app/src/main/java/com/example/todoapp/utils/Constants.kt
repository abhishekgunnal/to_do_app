package com.example.todoapp.utils

object Constants {
    const val STATUS_COMPLETED = "Completed"
    const val STATUS_PENDING = "Pending"
    const val STATUS_DELETE = "Delete"
    val TITLE_PATTERN: Regex = Regex("^\\s*[a-zA-Z]+[a-zA-Z\\s]*\\s*$")
}