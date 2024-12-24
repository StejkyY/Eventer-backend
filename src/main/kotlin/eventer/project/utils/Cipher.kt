package eventer.project.utils

import com.auth0.jwt.algorithms.Algorithm


object Cipher {

    val algorithm: Algorithm = Algorithm.HMAC256("a-secret")
}