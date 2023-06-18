package com.example.todoapp.ui.interfaces

import com.example.todoapp.data.database.Task

interface OnItemClickListener {
    fun onItemClick(task: Task, itemId: String)
}