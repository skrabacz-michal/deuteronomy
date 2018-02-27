package io.dka.deuteronomy.presentation

import arrow.core.IdHK
import arrow.core.Some
import arrow.core.identity
import arrow.data.Reader
import arrow.data.flatMap
import arrow.data.map
import arrow.effects.ev
import io.dka.deuteronomy.data.error.ApiError
import io.dka.deuteronomy.domain.getUserDetailsUseCase
import io.dka.deuteronomy.domain.getUsersUseCase
import io.dka.deuteronomy.domain.model.User
import io.dka.deuteronomy.domain.model.UsersError
import io.dka.deuteronomy.ioc.GetUserDetailsScope
import io.dka.deuteronomy.ioc.GetUsersScope
import java.net.HttpURLConnection

interface UserView
{
    fun showNotFoundError()
    fun showGenericError()
}

interface UsersView : UserView
{
    fun drawUsers(users: List<User>)
}

interface UserDetailsView : UserView
{
    fun drawUser(user: User)
}

fun onUserItemClick(userId: Long) = Reader.ask<IdHK, GetUsersScope>().flatMap({
    it.userDetailsPage.go(userId)
})

fun getUsers() = Reader.ask<IdHK, GetUsersScope>()
        .flatMap({ (_, view: UsersView) ->
            getUsersUseCase().map({ io ->
                io.ev().unsafeRunAsync { maybeUsers ->
                    maybeUsers.bimap(::exceptionAsUsersError, ::identity).fold(
                            { error -> drawError(error, view) },
                            { users -> drawUsers(users, view) })
                }
            })
        })

fun getUserDetails(userId: Long) = Reader.ask<IdHK, GetUserDetailsScope>()
        .flatMap({ (_, view: UserDetailsView) ->
            getUserDetailsUseCase(userId).map({ io ->
                io.ev().unsafeRunAsync { maybeUser ->
                    maybeUser.bimap(::exceptionAsUsersError, ::identity).fold(
                            { error -> drawError(error, view) },
                            { user -> drawUserDetails(user, view) })
                }
            })
        })

private fun drawError(error: UsersError, view: UserView) = when (error)
{
    is UsersError.UsersNotFound -> view.showNotFoundError()
    is UsersError.UserNotFound -> view.showNotFoundError()
    is UsersError.UnknownServerError -> view.showGenericError()
}

private fun drawUsers(users: List<User>, view: UsersView)
{
    view.drawUsers(users = users)
}

private fun drawUserDetails(user: User, view: UserDetailsView)
{
    view.drawUser(user)
}

private fun exceptionAsUsersError(throwable: Throwable): UsersError = when (throwable)
{
    is ApiError -> when (throwable.httpCode)
    {
        HttpURLConnection.HTTP_NOT_FOUND -> UsersError.UsersNotFound()
        else -> UsersError.UnknownServerError(Some(throwable))
    }
    else -> UsersError.UnknownServerError(Some(throwable))
}
