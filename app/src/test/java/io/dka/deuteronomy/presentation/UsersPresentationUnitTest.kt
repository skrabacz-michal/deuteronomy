package io.dka.deuteronomy.presentation

import android.test.mock.MockContext
import com.nhaarman.mockito_kotlin.*
import io.dka.deuteronomy.data.error.ApiError
import io.dka.deuteronomy.data.model.UserEntity
import io.dka.deuteronomy.data.source.cloud.ApiClient
import io.dka.deuteronomy.data.source.cloud.ApiClientImpl
import io.dka.deuteronomy.domain.model.User
import io.dka.deuteronomy.ioc.GetUsersScope
import io.dka.deuteronomy.presentation.runner.SingleThreadRunner
import org.junit.Test
import java.net.HttpURLConnection

class UsersPresentationUnitTest
{
    private val singleThreadRunner = SingleThreadRunner()

    @Test
    fun getUsers_empty()
    {
        // given
        val view = mock<UsersView> {}
        val apiClient = mock<ApiClient> {
            on { fetchUsers() } doReturn emptyList<UserEntity>()
        }
        val context = GetUsersScope(context = MockContext(), view = view, runner = singleThreadRunner, apiClient = apiClient)

        // when
        getUsers().run(context)

        // given
        verify(view).drawUsers(any())
    }

    @Test
    fun getUsers_isCorrect()
    {
        // given
        val users = createUsers(20)
        val view = mock<UsersView> {}
        val apiClient = mock<ApiClient> {
            on { fetchUsers() } doReturn users
        }
        val context = GetUsersScope(context = MockContext(), view = view, runner = singleThreadRunner, apiClient = apiClient)

        // when
        getUsers().run(context)

        // given
        verify(view).drawUsers(users.map { User(id = it.id, name = it.name) })
    }

    @Test
    fun getUsers_noFound()
    {
        // given
        val view = mock<UsersView> {}
        val apiClient = mock<ApiClientImpl> {
            on { fetchUsers() } doAnswer { throw ApiError(httpCode = HttpURLConnection.HTTP_NOT_FOUND) }
        }
        val context = GetUsersScope(context = MockContext(), view = view, runner = singleThreadRunner, apiClient = apiClient)

        // when
        getUsers().run(context)

        // given
        verify(view).showNotFoundError()
    }

    @Test
    fun getUsers_genericError()
    {
        // given
        val view = mock<UsersView> {}
        val apiClient = mock<ApiClientImpl> {
            on { fetchUsers() } doAnswer { throw ApiError(httpCode = HttpURLConnection.HTTP_BAD_REQUEST) }
        }
        val context = GetUsersScope(context = MockContext(), view = view, runner = singleThreadRunner, apiClient = apiClient)

        // when
        getUsers().run(context)

        // given
        verify(view).showGenericError()
    }

    /**
     * Helpers
     */
    private fun createUsers(count: Int = 10): List<UserEntity> =
            (0..count).map { UserEntity(id = it.toLong(), name = "name$it", username = "username$it", email = "email@$it") }
}
