package com.example

import com.example.data.local.ProjectConverters
import com.example.model.CharacterPrompt
import com.example.model.FlowProject
import com.example.model.StoryboardShot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testFlowProjectScriptGeneration() {
        val characters = listOf(
            CharacterPrompt(
                id = "CHAR_1",
                name = "Ethan Vance",
                tag = "[CHAR_1: Ethan Vance]",
                role = "Cyber Infiltrator",
                appearance = "Silver-streaked dark hair, cybernetic eye, matte black coat with teal lights.",
                referenceImagePrompt = "Character turnaround concept sheet of [CHAR_1: Ethan Vance] --ar 16:9"
            )
        )

        val shots = listOf(
            StoryboardShot(
                shotNumber = 1,
                shotTitle = "The Approach",
                timeline = "00:00 - 00:04 (4s)",
                durationSeconds = 4,
                aspectRatio = "16:9",
                narrativeBeat = "Approaching target",
                cameraType = "Dolly forward",
                firstFramePrompt = "[CHAR_1: Ethan Vance] stands on rooftop --ar 16:9",
                motionPrompt = "Slow push-in with rain ripples.",
                flowAiParameters = "--motion 4 --ar 16:9"
            )
        )

        val project = FlowProject(
            title = "Neon Infiltration",
            logline = "A high-stakes cyber heist.",
            story = "Ethan Vance infiltrates the tower.",
            visualStyle = "Cyberpunk Neo-Noir",
            characters = characters,
            shots = shots
        )

        val script = project.buildFullScript()

        assertTrue(script.contains("[CHAR_1: Ethan Vance]"))
        assertTrue(script.contains("16:9"))
        assertTrue(script.contains("First Frame Keyframe Prompt"))
        assertTrue(script.contains("Flow AI Motion Animation Prompt"))
        assertTrue(script.contains("The Approach"))
        assertTrue(shots[0].firstFramePrompt.startsWith("[CHAR_1: Ethan Vance]"))
    }

    @Test
    fun testProjectConverters() {
        val converters = ProjectConverters()
        val originalChars = listOf(
            CharacterPrompt(
                id = "CHAR_1",
                name = "Lyra Chen",
                tag = "[CHAR_1: Lyra Chen]",
                role = "Astral Navigator",
                appearance = "Starlight suit",
                referenceImagePrompt = "Lyra turnaround"
            )
        )

        val json = converters.fromCharacterList(originalChars)
        val restoredChars = converters.toCharacterList(json)

        assertEquals(1, restoredChars.size)
        assertEquals("CHAR_1", restoredChars[0].id)
        assertEquals("Lyra Chen", restoredChars[0].name)
    }
}
