package com.example.graphqldemo.resolver

import com.example.graphqldemo.jooq.tables.pojos.Task
import com.example.graphqldemo.jooq.tables.pojos.User
import com.example.graphqldemo.service.TaskService
import com.example.graphqldemo.service.UserService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.BatchMapping
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.server.ResponseStatusException

@Controller
class UserResolver(
    private val userService: UserService,
    private val taskService: TaskService,
) {
    @QueryMapping
    fun users(): List<User> = userService.getAllUsers()

    @QueryMapping
    fun user(
        @Argument id: Int,
    ): User? = userService.getUserById(id)

    @MutationMapping
    fun createUser(
        @Argument input: CreateUserInput,
    ): User {
        try {
            return userService.createUser(input.name, input.email)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @MutationMapping
    fun updateUser(
        @Argument id: Int,
        @Argument input: UpdateUserInput,
    ): User? {
        try {
            return userService.updateUser(id, input.name, input.email)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @MutationMapping
    fun deleteUser(
        @Argument id: Int,
    ): Boolean = userService.deleteUser(id)

    // @SchemaMapping(typeName = "User", field = "tasks")
    // fun tasks(user: User): List<Task> = user.id?.let { taskService.getTaskByAssigneeId(it) } ?: emptyList()

    @BatchMapping(typeName = "User", field = "tasks")
    fun tasks(users: List<User>): Map<User, List<Task>> =
        users.associateWith { user ->
            user.id?.let { taskService.getTaskByAssigneeId(it) } ?: emptyList()
        }
}

data class CreateUserInput(
    val email: String,
    val name: String,
)

data class UpdateUserInput(
    val email: String? = null,
    val name: String? = null,
)
