package com.example.todoapp.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.data.Task
import com.example.todoapp.data.TaskResult
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.databinding.ActivityTaskListBinding
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.adapters.TaskListAdapter
import com.example.todoapp.ui.interfaces.OnItemClickListener
import com.example.todoapp.utils.Constants
import com.example.todoapp.viewmodel.TaskViewModel
import com.example.todoapp.viewmodel.TaskViewModelFactory

class TaskListActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var mContext: Context
    private lateinit var binding: ActivityTaskListBinding
    private lateinit var viewModel: TaskViewModel
    private lateinit var taskListAdapter: TaskListAdapter
    private var allTasks: List<Task> = listOf()
    private lateinit var taskActivityResultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initListener()
        observeData()
        setAdapter()
        callAllTask()
    }


    private fun initData() {
        mContext=this
        setActionBarTitle("To-Do List")
        val taskDao = TaskDatabase.getInstance(applicationContext).taskDao()
        val taskRepository = TaskRepository(taskDao)
        viewModel = ViewModelProvider(this, TaskViewModelFactory(taskRepository))[TaskViewModel::class.java]
        binding.rvTaskList.apply { layoutManager= LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) }

    }

    private fun initListener() {
        taskActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(result.resultCode, result.data)
        }

        binding.fabAddTask.setOnClickListener {
            val intent = Intent(mContext, CreateTaskActivity::class.java)
            taskActivityResultLauncher.launch(intent)
        }
    }

    private fun callAllTask() {
        // Call the getAllTasks function of the ViewModel
        viewModel.getAllTasks()
    }

    private fun observeData() {
        viewModel.taskList.observe(this) {
            try {
                when (it) {
                    is TaskResult.Success -> {
                        allTasks = it.data // List<Task>
                        // Handle the list of tasks here
                        if(allTasks.isEmpty()){
                            binding.rvTaskList.visibility=View.GONE
                            binding.tvNoTask.visibility=View.VISIBLE
                        }else{
                            taskListAdapter.setTasks(allTasks)
                            binding.rvTaskList.visibility=View.VISIBLE
                            binding.tvNoTask.visibility=View.GONE
                        }

                    }
                    is TaskResult.Error -> {
                        val errorMessage = it.message
                        Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.taskResult.observe(this) {
            try {
                when (it) {
                    is TaskResult.Success -> {

                        callAllTask()
                        Toast.makeText(mContext, "Status Changed Successfully", Toast.LENGTH_SHORT).show()
                    }
                    is TaskResult.Error -> {
                        val errorMessage = it.message
                        Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAdapter() {
        taskListAdapter = TaskListAdapter(allTasks, this)
        binding.rvTaskList.adapter=taskListAdapter
    }

    override fun onItemClick(task: Task, itemId: String) {
        when(itemId){
            Constants.STATUS_COMPLETED ->{
                viewModel.updateTask(task, true)
            }

            Constants.STATUS_PENDING ->{
                viewModel.updateTask(task, false)
            }

            Constants.STATUS_DELETE ->{
                viewModel.deleteTask(task)
            }
        }
    }

    private fun handleActivityResult(resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                callAllTask()
            }
        }
    }
}