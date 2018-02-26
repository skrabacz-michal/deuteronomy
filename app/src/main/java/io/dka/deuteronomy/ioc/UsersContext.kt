package io.dka.deuteronomy.ioc

import android.content.Context
import arrow.effects.IO
import arrow.effects.async
import io.dka.deuteronomy.BuildConfig
import io.dka.deuteronomy.data.source.cloud.Api
import io.dka.deuteronomy.presentation.UserDetailsView
import io.dka.deuteronomy.presentation.UsersView
import io.dka.deuteronomy.presentation.navigation.UserDetailsPage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed class UsersContext(context: Context)
{
    val userDetailsPage = UserDetailsPage()

    val apiClient: Api
        get() = Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)

    val threading = IO.async()

    data class GetUsersContext(val context: Context, val view: UsersView) : UsersContext(context)
    data class GetUserDetailsContext(val context: Context, val view: UserDetailsView) : UsersContext(context)
}
