package io.dka.deuteronomy.data.source.cloud

import io.dka.deuteronomy.BuildConfig
import io.dka.deuteronomy.data.error.ApiError
import io.dka.deuteronomy.data.model.UserEntity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ApiClient
{
    @Throws(ApiError::class)
    fun fetchUsers(): List<UserEntity>

    @Throws(ApiError::class)
    fun fetchUser(userId: Long): UserEntity
}

open class ApiClientImpl : ApiClient
{
    private val retrofit: Retrofit
        get() = Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    private val apiClient: Api
        get() = retrofit
                .create(Api::class.java)

    override fun fetchUsers(): List<UserEntity>
    {
        return apiClient.users().execute().let { response ->
            when (response.isSuccessful)
            {
                true -> response.body() ?: emptyList()
                false -> throw ApiError(httpCode = response.code(), description = response.message())
            }
        }
    }

    override fun fetchUser(userId: Long): UserEntity
    {
        return apiClient.user(userId).execute().let { response ->
            when (response.isSuccessful)
            {
                true -> response.body() ?: UserEntity.empty()
                false -> throw ApiError(httpCode = response.code(), description = response.message())
            }
        }
    }
}
