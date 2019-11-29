package ir.seven.jalas.exceptions

import ir.seven.jalas.enums.ErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.NOT_FOUND)
class EntityDoesNotExist(message: String) : RuntimeException(message){
    constructor(type: ErrorMessage) : this(type.toString())
}