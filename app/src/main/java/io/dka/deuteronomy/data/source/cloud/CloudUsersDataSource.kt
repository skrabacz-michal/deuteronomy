package io.dka.deuteronomy.data.source.cloud

import android.support.annotation.VisibleForTesting
import arrow.HK
import arrow.core.IdHK
import arrow.data.Reader
import arrow.data.Try
import arrow.data.map
import arrow.effects.Async
import arrow.effects.IO
import arrow.effects.monadSuspend
import arrow.syntax.either.right
import arrow.typeclasses.bindingCatch
import io.dka.deuteronomy.data.error.ApiError
import io.dka.deuteronomy.data.model.UserEntity
import io.dka.deuteronomy.ioc.UsersContext.GetUserDetailsContext
import io.dka.deuteronomy.ioc.UsersContext.GetUsersContext
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import java.net.HttpURLConnection

fun fetchAllUsers() = Reader.ask<IdHK, GetUsersContext>().map({ ctx ->
    IO.monadSuspend().bindingCatch {
        runInAsyncContext(
                f = { queryForUsers(ctx) },
                onError = { IO.raiseError<List<UserEntity>>(it) },
                onSuccess = { IO.pure(it) },
                AC = ctx.threading
        ).bind()
    }
})

fun fetchUserDetails(userId: Long) = Reader.ask<IdHK, GetUserDetailsContext>().map({ ctx ->
    IO.monadSuspend().bindingCatch {
        runInAsyncContext(
                f = { queryForUser(ctx, userId) },
                onError = { IO.raiseError<UserEntity>(it) },
                onSuccess = { IO.pure(it) },
                AC = ctx.threading).bind()
    }
})


@VisibleForTesting
fun queryForUsers(context: GetUsersContext): List<UserEntity> =
        context.apiClient.users().execute().let { response ->
            when (response.isSuccessful)
            {
                true -> response.body() ?: emptyList()
                false -> throw ApiError(httpCode = response.code(), description = response.message())
            }
        }

@VisibleForTesting
fun queryForUser(context: GetUserDetailsContext, userId: Long): UserEntity =
        context.apiClient.user(userId).execute().let { response ->
            when (response.isSuccessful)
            {
                true -> response.body() ?: throw ApiError(httpCode = HttpURLConnection.HTTP_NOT_FOUND, description = response.message())
                false -> throw ApiError(httpCode = response.code(), description = response.message())
            }
        }

/**
 * Run async and fold result
 */
private fun <F, A, B> runInAsyncContext(
        f: () -> A,
        onError: (Throwable) -> B,
        onSuccess: (A) -> B, AC: Async<F>): HK<F, B>
{
    return AC.async { process ->
        async(CommonPool) {
            val result = Try { f() }.fold(onError, onSuccess)
            process(result.right())
        }
    }
}
