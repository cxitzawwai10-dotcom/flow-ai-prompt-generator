package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.model.CharacterPrompt
import com.example.model.FlowProject
import com.example.model.StoryboardShot
import org.json.JSONArray
import org.json.JSONObject

@Entity(tableName = "saved_projects")
@TypeConverters(ProjectConverters::class)
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val logline: String,
    val story: String,
    val visualStyle: String,
    val aspectRatio: String,
    val shotDurationSeconds: Int,
    val characters: List<CharacterPrompt>,
    val shots: List<StoryboardShot>,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomainModel(): FlowProject {
        return FlowProject(
            id = id,
            title = title,
            logline = logline,
            story = story,
            visualStyle = visualStyle,
            aspectRatio = aspectRatio,
            shotDurationSeconds = shotDurationSeconds,
            characters = characters,
            shots = shots,
            createdAt = createdAt
        )
    }

    companion object {
        fun fromDomainModel(project: FlowProject): ProjectEntity {
            return ProjectEntity(
                id = project.id,
                title = project.title,
                logline = project.logline,
                story = project.story,
                visualStyle = project.visualStyle,
                aspectRatio = project.aspectRatio,
                shotDurationSeconds = project.shotDurationSeconds,
                characters = project.characters,
                shots = project.shots,
                createdAt = project.createdAt
            )
        }
    }
}

class ProjectConverters {
    private val moshi = com.squareup.moshi.Moshi.Builder()
        .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
        .build()
    private val characterListType = com.squareup.moshi.Types.newParameterizedType(
        List::class.java,
        CharacterPrompt::class.java
    )
    private val shotListType = com.squareup.moshi.Types.newParameterizedType(
        List::class.java,
        StoryboardShot::class.java
    )

    private val charAdapter = moshi.adapter<List<CharacterPrompt>>(characterListType)
    private val shotAdapter = moshi.adapter<List<StoryboardShot>>(shotListType)

    @TypeConverter
    fun fromCharacterList(characters: List<CharacterPrompt>): String {
        return charAdapter.toJson(characters)
    }

    @TypeConverter
    fun toCharacterList(jsonStr: String): List<CharacterPrompt> {
        if (jsonStr.isBlank()) return emptyList()
        return charAdapter.fromJson(jsonStr) ?: emptyList()
    }

    @TypeConverter
    fun fromShotList(shots: List<StoryboardShot>): String {
        return shotAdapter.toJson(shots)
    }

    @TypeConverter
    fun toShotList(jsonStr: String): List<StoryboardShot> {
        if (jsonStr.isBlank()) return emptyList()
        return shotAdapter.fromJson(jsonStr) ?: emptyList()
    }
}
