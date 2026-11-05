package com.example.data.local

import com.example.model.FlowProject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProjectRepository(private val dao: ProjectDao) {
    val allProjects: Flow<List<FlowProject>> = dao.getAllProjects().map { entities ->
        entities.map { it.toDomainModel() }
    }

    suspend fun saveProject(project: FlowProject): Long {
        return dao.insertProject(ProjectEntity.fromDomainModel(project))
    }

    suspend fun deleteProject(id: Long) {
        dao.deleteProjectById(id)
    }
}
