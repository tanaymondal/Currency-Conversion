package pro.tanay.currency_conversion.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module
import pro.tanay.currency_conversion.database.createCurrencyDatabase
import pro.tanay.currency_conversion.datastore.createDataStore
import pro.tanay.currency_conversion.viewmodel.MainViewModel

actual val preferenceModule: Module = module {
    single { createDataStore(androidContext()) }
}

actual val viewModelModule = module {
    viewModelOf(::MainViewModel)
}

actual val databaseModule: Module = module {
    single { createCurrencyDatabase(androidContext()) }
}