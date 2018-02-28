package io.dka.deuteronomy.domain

import io.dka.deuteronomy.data.CachePolicy.NetworkOnly
import io.dka.deuteronomy.data.getUserDetails
import io.dka.deuteronomy.data.getUsers
import io.dka.deuteronomy.domain.model.User
import io.dka.deuteronomy.ioc.GetUserDetailsScope
import io.dka.deuteronomy.ioc.GetUsersScope

fun getUsersUseCase(): AsyncResult<GetUsersScope, List<User>> = getUsers(NetworkOnly)

fun getUserDetailsUseCase(userId: Long): AsyncResult<GetUserDetailsScope, User> = getUserDetails(NetworkOnly, userId)
