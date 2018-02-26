package io.dka.deuteronomy.domain

import io.dka.deuteronomy.data.CachePolicy.NetworkOnly
import io.dka.deuteronomy.data.getUserDetails
import io.dka.deuteronomy.data.getUsers

fun getUsersUseCase() = getUsers(NetworkOnly)

fun getUserDetailsUseCase(userId: Long) = getUserDetails(NetworkOnly, userId)
