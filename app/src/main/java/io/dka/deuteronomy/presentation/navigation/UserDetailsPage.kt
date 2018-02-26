package io.dka.deuteronomy.presentation.navigation

import arrow.core.IdHK
import arrow.data.Reader
import arrow.data.map
import io.dka.deuteronomy.ioc.GetUsersScope
import io.dka.deuteronomy.view.UserDetailsScene

class UserDetailsPage
{
    fun go(userId: Long) = Reader.ask<IdHK, GetUsersScope>().map({ (ctx) ->
        UserDetailsScene.launch(ctx, userId)
    })
}
