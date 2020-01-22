package ir.seven.jalas.globals

class SecurityConfigs {
    companion object {
        const val JWT_SIGNING_KEY = "W8F2DACHAS9"

        const val TRUSTED_CLIENT_ID = "dashboard"
        const val PANEL_API_RESOURCE_ID = "panel-api"
        const val TRUSTED_CLIENT_SECRET = "YI75MdABASE2RR"

        const val TRUSTED_CLIENT_ACCESS_TOKEN_VALIDITY_SECONDS = 24 * 60 * 60
        const val TRUSTED_CLIENT_REFRESH_TOKEN_VALIDITY_SECONDS = 7 * 24 * 60 * 60

    }
}