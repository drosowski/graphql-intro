package com.example.graphqldemo.service

import com.example.graphqldemo.jooq.tables.pojos.Project
import com.example.graphqldemo.repository.ProjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException

@Service
@Transactional
class ProjectService(private val projectRepository: ProjectRepository) {
    
    fun getAllProjects(): List<Project> = projectRepository.findAll()
    
    fun getProjectById(id: Int): Project? = projectRepository.findById(id)
    
    fun createProject(name: String, description: String? = null): Project =
        projectRepository.create(name, description)
    
    fun updateProject(id: Int, name: String? = null, description: String? = null): Project? =
        projectRepository.update(id, name, description)
    
    fun deleteProject(id: Int): Boolean = projectRepository.delete(id)
} 