package com.example.todoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.database.Task
import com.example.todoapp.data.TaskResult
import com.example.todoapp.data.repository.TaskRepository
import com.example.todoapp.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _taskResult = MutableLiveData<TaskResult<String>>()
    val taskResult: LiveData<TaskResult<String>> = _taskResult

    private val _taskList = MutableLiveData<TaskResult<List<Task>>>()
    val taskList: LiveData<TaskResult<List<Task>>> = _taskList

    fun insertTask(title: String, description: String) {
        if (title.isEmpty()) {
            _taskResult.postValue(TaskResult.Error("Please Enter Title"))
            return
        } else if (!Constants.TITLE_PATTERN.matches(title)) {
            _taskResult.postValue(TaskResult.Error("Please Enter Characters Only"))
            return
        }
        val task = Task(title = title, description = description)
        viewModelScope.launch (Dispatchers.IO){
            try {
                repository.insert(task)
                _taskResult.postValue(TaskResult.Success("Task Created Successfully"))
            } catch (e: Exception) {
                _taskResult.postValue(TaskResult.Error(e.message ?: "Unknown error occurred"))
            }
        }
    }

    fun getAllTasks() {
        viewModelScope.launch (Dispatchers.IO){
            try {
                val tasks = repository.getAllTasks()
                _taskList.postValue(TaskResult.Success(tasks))
            } catch (e: Exception) {
                _taskList.postValue(TaskResult.Error(e.message ?: "Unknown error occurred"))
            }
        }
    }

    fun updateTask(task: Task, isCompleted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val existingTask = repository.getTaskById(task.id)
                if (existingTask != null) {
                    val updatedTask = existingTask.copy(isCompleted = isCompleted)
                    repository.update(updatedTask)
                    if(isCompleted){
                        _taskResult.postValue(TaskResult.Success("Task Marked As Completed"))
                    }else{
                        _taskResult.postValue(TaskResult.Success("Task Marked As Pending"))
                    }
                } else {
                    _taskResult.postValue(TaskResult.Error("Task not found"))
                }
            } catch (e: Exception) {
                _taskResult.postValue(TaskResult.Error(e.message ?: "Unknown error occurred"))
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteTaskById(task.id)
                _taskResult.postValue(TaskResult.Success("Task Deleted Successfully"))
            } catch (e: Exception) {
                _taskResult.postValue(TaskResult.Error(e.message ?: "Unknown error occurred"))
            }
        }
    }
}


