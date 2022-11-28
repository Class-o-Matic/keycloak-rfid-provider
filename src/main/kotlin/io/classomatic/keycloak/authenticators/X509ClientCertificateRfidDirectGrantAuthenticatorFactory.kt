package io.classomatic.keycloak.authenticators

import org.keycloak.authentication.Authenticator
import org.keycloak.authentication.authenticators.x509.AbstractX509ClientCertificateAuthenticatorFactory
import org.keycloak.models.AuthenticationExecutionModel
import org.keycloak.models.KeycloakSession

class X509ClientCertificateRfidDirectGrantAuthenticatorFactory : AbstractX509ClientCertificateAuthenticatorFactory() {
    companion object {
        @JvmStatic
        val PROVIDER_ID = "rfid-authenticator"

        @JvmStatic
        private val SINGLETON = X509ClientCertificateRfidDirectGrantAuthenticator()

        @JvmStatic
        private val REQUIREMENT_CHOICES = arrayOf(
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.ALTERNATIVE,
            AuthenticationExecutionModel.Requirement.DISABLED
        )
    }

    override fun getId(): String = PROVIDER_ID

    override fun getHelpText(): String = "Authentication with a RFID card"

    override fun getDisplayType(): String = "RFID Authenticator with X509"

    override fun create(session: KeycloakSession?): Authenticator = SINGLETON

    override fun getRequirementChoices(): Array<AuthenticationExecutionModel.Requirement> {
        return REQUIREMENT_CHOICES
    }

    override fun isUserSetupAllowed(): Boolean {
        return false
    }


}