package com.example.graphqldemo.repository

import com.example.graphqldemo.jooq.tables.pojos.Project
import com.example.graphqldemo.jooq.tables.references.PROJECT
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.NoSuchElementException

@Repository
class ProjectRepository(
    private val dsl: DSLContext,
) {
    fun findAll(): List<Project> =
        dsl
            .selectFrom(PROJECT)
            .fetchInto(Project::class.java)

    fun findById(id: Int): Project? =
        dsl
            .selectFrom(PROJECT)
            .where(PROJECT.ID.eq(id))
            .fetchOneInto(Project::class.java)

    fun create(
        name: String,
        description: String? = null,
    ): Project {
        val now = LocalDateTime.now()
        return dsl
            .insertInto(PROJECT)
            .set(PROJECT.NAME, name)
            .set(PROJECT.DESCRIPTION, description)
            .set(PROJECT.CREATED_AT, now)
            .set(PROJECT.UPDATED_AT, now)
            .returning()
            .fetchOneInto(Project::class.java)
            ?: throw IllegalStateException("Failed to create project")
    }

    fun update(
        id: Int,
        name: String? = null,
        description: String? = null,
    ): Project? {
        val update =
            dsl
                .update(PROJECT)
                .set(PROJECT.UPDATED_AT, LocalDateTime.now())

        name?.let { update.set(PROJECT.NAME, it) }
        description?.let { update.set(PROJECT.DESCRIPTION, it) }

        return update
            .where(PROJECT.ID.eq(id))
            .returning()
            .fetchOneInto(Project::class.java)
    }

    fun delete(id: Int): Boolean =
        dsl
            .deleteFrom(PROJECT)
            .where(PROJECT.ID.eq(id))
            .execute() > 0
} 
