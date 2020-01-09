package ir.seven.jalas.dto

data class NormalizedListFormat<T> (
        val list: List<T>
)

fun <T> List<T>.toNormalizedForm(): NormalizedListFormat<T> = NormalizedListFormat(this)