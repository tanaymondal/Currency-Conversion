package pro.tanay.currency_conversion

import android.app.Application
import pro.tanay.currency_conversion.di.KoinInitializer

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinInitializer(context = applicationContext).init()
    }
}