package conti.victor.order_parser.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun badRequestExceptionHandler(e: BadRequestException): ErrorResponse {
        return ErrorResponse(
            e.message,
            HttpStatus.BAD_REQUEST
        )
    }
}

data class ErrorResponse(
    val message: String?,
    val code: HttpStatus
)