package eventer.project.app.errorhandler

class AuthorizationException(message: String?) : RuntimeException(message)

class BadRequestException(message: String?) : RuntimeException(message)

class NotFoundException(message: String?) : RuntimeException(message)

class SomethingWentWrongException(message: String?) : RuntimeException(message)

class MissingRequestBodyException(message: String?) : RuntimeException(message)