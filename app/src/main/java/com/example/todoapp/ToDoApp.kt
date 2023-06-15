package com.example.todoapp

import android.app.Application
import androidx.room.Room
import com.example.todoapp.database.TaskDatabase

class ToDoApp : Application() {
    companion object {
        lateinit var database: TaskDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "task-db")
            .build()
    }
}
