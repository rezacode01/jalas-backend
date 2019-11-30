package ir.seven.jalas.exceptions

import ir.seven.jalas.enums.ErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class InternalServerError(message: String) : RuntimeException(message) {
    constructor(type: ErrorMessage) : this(type.toString())
}