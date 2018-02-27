package io.dka.deuteronomy.presentation

import android.test.mock.MockContext
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.dka.deuteronomy.data.error.ApiError
import io.dka.deuteronomy.data.map
import io.dka.deuteronomy.data.model.UserEntity
import io.dka.deuteronomy.data.source.cloud.ApiClient
import io.dka.deuteronomy.data.source.cloud.ApiClientImpl
import io.dka.deuteronomy.ioc.GetUserDetailsScope
import io.dka.deuteronomy.presentation.runner.SingleThreadRunner
import org.junit.Test
import java.net.HttpURLConnection

class UserPresentationUnitTest
{
    private val singleThreadRunner = SingleThreadRunner()

    @Test
    fun getUser_isCorrect()
    {
        // given
        val user = createUser(10)
        val view = mock<UserDetailsView> {}
        val apiClient = mock<ApiClient> {
            on { fetchUser(user.id) } doReturn user
        }
        val context = GetUserDetailsScope(context = MockContext(), view = view, runner = singleThreadRunner, apiClient = apiClient)

        // when
        getUserDetails(user.id).run(context)

        // given
        verify(view).drawUser(user.map())
    }


    @Test
    fun getUsers_noFound()
    {
        // given
        val userId: Long = 10
        val view = mock<UserDetailsView> {}
        val apiClient = mock<ApiClientImpl> {
            on { fetchUser(userId) } doAnswer { throw ApiError(httpCode = HttpURLConnection.HTTP_NOT_FOUND) }
        }
        val context = GetUserDetailsScope(context = MockContext(), view = view, runner = singleThreadRunner, apiClient = apiClient)

        // when
        getUserDetails(userId).run(context)

        // given
        verify(view).showNotFoundError()
    }

    @Test
    fun getUsers_genericError()
    {
        // given
        val userId: Long = 10
        val view = mock<UserDetailsView> {}
        val apiClient = mock<ApiClientImpl> {
            on { fetchUser(userId) } doAnswer { throw ApiError(httpCode = HttpURLConnection.HTTP_BAD_REQUEST) }
        }
        val context = GetUserDetailsScope(context = MockContext(), view = view, runner = singleThreadRunner, apiClient = apiClient)

        // when
        getUserDetails(userId).run(context)

        // given
        verify(view).showGenericError()
    }

    /**
     * Helpers
     */
    private fun createUser(userId: Long = 1): UserEntity =
            UserEntity(id = userId, name = "name$userId", username = "username$userId", email = "email@$userId")
}
