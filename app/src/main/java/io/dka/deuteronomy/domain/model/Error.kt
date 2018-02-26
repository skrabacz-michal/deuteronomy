package io.dka.deuteronomy.domain.model

import arrow.core.None
import arrow.core.Option

sealed class UsersError
{
    class UsersNotFound : UsersError()
    class UserNotFound(val id: Long) : UsersError()
    data class UnknownServerError(val e: Option<Throwable> = None) : UsersError()
}
