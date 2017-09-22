package mail

import org.simplejavamail.email.Email
import org.simplejavamail.mailer.config.TransportStrategy
import util.ReceivedEmail
import util.ReceivedEmailSet
import java.io.Closeable

interface ReceiveEmail : Closeable {
	fun receive(): ReceivedEmailSet

	fun receive(ssl: Boolean): ReceivedEmailSet

	fun delete(receivedEmail: ReceivedEmail)

	override fun close()
}

interface SendEmail {
	fun send(email: Email)

	fun send(email: Email, transportStrategy: TransportStrategy)
}