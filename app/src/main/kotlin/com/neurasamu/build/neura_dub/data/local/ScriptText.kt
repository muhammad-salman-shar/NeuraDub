package com.neurasamu.build.neura_dub.data.local

import kotlin.math.abs
import kotlin.math.roundToInt

object ScriptText {

    private const val DEFAULT_FLAP_MS = 120

    fun estimateFlapCount(timeInMs: Int, timeOutMs: Int): Int {
        val duration = (timeOutMs - timeInMs).coerceAtLeast(0)
        if (duration == 0) return 1
        return ((duration + DEFAULT_FLAP_MS / 2) / DEFAULT_FLAP_MS).coerceAtLeast(1)
    }

    fun countSyllables(text: String): Int {
        if (text.isBlank()) return 0
        val vowels = "aeiou"
        var count = 0
        var prevWasVowel = false
        for (ch in text.lowercase()) {
            val isVowel = ch in vowels
            if (isVowel && !prevWasVowel) count++
            prevWasVowel = isVowel
        }
        return count.coerceAtLeast(1)
    }

    fun flapDeltaPercent(syllables: Int, flaps: Int): Float {
        if (flaps == 0) return 0f
        return abs(syllables - flaps).toFloat() / flaps * 100f
    }

    fun formatTimecode(ms: Int, framerateMilli: Int): String {
        if (ms < 0) return "--:--:--"
        val totalSeconds = ms / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        val millis = ms % 1000
        val fps = framerateMilli / 1000.0
        val frames = if (fps > 0) ((millis / 1000.0) * fps).roundToInt() else 0
        return "%02d:%02d:%02d.%03d".format(hours, minutes, seconds, millis) +
            " (f$frames)"
    }
}
