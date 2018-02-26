package io.dka.deuteronomy.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.dka.deuteronomy.R
import io.dka.deuteronomy.ioc.GetUserDetailsScope
import io.dka.deuteronomy.presentation.UserDetailsView
import io.dka.deuteronomy.presentation.getUserDetails
import io.dka.deuteronomy.view.model.UserViewModel
import kotlinx.android.synthetic.main.activity_user_details.*

class UserDetailsScene : AppCompatActivity(), UserDetailsView
{
    companion object
    {
        private const val USER_ID = "user_id"

        fun launch(parent: Context, userId: Long) =
                parent.startActivity(Intent(parent, UserDetailsScene::class.java).apply {
                    putExtra(USER_ID, userId)
                })
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
    }

    override fun onResume()
    {
        super.onResume()
        intent.extras?.getLong(USER_ID)?.let { userId ->
            getUserDetails(userId).run(GetUserDetailsScope(context = this, view = this))
        } ?: closeWithError()
    }

    /**
     * Presentation
     */
    override fun drawUser(user: UserViewModel) = runOnUiThread {
        userName.text = user.name
    }

    override fun showNotFoundError()
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showGenericError()
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun closeWithError()
    {
        onBackPressed()
    }
}