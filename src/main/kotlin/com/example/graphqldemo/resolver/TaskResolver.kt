package com.example.graphqldemo.resolver

import com.example.graphqldemo.jooq.tables.pojos.Project
import com.example.graphqldemo.jooq.tables.pojos.Task
import com.example.graphqldemo.jooq.tables.pojos.User
import com.example.graphqldemo.service.ProjectService
import com.example.graphqldemo.service.TaskService
import com.example.graphqldemo.service.UserService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

@Controller
class TaskResolver(
    private val taskService: TaskService,
    private val projectService: ProjectService,
    private val userService: UserService,
) {
    val sink = Sinks.many().multicast().onBackpressureBuffer<Task>()

    @QueryMapping
    fun tasks(): List<Task> = taskService.getAllTasks()

    @QueryMapping
    fun task(
        @Argument id: Int,
    ): Task? = taskService.getTaskById(id)

    @QueryMapping
    fun tasksByProject(
        @Argument projectId: Int,
    ): List<Task> = taskService.getTaskByProjectId(projectId)

    @QueryMapping
    fun tasksByAssignee(
        @Argument assigneeId: Int,
    ): List<Task> = taskService.getTaskByAssigneeId(assigneeId)

    @MutationMapping
    fun createTask(
        @Argument input: CreateTaskInput,
    ): Task =
        try {
            val task =
                taskService.createTask(
                    title = input.title,
                    description = input.description,
                    projectId = input.projectId,
                    assigneeId = input.assigneeId,
                    status = input.status?.name ?: "TODO",
                )
            sink.tryEmitNext(task)
            task
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }

    @MutationMapping
    fun updateTask(
        @Argument id: Int,
        @Argument input: UpdateTaskInput,
    ): Task? =
        try {
            val updatedTask =
                taskService.updateTask(
                    id = id,
                    title = input.title,
                    description = input.description,
                    status = input.status?.name,
                    assigneeId = input.assigneeId,
                )
            if (updatedTask != null) {
                sink.tryEmitNext(updatedTask)
            }
            updatedTask
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }

    @MutationMapping
    fun updateTaskStatus(
        @Argument id: Int,
        @Argument status: TaskStatus,
    ): Task? =
        try {
            val updatedTask = taskService.updateTaskStatus(id, status.name)
            if (updatedTask != null) {
                sink.tryEmitNext(updatedTask)
            }
            updatedTask
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }

    @MutationMapping
    fun deleteTask(
        @Argument id: Int,
    ): Boolean = taskService.deleteTask(id)

    @SchemaMapping(typeName = "Task", field = "project")
    fun project(task: Task): Project? = task.projectId?.let { projectService.getProjectById(it) }

    @SchemaMapping(typeName = "Task", field = "assignee")
    fun assignee(task: Task): User? = task.assigneeId?.let { userService.getUserById(it) }

    @SubscriptionMapping
    fun taskUpdates(): Flux<Task> = Flux.merge(sink.asFlux(), Flux.never())
}

enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE,
}

data class CreateTaskInput(
    val title: String,
    val description: String? = null,
    val projectId: Int,
    val assigneeId: Int? = null,
    val status: TaskStatus? = null,
)

data class UpdateTaskInput(
    val title: String? = null,
    val description: String? = null,
    val assigneeId: Int? = null,
    val status: TaskStatus? = null,
)
