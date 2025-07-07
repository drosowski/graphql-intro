package com.example.graphqldemo.service

import com.example.graphqldemo.jooq.tables.pojos.Task
import com.example.graphqldemo.jooq.tables.pojos.User
import com.example.graphqldemo.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
) {
    fun getAllUsers(): List<User> = userRepository.findAll()

    fun getUserById(id: Int): User? = userRepository.findById(id)

    fun createUser(
        name: String,
        email: String,
    ): User {
        if (userRepository.findByEmail(email) != null) {
            throw IllegalArgumentException("User with email $email already exists")
        }
        return userRepository.create(name, email)
    }

    fun updateUser(
        id: Int,
        name: String? = null,
        email: String? = null,
    ): User? {
        email?.let {
            val existingUser = userRepository.findByEmail(it)
            if (existingUser != null && existingUser.id != id) {
                throw IllegalArgumentException("User with email $email already exists")
            }
        }
        return userRepository.update(id, name, email)
    }

    fun deleteUser(id: Int): Boolean = userRepository.delete(id)
}
