package com.example.graphqldemo.repository

import com.example.graphqldemo.jooq.tables.pojos.User
import com.example.graphqldemo.jooq.tables.references.USER
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.NoSuchElementException

@Repository
class UserRepository(
    private val dsl: DSLContext,
) {
    fun findAll(): List<User> =
        dsl
            .selectFrom(USER)
            .fetchInto(User::class.java)

    fun findById(id: Int): User? =
        dsl
            .selectFrom(USER)
            .where(USER.ID.eq(id))
            .fetchOneInto(User::class.java)

    fun findByEmail(email: String): User? =
        dsl
            .selectFrom(USER)
            .where(USER.EMAIL.eq(email))
            .fetchOneInto(User::class.java)

    fun create(
        name: String,
        email: String,
    ): User {
        val now = LocalDateTime.now()
        return dsl
            .insertInto(USER)
            .set(USER.NAME, name)
            .set(USER.EMAIL, email)
            .set(USER.CREATED_AT, now)
            .set(USER.UPDATED_AT, now)
            .returning()
            .fetchOneInto(User::class.java)
            ?: throw IllegalStateException("Failed to create user")
    }

    fun update(
        id: Int,
        name: String? = null,
        email: String? = null,
    ): User? {
        val update =
            dsl
                .update(USER)
                .set(USER.UPDATED_AT, LocalDateTime.now())

        name?.let { update.set(USER.NAME, it) }
        email?.let { update.set(USER.EMAIL, it) }

        return update
            .where(USER.ID.eq(id))
            .returning()
            .fetchOneInto(User::class.java)
    }

    fun delete(id: Int): Boolean =
        dsl
            .deleteFrom(USER)
            .where(USER.ID.eq(id))
            .execute() > 0
}
