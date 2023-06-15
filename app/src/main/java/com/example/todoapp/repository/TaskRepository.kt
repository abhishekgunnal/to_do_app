package com.example.todoapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.data.Task
import com.example.todoapp.data.TaskResult
import com.example.todoapp.database.TaskDao

class TaskRepository(private val taskDao: TaskDao) {
    private val _taskResult: MutableLiveData<TaskResult<Any?>> = MutableLiveData()

    suspend fun insert(task: Task) {
        try {
            taskDao.insert(task)
            _taskResult.postValue(TaskResult.Success(task))
        } catch (e: Exception) {
            _taskResult.postValue(TaskResult.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun update(task: Task) {
        try {
            taskDao.update(task)
            _taskResult.postValue(TaskResult.Success(task))
        } catch (e: Exception) {
            _taskResult.postValue(TaskResult.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun delete(task: Task) {
        try {
            taskDao.delete(task)
            _taskResult.postValue(TaskResult.Success(task))
        } catch (e: Exception) {
            _taskResult.postValue(TaskResult.Error(e.message ?: "Unknown error occurred"))
        }
    }

    fun getAllTasks(): List<Task>{
        return taskDao.getAllTasks()
    }

    suspend fun getTaskById(taskId: Long): Task? {
        return taskDao.getTaskById(taskId)
    }

    suspend fun deleteTaskById(taskId: Long) {
        taskDao.deleteTaskById(taskId)
    }

}


