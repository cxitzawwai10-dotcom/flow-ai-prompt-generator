package com.example.model

data class CharacterPrompt(
    val id: String = "", // e.g. "CHAR_1"
    val name: String = "", // e.g. "Ethan Vance"
    val tag: String = "", // e.g. "[CHAR_1: Ethan Vance]"
    val role: String = "", // e.g. "Protagonist / Cyber Infiltrator"
    val appearance: String = "", // Detailed consistent physical traits
    val referenceImagePrompt: String = "" // Midjourney / Flow AI character turnaround prompt
)

data class StoryboardShot(
    val shotNumber: Int = 1,
    val shotTitle: String = "",
    val timeline: String = "", // e.g. "00:00 - 00:04 (4s)"
    val durationSeconds: Int = 4,
    val aspectRatio: String = "16:9",
    val narrativeBeat: String = "",
    val cameraType: String = "", // e.g. "Slow Dolly Zoom"
    val firstFramePrompt: String = "", // Begins with [CHAR_X: Name]
    val motionPrompt: String = "", // Flow AI motion physics, camera movement, speed
    val flowAiParameters: String = "--motion 4 --camera push-in --fps 24 --ar 16:9"
)

data class FlowProject(
    val id: Long = 0,
    val title: String = "",
    val logline: String = "",
    val story: String = "",
    val visualStyle: String = "Cinematic 35mm Film",
    val aspectRatio: String = "16:9",
    val shotDurationSeconds: Int = 4,
    val characters: List<CharacterPrompt> = emptyList(),
    val shots: List<StoryboardShot> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) {
    val totalRuntimeSeconds: Int
        get() = shots.sumOf { it.durationSeconds }

    fun buildFullScript(): String {
        val sb = StringBuilder()
        sb.append("# $title - Flow AI Animation Script\n\n")
        sb.append("**Logline:** $logline\n")
        sb.append("**Visual Style:** $visualStyle\n")
        sb.append("**Aspect Ratio:** $aspectRatio\n")
        sb.append("**Total Duration:** ${totalRuntimeSeconds}s (4 Shots, ${shotDurationSeconds}s each)\n\n")
        sb.append("---\n\n")
        sb.append("## 🎭 CHARACTER REFERENCE PROMPTS\n\n")
        characters.forEach { char ->
            sb.append("### ${char.tag} (${char.role})\n")
            sb.append("**Consistent Appearance:** ${char.appearance}\n\n")
            sb.append("**Character Sheet / Reference Prompt:**\n")
            sb.append("```\n${char.referenceImagePrompt}\n```\n\n")
        }
        sb.append("---\n\n")
        sb.append("## 🎬 4-SHOT STORYBOARD & MOTION PROMPTS\n\n")
        shots.forEach { shot ->
            sb.append("### Shot ${shot.shotNumber}: ${shot.shotTitle} [${shot.timeline}]\n")
            sb.append("**Narrative Beat:** ${shot.narrativeBeat}\n")
            sb.append("**Camera Movement:** ${shot.cameraType}\n")
            sb.append("**Aspect Ratio:** ${shot.aspectRatio}\n\n")
            sb.append("**First Frame Keyframe Prompt:**\n")
            sb.append("```\n${shot.firstFramePrompt}\n```\n\n")
            sb.append("**Flow AI Motion Animation Prompt:**\n")
            sb.append("```\n${shot.motionPrompt}\n```\n\n")
            sb.append("**Flow Parameters:** `${shot.flowAiParameters}`\n\n")
        }
        return sb.toString()
    }
}
