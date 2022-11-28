package io.classomatic.keycloak.authenticators

import org.keycloak.authentication.AuthenticationFlowContext
import org.keycloak.authentication.AuthenticationFlowError
import org.keycloak.authentication.authenticators.x509.AbstractX509ClientCertificateDirectGrantAuthenticator
import org.keycloak.events.Errors
import javax.ws.rs.core.Response

class X509ClientCertificateRfidDirectGrantAuthenticator : AbstractX509ClientCertificateDirectGrantAuthenticator() {

    override fun requiresUser(): Boolean = false

    init {

    }

    override fun authenticate(context: AuthenticationFlowContext?) {
        val certificates = getCertificateChain(context)

        if (certificates.isNullOrEmpty()) {
            logger.debug("[X509ClientCertificateRfidDirectGrantAuthenticator:authenticate] x509 client certificate is not available for mutual SSL")
            context?.event?.error(Errors.USER_NOT_FOUND)
            val response = errorResponse(Response.Status.UNAUTHORIZED.statusCode, "invalid_request", "X509 client certificate is missing")
            context?.failure(AuthenticationFlowError.INVALID_USER, response)
            return
        }

        saveX509CertificateAuditDataToAuthSession(context, certificates[0])
        recordX509CertificateAuditDataViaContextEvent(context)

        val rfid = retrieveRfid(context)

        val user = context?.session
            ?.users()
            ?.searchForUserByUserAttributeStream(context.realm, "rfid_number", rfid)
            ?.findFirst()
            ?.orElse(null)

        if (user == null) {
            context?.event?.error(Errors.INVALID_USER_CREDENTIALS)
            val response = errorResponse(Response.Status.UNAUTHORIZED.statusCode, "invalid_grant", "Invalid user credentials")
            context?.failure(AuthenticationFlowError.INVALID_USER, response)
            return
        }

        if (!user.isEnabled) {
            context.event.user(user)
            context.event.error(Errors.USER_DISABLED)
            val response = errorResponse(Response.Status.BAD_REQUEST.statusCode, "invalid_grant", "Account disabled")
            context.failure(AuthenticationFlowError.INVALID_USER, response)
            return
        }

        context.user = user
        context.success()
    }

    private fun retrieveRfid(context: AuthenticationFlowContext?): String? {
        val inputData = context?.httpRequest?.decodedFormParameters
        return inputData?.getFirst("rfid")
    }
}