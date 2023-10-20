package com.example.vcare_app.model

data class ErrorResponse(val message: Array<String>, val error: String, val statusCode: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ErrorResponse

        if (!message.contentEquals(other.message)) return false
        if (error != other.error) return false
        if (statusCode != other.statusCode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = message.contentHashCode()
        result = 31 * result + error.hashCode()
        result = 31 * result + statusCode
        return result
    }
}