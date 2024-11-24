package pro.tanay.currency_conversion.domain

interface ILocalRepository {
    fun preference(): IPreferenceRepository
    fun database(): IDatabaseRepository
}