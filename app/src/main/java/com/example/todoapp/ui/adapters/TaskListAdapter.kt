package com.example.todoapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.database.Task
import com.example.todoapp.databinding.ItemTaskListBinding
import com.example.todoapp.ui.interfaces.OnItemClickListener
import com.example.todoapp.utils.Constants

class TaskListAdapter(private var tasks: List<Task>, private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskListBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = tasks.size

    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(private val binding: ItemTaskListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.tvTitle.text = task.title
            //binding.taskDescription.text = task.description

            // Handle task completion status
            if (task.isCompleted) {
                binding.tvStatus.text = Constants.STATUS_COMPLETED
            } else {
                binding.tvStatus.text = Constants.STATUS_PENDING
            }

            binding.imgPending.setOnClickListener {
                onItemClickListener.onItemClick(task,Constants.STATUS_PENDING)
            }

            binding.imgCompleted.setOnClickListener {
                onItemClickListener.onItemClick(task,Constants.STATUS_COMPLETED)
            }

            binding.imgDelete.setOnClickListener {
                onItemClickListener.onItemClick(task,Constants.STATUS_DELETE)
            }
        }
    }
}
