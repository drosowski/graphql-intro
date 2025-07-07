package com.example.graphqldemo.service

import com.example.graphqldemo.jooq.tables.pojos.Task
import com.example.graphqldemo.repository.TaskRepository
import com.example.graphqldemo.repository.ProjectRepository
import com.example.graphqldemo.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException

@Service
@Transactional
class TaskService(
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository
) {
    
    fun getAllTasks(): List<Task> = taskRepository.findAll()
    
    fun getTaskById(id: Int): Task? = taskRepository.findById(id)
    
    fun getTaskByProjectId(projectId: Int): List<Task> = taskRepository.findByProjectId(projectId)
    
    fun getTaskByAssigneeId(assigneeId: Int): List<Task> = taskRepository.findByAssigneeId(assigneeId)
    
    fun createTask(
        title: String,
        description: String? = null,
        projectId: Int,
        assigneeId: Int? = null,
        status: String = "TODO"
    ): Task {
        // Validate project exists
        if (projectRepository.findById(projectId) == null) {
            throw IllegalArgumentException("Project not found with id: $projectId")
        }
        // Validate assignee exists if provided
        if (assigneeId != null && userRepository.findById(assigneeId) == null) {
            throw IllegalArgumentException("User not found with id: $assigneeId")
        }
        // Validate status
        validateStatus(status)
        return taskRepository.create(title, description, projectId, assigneeId, status)
    }
    
    fun updateTask(
        id: Int,
        title: String? = null,
        description: String? = null,
        status: String? = null,
        assigneeId: Int? = null
    ): Task? {
        // Validate assignee exists if provided
        if (assigneeId != null && userRepository.findById(assigneeId) == null) {
            throw IllegalArgumentException("User not found with id: $assigneeId")
        }
        // Validate status if provided
        status?.let { validateStatus(it) }
        return taskRepository.update(id, title, description, status, assigneeId)
    }
    
    fun updateTaskStatus(id: Int, status: String): Task? {
        validateStatus(status)
        return taskRepository.updateStatus(id, status)
    }
    
    fun deleteTask(id: Int): Boolean = taskRepository.delete(id)
    
    private fun validateStatus(status: String) {
        if (status !in setOf("TODO", "IN_PROGRESS", "DONE")) {
            throw IllegalArgumentException("Invalid status: $status")
        }
    }
} 