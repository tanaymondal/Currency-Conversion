package pro.tanay.currency_conversion.di

import org.koin.dsl.module
import pro.tanay.currency_conversion.data.RepositoryImpl
import pro.tanay.currency_conversion.data.local.LocalRepositoryImpl
import pro.tanay.currency_conversion.data.local.database.DatabaseRepositoryImpl
import pro.tanay.currency_conversion.data.local.preference.PreferenceRepositoryImpl
import pro.tanay.currency_conversion.data.remote.RemoteRepositoryImpl
import pro.tanay.currency_conversion.domain.IDatabaseRepository
import pro.tanay.currency_conversion.domain.ILocalRepository
import pro.tanay.currency_conversion.domain.IPreferenceRepository
import pro.tanay.currency_conversion.domain.IRemoteRepository
import pro.tanay.currency_conversion.domain.IRepository
import pro.tanay.currency_conversion.ext.CoroutineDispatcherProvider

val appModule = module {

    single<IPreferenceRepository> { PreferenceRepositoryImpl(get()) }
    single<IDatabaseRepository> { DatabaseRepositoryImpl(get()) }

    single<ILocalRepository> { LocalRepositoryImpl(get(), get()) }
    single<IRemoteRepository> { RemoteRepositoryImpl(get()) }

    single<IRepository> { RepositoryImpl(get(), get()) }

    single<CoroutineDispatcherProvider> { CoroutineDispatcherProvider() }
}