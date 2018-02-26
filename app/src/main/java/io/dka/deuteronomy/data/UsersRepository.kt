package io.dka.deuteronomy.data

import arrow.HK
import arrow.data.Reader
import arrow.data.map
import arrow.effects.IOHK
import arrow.syntax.functor.map
import io.dka.deuteronomy.data.source.cloud.fetchAllUsers
import io.dka.deuteronomy.data.source.cloud.fetchUserDetails
import io.dka.deuteronomy.domain.model.User
import io.dka.deuteronomy.ioc.GetUserDetailsScope
import io.dka.deuteronomy.ioc.GetUsersScope

sealed class CachePolicy
{
    object NetworkOnly : CachePolicy()
//    object NetworkFirst : CachePolicy()
//    object LocalOnly : CachePolicy()
//    object LocalFirst : CachePolicy()
}

fun getUsers(cache: CachePolicy): Reader<GetUsersScope, HK<IOHK, List<User>>> = when (cache)
{
    CachePolicy.NetworkOnly -> fetchAllUsers()
            .map({ it.map { it.map { it.map() } } }) // TODO msq - must be a better way
}

fun getUserDetails(cache: CachePolicy, userId: Long): Reader<GetUserDetailsScope, HK<IOHK, User>> = when (cache)
{
    CachePolicy.NetworkOnly -> fetchUserDetails(userId)
            .map({ it.map { it.map() } }) // TODO msq - must be a better way
}
