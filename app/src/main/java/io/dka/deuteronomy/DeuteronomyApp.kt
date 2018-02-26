package io.dka.deuteronomy

import android.app.Application

class DeuteronomyApp : Application()
{
    override fun onCreate()
    {
        super.onCreate()
        Logger.log("Application created")
    }
}