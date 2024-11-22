package pro.tanay.currency_conversion.domain.model

sealed class RequestState {
    data object Loading : RequestState()
    data class Success(val data: ApiResponse) : RequestState()
    data class Error(val message: String) : RequestState()
}