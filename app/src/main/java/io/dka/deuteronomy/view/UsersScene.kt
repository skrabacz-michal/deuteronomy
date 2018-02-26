package io.dka.deuteronomy.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapter.Type
import io.dka.deuteronomy.BR
import io.dka.deuteronomy.R
import io.dka.deuteronomy.databinding.ItemUserBinding
import io.dka.deuteronomy.ioc.GetUsersScope
import io.dka.deuteronomy.presentation.UsersView
import io.dka.deuteronomy.presentation.getUsers
import io.dka.deuteronomy.presentation.onUserItemClick
import io.dka.deuteronomy.view.model.UserViewModel
import kotlinx.android.synthetic.main.activity_users.*

class UsersScene : AppCompatActivity(), UsersView
{
    lateinit var getUsersContext: GetUsersScope

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        usersList.setHasFixedSize(true)
        usersList.layoutManager = LinearLayoutManager(this)

        getUsersContext = GetUsersScope(context = this, view = this)
    }

    override fun onResume()
    {
        super.onResume()
        getUsers().run(getUsersContext)
    }

    /**
     * Presentation
     */
    override fun drawUsers(users: List<UserViewModel>) = runOnUiThread {
        LastAdapter(users, BR.user)
                .map<UserViewModel>(Type<ItemUserBinding>(R.layout.item_user)
                        .onClick {
                            it.binding.user?.id?.let { userId ->
                                onUserItemClick(userId).run(getUsersContext)
                            }
                        }
                )
                .into(usersList)
    }

    override fun showNotFoundError()
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showGenericError()
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}