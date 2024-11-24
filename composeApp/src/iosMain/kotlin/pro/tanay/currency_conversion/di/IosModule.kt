package pro.tanay.currency_conversion.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import pro.tanay.currency_conversion.database.createCurrencyDatabase
import pro.tanay.currency_conversion.datastore.createDataStore
import pro.tanay.currency_conversion.viewmodel.CurrencyViewModel

actual val preferenceModule: Module = module {
    single { createDataStore() }
}

actual val viewModelModule = module {
    singleOf(::CurrencyViewModel)
}

actual val databaseModule: Module = module {
    single { createCurrencyDatabase() }
}