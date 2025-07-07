package com.example.graphqldemo.repository

import com.example.graphqldemo.jooq.tables.references.TASK
import com.example.graphqldemo.jooq.tables.pojos.Task
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.NoSuchElementException

@Repository
class TaskRepository(private val dsl: DSLContext) {
    
    fun findAll(): List<Task> =
        dsl.selectFrom(TASK)
           .fetchInto(Task::class.java)
    
    fun findById(id: Int): Task? =
        dsl.selectFrom(TASK)
           .where(TASK.ID.eq(id))
           .fetchOneInto(Task::class.java)
    
    fun findByProjectId(projectId: Int): List<Task> =
        dsl.selectFrom(TASK)
           .where(TASK.PROJECT_ID.eq(projectId))
           .fetchInto(Task::class.java)
    
    fun findByAssigneeId(assigneeId: Int): List<Task> =
        dsl.selectFrom(TASK)
           .where(TASK.ASSIGNEE_ID.eq(assigneeId))
           .fetchInto(Task::class.java)
    
    fun create(
        title: String,
        description: String? = null,
        projectId: Int,
        assigneeId: Int? = null,
        status: String = "TODO"
    ): Task {
        val now = LocalDateTime.now()
        return dsl.insertInto(TASK)
            .set(TASK.TITLE, title)
            .set(TASK.DESCRIPTION, description)
            .set(TASK.PROJECT_ID, projectId)
            .set(TASK.ASSIGNEE_ID, assigneeId)
            .set(TASK.STATUS, status)
            .set(TASK.CREATED_AT, now)
            .set(TASK.UPDATED_AT, now)
            .returning()
            .fetchOneInto(Task::class.java)
            ?: throw IllegalStateException("Failed to create task")
    }
    
    fun update(
        id: Int,
        title: String? = null,
        description: String? = null,
        status: String? = null,
        assigneeId: Int? = null
    ): Task? {
        val update = dsl.update(TASK)
            .set(TASK.UPDATED_AT, LocalDateTime.now())
        
        title?.let { update.set(TASK.TITLE, it) }
        description?.let { update.set(TASK.DESCRIPTION, it) }
        status?.let { update.set(TASK.STATUS, it) }
        update.set(TASK.ASSIGNEE_ID, assigneeId) // Can be set to null
        
        return update.where(TASK.ID.eq(id))
            .returning()
            .fetchOneInto(Task::class.java)
    }
    
    fun updateStatus(id: Int, status: String): Task? =
        dsl.update(TASK)
           .set(TASK.STATUS, status)
           .set(TASK.UPDATED_AT, LocalDateTime.now())
           .where(TASK.ID.eq(id))
           .returning()
           .fetchOneInto(Task::class.java)
    
    fun delete(id: Int): Boolean =
        dsl.deleteFrom(TASK)
           .where(TASK.ID.eq(id))
           .execute() > 0
} 