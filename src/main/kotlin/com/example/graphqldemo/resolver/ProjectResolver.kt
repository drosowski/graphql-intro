package com.example.graphqldemo.resolver

import com.example.graphqldemo.jooq.tables.pojos.Project
import com.example.graphqldemo.jooq.tables.pojos.Task
import com.example.graphqldemo.service.ProjectService
import com.example.graphqldemo.service.TaskService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.server.ResponseStatusException

@Controller
class ProjectResolver(
    private val projectService: ProjectService,
    private val taskService: TaskService,
) {
    @QueryMapping
    fun projects(): List<Project> = projectService.getAllProjects()

    @QueryMapping
    fun project(
        @Argument id: Int,
    ): Project? = projectService.getProjectById(id)

    @MutationMapping
    fun createProject(
        @Argument input: CreateProjectInput,
    ): Project {
        try {
            return projectService.createProject(input.name, input.description)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @MutationMapping
    fun updateProject(
        @Argument id: Int,
        @Argument input: UpdateProjectInput,
    ): Project? {
        try {
            return projectService.updateProject(id, input.name, input.description)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @MutationMapping
    fun deleteProject(
        @Argument id: Int,
    ): Boolean = projectService.deleteProject(id)

    @SchemaMapping(typeName = "Project", field = "tasks")
    fun tasks(project: Project): List<Task> = project.id?.let { taskService.getTaskByProjectId(it) } ?: emptyList()
}

data class CreateProjectInput(
    val name: String,
    val description: String?,
)

data class UpdateProjectInput(
    val name: String? = null,
    val description: String? = null,
) 
