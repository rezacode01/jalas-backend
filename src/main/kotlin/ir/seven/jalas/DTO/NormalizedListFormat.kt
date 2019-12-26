package ir.seven.jalas.DTO

data class NormalizedListFormat<T> (
        val list: List<T>
)

fun <T> List<T>.toNormalizedForm(): NormalizedListFormat<T> = NormalizedListFormat(this)