package com.example.notezz.model

data class ErrorResponse(
    val error: ErrorData
    )
data class ErrorData(
    val status: Int,
    val message: String
)
