package pro.tanay.currency_conversion

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform