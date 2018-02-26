package io.dka.deuteronomy.presentation.navigation

import arrow.core.IdHK
import arrow.data.Reader
import arrow.data.map
import io.dka.deuteronomy.ioc.UsersContext
import io.dka.deuteronomy.view.UserDetailsScene

class UserDetailsPage
{
    fun go(userId: Long) = Reader.ask<IdHK, UsersContext.GetUsersContext>().map({ (ctx) ->
        UserDetailsScene.launch(ctx, userId)
    })
}
