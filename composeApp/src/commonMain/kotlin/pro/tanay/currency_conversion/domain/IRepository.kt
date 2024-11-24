package pro.tanay.currency_conversion.domain

interface IRepository {
    fun localRepository(): ILocalRepository
    fun remoteRepository(): IRemoteRepository
}