package io.dka.deuteronomy.data.error

import arrow.core.None
import arrow.core.Option

class ApiError(val httpCode: Int, description: String?) : Throwable(description)
