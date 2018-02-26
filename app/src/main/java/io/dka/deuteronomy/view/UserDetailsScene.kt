package io.dka.deuteronomy.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.dka.deuteronomy.R

class UserDetailsScene : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
    }
}