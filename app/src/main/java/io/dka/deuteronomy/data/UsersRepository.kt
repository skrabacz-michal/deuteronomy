package io.dka.deuteronomy.data

import arrow.data.map
import arrow.syntax.functor.map
import io.dka.deuteronomy.data.source.cloud.fetchAllUsers
import io.dka.deuteronomy.data.source.cloud.fetchUserDetails

sealed class CachePolicy
{
    object NetworkOnly : CachePolicy()
}

fun getUsers(cache: CachePolicy) = when (cache)
{
    CachePolicy.NetworkOnly -> fetchAllUsers()
            .map({ it.map { it.map { it.map() } } }) // TODO msq - must be a better way

}

fun getUserDetails(cache: CachePolicy, userId: Long) = when (cache)
{
    CachePolicy.NetworkOnly -> fetchUserDetails(userId)
            .map({ it.map { it.map() } }) // TODO msq - must be a better way
}
