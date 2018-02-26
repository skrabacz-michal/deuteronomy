package io.dka.deuteronomy.data.source.cloud

import arrow.HK
import arrow.core.IdHK
import arrow.data.Reader
import arrow.data.map
import arrow.effects.IO
import arrow.effects.IOHK
import arrow.effects.monadSuspend
import arrow.typeclasses.bindingCatch
import io.dka.deuteronomy.data.model.UserEntity
import io.dka.deuteronomy.ioc.GetUserDetailsScope
import io.dka.deuteronomy.ioc.GetUsersScope

fun fetchAllUsers(): Reader<GetUsersScope, HK<IOHK, List<UserEntity>>> = Reader.ask<IdHK, GetUsersScope>().map({ ctx ->
    IO.monadSuspend().bindingCatch {
        ctx.runner.run(
                f = { queryForUsers(ctx) },
                onError = { IO.raiseError<List<UserEntity>>(it) },
                onSuccess = { IO.pure(it) }
        ).bind()
    }
})

fun fetchUserDetails(userId: Long): Reader<GetUserDetailsScope, HK<IOHK, UserEntity>> = Reader.ask<IdHK, GetUserDetailsScope>().map({ ctx ->
    IO.monadSuspend().bindingCatch {
        ctx.runner.run(
                f = { queryForUser(ctx, userId) },
                onError = { IO.raiseError<UserEntity>(it) },
                onSuccess = { IO.pure(it) })
                .bind()
    }
})

private fun queryForUsers(context: GetUsersScope): List<UserEntity> =
        context.apiClient.fetchUsers()

private fun queryForUser(context: GetUserDetailsScope, userId: Long): UserEntity =
        context.apiClient.fetchUser(userId)
