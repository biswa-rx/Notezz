package com.example.notezz.model.auth_model

data class ErrorResponse(
    var error: ErrorData
    )
data class ErrorData(
    var status: Int,
    var message: String
)
