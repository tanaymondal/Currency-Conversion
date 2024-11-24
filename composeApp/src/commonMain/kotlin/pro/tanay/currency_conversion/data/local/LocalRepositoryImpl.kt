package pro.tanay.currency_conversion.data.local

import pro.tanay.currency_conversion.domain.IDatabaseRepository
import pro.tanay.currency_conversion.domain.ILocalRepository
import pro.tanay.currency_conversion.domain.IPreferenceRepository

class LocalRepositoryImpl(
    private val preferenceRepository: IPreferenceRepository,
    private val databaseRepository: IDatabaseRepository
) : ILocalRepository {
    override fun preference(): IPreferenceRepository = preferenceRepository

    override fun database(): IDatabaseRepository = databaseRepository
}