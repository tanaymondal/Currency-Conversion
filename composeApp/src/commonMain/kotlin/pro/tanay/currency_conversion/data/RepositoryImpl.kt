package pro.tanay.currency_conversion.data

import pro.tanay.currency_conversion.domain.ILocalRepository
import pro.tanay.currency_conversion.domain.IRemoteRepository
import pro.tanay.currency_conversion.domain.IRepository

class RepositoryImpl(
    private val localRepository: ILocalRepository,
    private val remoteRepository: IRemoteRepository
) : IRepository {

    override fun localRepository(): ILocalRepository = localRepository

    override fun remoteRepository(): IRemoteRepository = remoteRepository
}