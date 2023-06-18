package com.example.todoapp.ui.activities

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.data.TaskResult
import com.example.todoapp.data.database.TaskDatabase
import com.example.todoapp.databinding.ActivityCreateTaskBinding
import com.example.todoapp.data.repository.TaskRepository
import com.example.todoapp.viewmodel.TaskViewModel
import com.example.todoapp.viewmodel.TaskViewModelFactory

class CreateTaskActivity : AppCompatActivity() {
    private lateinit var mContext: Context
    private lateinit var binding:ActivityCreateTaskBinding
    private lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initListener()
        observeData()
}

    private fun initData() {
        mContext=this
        setActionBarTitle("Create Task")

        val taskDao = TaskDatabase.getInstance(applicationContext).taskDao()
        val taskRepository = TaskRepository(taskDao)
        viewModel = ViewModelProvider(this, TaskViewModelFactory(taskRepository))[TaskViewModel::class.java]
    }

    private fun initListener() {
        binding.btnCreate.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()
            callCreateTask(title, description)
        }
    }

    private fun callCreateTask(title: String, description: String) {
        // Call the createTask function of the ViewModel
        viewModel.insertTask(title, description)
    }

    private fun observeData() {
        // Observe the createdTask LiveData to get the created task
        viewModel.taskResult.observe(this) { result ->
            when (result) {
                is TaskResult.Success -> {
                    // Handle successful result
                    val message = result.data
                    // Perform necessary actions with the task
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                is TaskResult.Error -> {
                    // Handle error result
                    val errorMessage = result.message
                    // Display error message to the user or perform error handling
                    Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}
fun AppCompatActivity.setActionBarTitle(title: String) {
    supportActionBar?.title = title
}