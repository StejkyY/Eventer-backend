package eventer.project.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.interfaces.DecodedJWT
import eventer.project.app.models.objects.User
import java.util.*

object JwtProvider {

    const val issuer = "eventer"
    const val audience = "eventer-audience"

    /**
     * Creates a JSON Web Token for a given user.
     */
    fun createJWT(user: User): String = JWT.create()
        .withIssuedAt(Date())
        .withSubject(user.email)
        .withIssuer(issuer)
        .withAudience(audience)
        .withExpiresAt(Date(System.currentTimeMillis() + 1 * 24 * 60 * 60 * 1000))
        .sign(Cipher.algorithm)
}