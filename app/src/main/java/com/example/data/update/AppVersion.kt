package com.example.data.update

/**
 * Dotted-numeric version handling for the GitHub release channel.
 *
 * Tags are written as `v3.0.10`, `3.0.10` or `v3.0.10-beta`; the first numeric group wins so all
 * three compare as `3.0.10`. Missing slots count as zero, which keeps `3.0.9` == `3.0.9.0`.
 */
object AppVersion {

    private val NUMBER_GROUP = Regex("""\d+(?:\.\d+)*""")

    /** `"v3.0.10"` -> `[3, 0, 10]`; `null` when [raw] has no usable version. */
    fun parse(raw: String?): List<Int>? {
        if (raw.isNullOrBlank()) return null
        val match = NUMBER_GROUP.find(raw)?.value ?: return null
        val parts = match.split('.').mapNotNull { it.toIntOrNull() }
        return parts.ifEmpty { null }
    }

    /** Canonical text form (`"v3.0.10"` -> `"3.0.10"`) used for comparisons and file names. */
    fun normalize(raw: String?): String? = parse(raw)?.joinToString(".")

    /** True when [candidate] is a strictly newer version than [current]. */
    fun isNewer(candidate: String?, current: String?): Boolean {
        val candidateParts = parse(candidate) ?: return false
        val currentParts = parse(current) ?: return false
        val slots = maxOf(candidateParts.size, currentParts.size)
        for (index in 0 until slots) {
            val left = candidateParts.getOrElse(index) { 0 }
            val right = currentParts.getOrElse(index) { 0 }
            if (left != right) return left > right
        }
        return false
    }
}

/** Human readable APK size, e.g. `"2.4 MB"`. */
fun formatBytesCompact(bytes: Long): String {
    if (bytes <= 0L) return "Unknown size"
    val kb = bytes / 1024.0
    if (kb < 1024.0) return "${kb.toInt().coerceAtLeast(1)} KB"
    val mb = kb / 1024.0
    return if (mb < 10.0) String.format("%.1f MB", mb) else "${mb.toInt()} MB"
}
