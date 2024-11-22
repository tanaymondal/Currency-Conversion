package pro.tanay.currency_conversion.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import pro.tanay.currency_conversion.createDataStore

actual val preferenceModule: Module = module {
    single { createDataStore() }
}

actual val viewModelModule = module {
    singleOf(::MainViewModel)
}