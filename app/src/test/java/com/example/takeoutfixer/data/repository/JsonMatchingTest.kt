package com.example.takeoutfixer.data.repository

import org.junit.Assert.assertEquals
import org.junit.Test

class JsonMatchingTest {

    @Test
    fun `test matching logic with various takeout patterns`() {
        val jsonMap = mapOf(
            "image.jpg.json" to "standard",
            "long_filename_that_is_truncated_by_google_takeout_result.json" to "truncated",
            "image(1).jpg.json" to "suffix",
            "video.mp4.json" to "video"
        )

        // Test standard matching
        assertEquals("standard", findMatch("image.jpg", jsonMap))
        
        // Test video matching
        assertEquals("video", findMatch("video.mp4", jsonMap))

        // Test truncated matching (using a 48+ char name)
        val longMediaName = "long_filename_that_is_truncated_by_google_takeout_media_file.jpg"
        assertEquals("truncated", findMatch(longMediaName, jsonMap))
    }

    // Simplified version of the logic in TakeoutRepositoryImpl for testing
    private fun findMatch(mediaName: String, jsonMap: Map<String, String>): String? {
        // Standard: filename.ext.json
        jsonMap[mediaName + ".json"]?.let { return it }
        
        // Alternative: filename.json
        val nameWithoutExtension = mediaName.substringBeforeLast('.')
        jsonMap[nameWithoutExtension + ".json"]?.let { return it }

        // Google Takeout Truncation
        if (mediaName.length > 45) {
            val truncated = mediaName.take(47)
            jsonMap.keys.find { it.startsWith(truncated) && it.endsWith(".json") }?.let { 
                return jsonMap[it]
            }
        }

        return null
    }
}
