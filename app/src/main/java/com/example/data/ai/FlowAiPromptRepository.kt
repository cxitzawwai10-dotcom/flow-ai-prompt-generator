package com.example.data.ai

import android.util.Log
import com.example.model.CharacterPrompt
import com.example.model.FlowProject
import com.example.model.StoryboardShot
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

private const val TAG = "FlowAiRepo"

class FlowAiPromptRepository {

    // Define JSON schema for strict structured output
    private val characterSchema = Schema.obj(
        properties = mapOf(
            "id" to Schema.string(description = "Character ID identifier, e.g. CHAR_1"),
            "name" to Schema.string(description = "Character name, e.g. Ethan Vance"),
            "tag" to Schema.string(description = "Character formatted tag, e.g. [CHAR_1: Ethan Vance]"),
            "role" to Schema.string(description = "Character narrative role, e.g. Cyber Infiltrator"),
            "appearance" to Schema.string(description = "Strict consistent physical visual description: face, hair, clothing, signature accessories, color scheme"),
            "referenceImagePrompt" to Schema.string(description = "Midjourney / Flow AI character turnaround reference sheet prompt with neutral studio lighting and multiple angles --ar 16:9")
        )
    )

    private val shotSchema = Schema.obj(
        properties = mapOf(
            "shotNumber" to Schema.integer(description = "Sequential shot index 1, 2, 3, or 4"),
            "shotTitle" to Schema.string(description = "Short descriptive title of the shot"),
            "timeline" to Schema.string(description = "Timeline timecode range, e.g. 00:00 - 00:04 (4s)"),
            "durationSeconds" to Schema.integer(description = "Shot length in seconds, e.g. 4"),
            "aspectRatio" to Schema.string(description = "Aspect ratio string, always '16:9'"),
            "narrativeBeat" to Schema.string(description = "Story progression happening in this shot"),
            "cameraType" to Schema.string(description = "Camera motion description, e.g. Cinematic Low-Angle Dolly In"),
            "firstFramePrompt" to Schema.string(description = "Starting frame image prompt. CRITICAL: MUST START with the character ID and name, e.g. '[CHAR_1: Ethan Vance] ...' followed by environment, lighting, pose, 16:9 composition, 8k quality"),
            "motionPrompt" to Schema.string(description = "Flow AI motion and animation instructions: camera velocity, subject movement, fluid dynamics, lighting change, speed pacing"),
            "flowAiParameters" to Schema.string(description = "Flow AI parameter flags, e.g. '--motion 4 --camera push-in --fps 24 --ar 16:9'")
        )
    )

    private val flowProjectSchema = Schema.obj(
        properties = mapOf(
            "title" to Schema.string(description = "Cinematic title for the animation"),
            "logline" to Schema.string(description = "Compelling logline summarizing the story"),
            "visualStyle" to Schema.string(description = "Visual aesthetic and art style"),
            "characters" to Schema.array(items = characterSchema, description = "Identified characters with IDs"),
            "shots" to Schema.array(items = shotSchema, description = "Exactly 4 chronological storyboard shots")
        )
    )

    private val genConfig by lazy {
        generationConfig {
            responseMimeType = "application/json"
            responseSchema = flowProjectSchema
            temperature = 0.7f
        }
    }

    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = genConfig
        )
    }

    suspend fun generateFlowPrompts(
        story: String,
        style: String,
        shotDuration: Int = 4,
        aspectRatio: String = "16:9"
    ): FlowProject = withContext(Dispatchers.IO) {
        val totalTime = shotDuration * 4
        val prompt = buildString {
            append("You are an expert director and lead prompt engineer for Flow AI and cinematic generative video (Runway Gen-3, Kling, Sora, Luma Dream Machine).\n\n")
            append("Analyze the following user story and generate professional English prompts for a 4-shot animated sequence in Flow AI.\n\n")
            append("USER STORY:\n\"$story\"\n\n")
            append("SPECIFICATIONS & STRICT MANDATES:\n")
            append("1. ALL outputs must be in fluent, evocative, professional English.\n")
            append("2. Extract 1 to 3 distinct characters and assign IDs: [CHAR_1: Full Name], [CHAR_2: Full Name], etc.\n")
            append("3. For each character, produce a comprehensive Character Reference Image Prompt (turnaround character sheet, neutral background, front and profile views, 8k, detailed clothing and physical traits) so the creator can lock in their appearance in Flow AI.\n")
            append("4. Keep character appearances 100% consistent across all shots (same outfit details, hair color/style, eye color, distinctive items).\n")
            append("5. MUST HAVE EXACTLY 4 SHOTS representing a classic cinematic four-act mini-arc (Shot 1: Establishing & Setup, Shot 2: Inciting Incident / Action, Shot 3: Climax / Turning Point, Shot 4: Resolution / Lingering Finale).\n")
            append("6. CRITICAL RULE FOR EVERY SHOT: Every shot's 'firstFramePrompt' MUST BEGIN WITH the character ID and name, e.g. '[CHAR_1: Ethan Vance] ...' or '[CHAR_1: Ethan Vance] and [CHAR_2: Maya Lin] ...'. Never omit the character ID prefix.\n")
            append("7. First Frame Prompts must include detailed scene composition, cinematic lighting (volumetric, anamorphic rim light, golden hour, neon backlight), background environment, camera focal length, and end with '--ar 16:9'.\n")
            append("8. Motion Prompts must provide precise animation instructions for Flow AI: camera trajectory (e.g. slow orbit, forward track, tilt up), character action, secondary physics (wind blowing cloth, smoke drifting, rain ripples), and camera speed/flow dynamics.\n")
            append("9. Timeline: Shot duration is $shotDuration seconds each (Shot 1: 00:00 - 00:0${shotDuration} (${shotDuration}s), Shot 2: 00:0${shotDuration} - 00:${String.format("%02d", shotDuration * 2)} (${shotDuration}s), Shot 3: 00:${String.format("%02d", shotDuration * 2)} - 00:${String.format("%02d", shotDuration * 3)} (${shotDuration}s), Shot 4: 00:${String.format("%02d", shotDuration * 3)} - 00:${String.format("%02d", totalTime)} (${shotDuration}s)). Total: ${totalTime}s.\n")
            append("10. Visual Style: '$style'. Aspect Ratio: '$aspectRatio'.\n")
            append("Generate the structured JSON according to the schema.")
        }

        try {
            val response = withTimeout(60_000L) {
                model.generateContent(prompt)
            }
            val jsonText = response.text ?: throw IllegalStateException("Model returned empty text.")
            Log.d(TAG, "Raw response received: ${jsonText.take(200)}...")

            parseFlowProjectJson(jsonText, story, style, shotDuration, aspectRatio)
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Generation timed out after 60s", e)
            throw IllegalStateException("AI prompt generation timed out. Please check your network and try again.")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Generation failed", e)
            throw e
        }
    }

    private fun parseFlowProjectJson(
        jsonString: String,
        originalStory: String,
        style: String,
        shotDuration: Int,
        aspectRatio: String
    ): FlowProject {
        val root = JSONObject(jsonString)
        val title = root.optString("title", "Untitled Animation")
        val logline = root.optString("logline", "")
        val visualStyle = root.optString("visualStyle", style)

        // Parse characters
        val charactersList = mutableListOf<CharacterPrompt>()
        val charArray = root.optJSONArray("characters")
        if (charArray != null) {
            for (i in 0 until charArray.length()) {
                val cObj = charArray.getJSONObject(i)
                val id = cObj.optString("id", "CHAR_${i + 1}")
                val name = cObj.optString("name", "Character ${i + 1}")
                val tag = cObj.optString("tag", "[$id: $name]")
                val role = cObj.optString("role", "Key Character")
                val appearance = cObj.optString("appearance", "")
                val refPrompt = cObj.optString("referenceImagePrompt", "")
                charactersList.add(
                    CharacterPrompt(
                        id = id,
                        name = name,
                        tag = tag,
                        role = role,
                        appearance = appearance,
                        referenceImagePrompt = refPrompt
                    )
                )
            }
        }

        // Parse 4 shots
        val shotsList = mutableListOf<StoryboardShot>()
        val shotArray = root.optJSONArray("shots")
        if (shotArray != null) {
            for (i in 0 until shotArray.length()) {
                val sObj = shotArray.getJSONObject(i)
                val shotNum = sObj.optInt("shotNumber", i + 1)
                val shotTitle = sObj.optString("shotTitle", "Shot $shotNum")
                val startSec = i * shotDuration
                val endSec = (i + 1) * shotDuration
                val defaultTimeline = String.format("00:%02d - 00:%02d (%ds)", startSec, endSec, shotDuration)
                val timeline = sObj.optString("timeline", defaultTimeline)
                val duration = sObj.optInt("durationSeconds", shotDuration)
                val ar = sObj.optString("aspectRatio", aspectRatio)
                val beat = sObj.optString("narrativeBeat", "")
                val camera = sObj.optString("cameraType", "Cinematic Smooth Track")
                var firstFrame = sObj.optString("firstFramePrompt", "")
                val motion = sObj.optString("motionPrompt", "")
                val params = sObj.optString("flowAiParameters", "--motion 4 --ar $aspectRatio --fps 24")

                // Enforce character ID prefix if model forgot on first frame
                if (charactersList.isNotEmpty() && !firstFrame.startsWith("[")) {
                    val primaryChar = charactersList.first()
                    firstFrame = "${primaryChar.tag} $firstFrame"
                }

                shotsList.add(
                    StoryboardShot(
                        shotNumber = shotNum,
                        shotTitle = shotTitle,
                        timeline = timeline,
                        durationSeconds = duration,
                        aspectRatio = ar,
                        narrativeBeat = beat,
                        cameraType = camera,
                        firstFramePrompt = firstFrame,
                        motionPrompt = motion,
                        flowAiParameters = params
                    )
                )
            }
        }

        return FlowProject(
            title = title,
            logline = logline,
            story = originalStory,
            visualStyle = visualStyle,
            aspectRatio = aspectRatio,
            shotDurationSeconds = shotDuration,
            characters = charactersList,
            shots = shotsList,
            createdAt = System.currentTimeMillis()
        )
    }
}
