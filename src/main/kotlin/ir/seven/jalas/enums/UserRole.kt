package ir.seven.jalas.enums

enum class UserRole(val role: String) {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    TRUSTED_CLIENT("ROLE_TRUSTED_CLIENT"),
    TRUSTED_SERVICE("ROLE_TRUSTED_SERVICE")
}
