package io.dka.deuteronomy.ioc

import android.content.Context
import io.dka.deuteronomy.data.source.cloud.ApiClient
import io.dka.deuteronomy.data.source.cloud.ApiClientImpl
import io.dka.deuteronomy.presentation.UserDetailsView
import io.dka.deuteronomy.presentation.UsersView
import io.dka.deuteronomy.presentation.navigation.UserDetailsPage
import io.dka.deuteronomy.presentation.runner.BackgroundRunner
import io.dka.deuteronomy.presentation.runner.Runner

// TODO msq - inject ApiClient

data class GetUsersScope(val context: Context,
                         val view: UsersView,
                         val runner: Runner = BackgroundRunner(),
                         val apiClient: ApiClient = ApiClientImpl(),
                         val userDetailsPage: UserDetailsPage = UserDetailsPage())

data class GetUserDetailsScope(val context: Context,
                               val view: UserDetailsView,
                               val runner: Runner = BackgroundRunner(),
                               val apiClient: ApiClient = ApiClientImpl())
