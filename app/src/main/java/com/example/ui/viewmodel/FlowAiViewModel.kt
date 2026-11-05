package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.FlowAiPromptRepository
import com.example.data.local.AppDatabase
import com.example.data.local.ProjectRepository
import com.example.model.CharacterPrompt
import com.example.model.FlowProject
import com.example.model.StoryboardShot
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface FlowAiUiState {
    data object Idle : FlowAiUiState
    data class Generating(val step: String) : FlowAiUiState
    data class Success(val project: FlowProject) : FlowAiUiState
    data class Error(val message: String) : FlowAiUiState
}

data class StoryPreset(
    val title: String,
    val icon: String,
    val style: String,
    val story: String
)

class FlowAiViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val projectRepository = ProjectRepository(db.projectDao())
    private val aiRepository = FlowAiPromptRepository()

    val savedProjects: StateFlow<List<FlowProject>> = projectRepository.allProjects
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow<FlowAiUiState>(FlowAiUiState.Idle)
    val uiState: StateFlow<FlowAiUiState> = _uiState.asStateFlow()

    private val _storyInput = MutableStateFlow("")
    val storyInput: StateFlow<String> = _storyInput.asStateFlow()

    private val _selectedStyle = MutableStateFlow("Cinematic 35mm Film")
    val selectedStyle: StateFlow<String> = _selectedStyle.asStateFlow()

    private val _shotDuration = MutableStateFlow(4)
    val shotDuration: StateFlow<Int> = _shotDuration.asStateFlow()

    val aspectRatio: String = "16:9"

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    // Active project displayed in Storyboard & Script tabs
    private val _currentProject = MutableStateFlow<FlowProject?>(null)
    val currentProject: StateFlow<FlowProject?> = _currentProject.asStateFlow()

    val styleOptions = listOf(
        "Cinematic 35mm Film",
        "Japanese Anime (Ufotable)",
        "3D Unreal Engine 5",
        "Cyberpunk Neo-Noir",
        "Dark Fantasy Baroque",
        "Retro 80s Synthwave"
    )

    val durationOptions = listOf(3, 4, 5)

    val storyPresets = listOf(
        StoryPreset(
            title = "Cyber Infiltration",
            icon = "⚡",
            style = "Cyberpunk Neo-Noir",
            story = "In Neo-Kowloon 2099, cyber-infiltrator Ethan Vance breaches an orbital glass skybridge above glowing neon canyons. As he hacks the holographic terminal, corporate operative Maya Lin deploys a kinetic shock barrier while rain ripples in slow motion."
        ),
        StoryPreset(
            title = "Starlight Cartographer",
            icon = "✨",
            style = "Cinematic 35mm Film",
            story = "Astral navigator Lyra Chen stands at the observation deck of the starship Solstice, watching the dying corona of a sapphire supernova. She calibrates the glowing astrolabe ring as stardust drifts like floating embers across the zero-gravity cabin."
        ),
        StoryPreset(
            title = "Dragon Awakening",
            icon = "🐉",
            style = "Dark Fantasy Baroque",
            story = "Apprentice blacksmith Kael scales the obsidian altar of Mount Ignis to awaken the dormant magma dragon Ignacia. Striking his ancestral hammer upon the rune stone, molten embers surge through the dragon's stone scales as its golden eyes ignite."
        ),
        StoryPreset(
            title = "Tokyo Rain Romance",
            icon = "🌧️",
            style = "Japanese Anime (Ufotable)",
            story = "Under a warm noodle stall lantern in a rain-drenched Shinjuku alleyway, violinist Ren plays a melancholic melody. Street dancer Aoi halts under her clear umbrella as neon reflections and glowing raindrops dance around them in 24fps slow motion."
        )
    )

    init {
        // Load initial sample so user immediately sees how it works
        loadPreset(storyPresets.first())
    }

    fun updateStory(text: String) {
        _storyInput.value = text
    }

    fun setStyle(style: String) {
        _selectedStyle.value = style
    }

    fun setShotDuration(duration: Int) {
        _shotDuration.value = duration
    }

    fun clearStory() {
        _storyInput.value = ""
    }

    fun loadPreset(preset: StoryPreset) {
        _storyInput.value = preset.story
        _selectedStyle.value = preset.style
    }

    fun generatePrompts() {
        val story = _storyInput.value.trim()
        if (story.isBlank()) {
            viewModelScope.launch {
                _toastEvent.emit("Please enter a story before generating.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.value = FlowAiUiState.Generating("Analyzing story narrative beats...")
            try {
                _uiState.value = FlowAiUiState.Generating("Synthesizing characters & 4-shot storyboard...")
                val project = aiRepository.generateFlowPrompts(
                    story = story,
                    style = _selectedStyle.value,
                    shotDuration = _shotDuration.value,
                    aspectRatio = aspectRatio
                )
                _currentProject.value = project
                _uiState.value = FlowAiUiState.Success(project)
                _toastEvent.emit("✨ Flow AI prompts generated successfully!")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val errorMsg = e.localizedMessage ?: "Failed to generate prompts."
                _uiState.value = FlowAiUiState.Error(errorMsg)
                _toastEvent.emit("Error: $errorMsg")
            }
        }
    }

    fun saveCurrentProject() {
        val project = _currentProject.value ?: return
        viewModelScope.launch {
            try {
                val newId = projectRepository.saveProject(project)
                _currentProject.value = project.copy(id = newId)
                _toastEvent.emit("💾 Storyboard saved to your library!")
            } catch (e: Exception) {
                _toastEvent.emit("Failed to save: ${e.localizedMessage}")
            }
        }
    }

    fun loadSavedProject(project: FlowProject) {
        _currentProject.value = project
        _storyInput.value = project.story
        _selectedStyle.value = project.visualStyle
        _shotDuration.value = project.shotDurationSeconds
        _uiState.value = FlowAiUiState.Success(project)
        viewModelScope.launch {
            _toastEvent.emit("Loaded \"${project.title}\"")
        }
    }

    fun deleteProject(id: Long) {
        viewModelScope.launch {
            try {
                projectRepository.deleteProject(id)
                _toastEvent.emit("Project deleted.")
            } catch (e: Exception) {
                _toastEvent.emit("Error deleting: ${e.localizedMessage}")
            }
        }
    }
}
