package io.dka.deuteronomy.data.error

class ApiError(val httpCode: Int, description: String? = null) : Throwable(description)
