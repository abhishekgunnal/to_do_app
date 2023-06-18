package com.example.todoapp.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.todoapp.data.database.Task
import com.example.todoapp.data.TaskResult
import com.example.todoapp.data.database.TaskDao

class TaskRepository(private val taskDao: TaskDao) {
    suspend fun insert(task: Task) {
        try {
            taskDao.insert(task)
        } catch (e: Exception) {
            throw Exception(e.message ?: "Unknown error occurred")
        }
    }

    suspend fun update(task: Task) {
        try {
            taskDao.update(task)
        } catch (e: Exception) {
            throw Exception(e.message ?: "Unknown error occurred")
        }
    }

    suspend fun delete(task: Task) {
        try {
            taskDao.delete(task)
        } catch (e: Exception) {
            throw Exception(e.message ?: "Unknown error occurred")
        }
    }

    fun getAllTasks(): List<Task> {
        return taskDao.getAllTasks()
    }

    suspend fun getTaskById(taskId: Long): Task? {
        return taskDao.getTaskById(taskId)
    }

    suspend fun deleteTaskById(taskId: Long) {
        taskDao.deleteTaskById(taskId)
    }
}



