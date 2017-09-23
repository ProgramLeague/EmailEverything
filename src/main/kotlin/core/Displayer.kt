package core

import org.simplejavamail.email.Email
import org.simplejavamail.email.Recipient
import util.ReceivedEmail
import java.io.PrintStream

object Displayer {
	fun displayEmail(printStream: PrintStream, email: Email) {
		printStream.println("Email No.${email.id}")
		printStream.println("SUBJECT: ${email.subject}")
		printStream.print("TO: ${parseRecipients(email.recipients)}>")
		printStream.println("CONTENT: ${email.text}\n")
	}

	fun displayReceivedEmail(printStream: PrintStream, receivedEmail: ReceivedEmail) {
		printStream.println("Email No.${receivedEmail.id}")
		printStream.println("SUBJECT: ${receivedEmail.subject}")
		printStream.print("FROM: ${parseRecipients(receivedEmail.from)}")
		printStream.println("SENT DATE: ${receivedEmail.sentDate}")
		printStream.println("CONTENT: ${receivedEmail.content}\n")
	}

	private fun parseRecipients(recipient: Collection<Recipient>): String {
		val builder = StringBuilder()
		for (thisRecipient in recipient)
			builder.append(thisRecipient.name).append(" <").append(thisRecipient.address).append(">\n")
		return builder.toString()
	}
}