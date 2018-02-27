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

object AppScope
{
    val apiClient: ApiClient by lazy { ApiClientImpl() }

    val backgroundRunner: Runner by lazy { BackgroundRunner() }
}

data class GetUsersScope(val context: Context,
                         val view: UsersView,
                         val runner: Runner = AppScope.backgroundRunner,
                         val apiClient: ApiClient = AppScope.apiClient,
                         val userDetailsPage: UserDetailsPage = UserDetailsPage())

data class GetUserDetailsScope(val context: Context,
                               val view: UserDetailsView,
                               val runner: Runner = AppScope.backgroundRunner,
                               val apiClient: ApiClient = AppScope.apiClient)
