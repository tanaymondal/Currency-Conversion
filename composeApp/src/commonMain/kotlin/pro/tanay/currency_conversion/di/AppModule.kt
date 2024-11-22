package pro.tanay.currency_conversion.di

import org.koin.dsl.module
import pro.tanay.currency_conversion.data.local.PreferenceRepositoryImpl
import pro.tanay.currency_conversion.data.remote.api.CurrencyApiRepositoryImpl
import pro.tanay.currency_conversion.domain.ICurrencyApiRepository
import pro.tanay.currency_conversion.domain.IPreferenceRepository

val appModule = module {
    single<IPreferenceRepository> { PreferenceRepositoryImpl(get()) }
    single<ICurrencyApiRepository> { CurrencyApiRepositoryImpl(get()) }
}