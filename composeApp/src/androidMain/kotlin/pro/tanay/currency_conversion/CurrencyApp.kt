package pro.tanay.currency_conversion

import android.app.Application
import pro.tanay.currency_conversion.di.KoinInitializer

class CurrencyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinInitializer(context = applicationContext).init()
    }
}