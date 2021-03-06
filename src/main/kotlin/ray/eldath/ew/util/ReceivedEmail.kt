package ray.eldath.ew.util

import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.email.Recipient
import ray.eldath.ew.core.Sender
import ray.eldath.ew.tool.Config
import ray.eldath.ew.tool.Constants
import java.time.LocalDate

class ReceivedEmail(val id: Int, val subject: String, val from: Collection<Recipient>, val size: Int,
                    val sentDate: LocalDate, val seen: Boolean, val content: String) {
	override fun toString(): String =
			"SUBJECT: \"$subject\"\nFROM: $from\nSIZE: $size\nSENT DATE: $sentDate\nSEEN: $seen\nCONTENT: $content"

	fun reply(sender: Sender, subject: String, content: String) =
			sender.send(EmailBuilder()
					.to(*from.toTypedArray())
					.from(Config.EMAIL_HOST_CONFIG.name, Config.EMAIL_HOST_CONFIG.address)
					.subject("${Constants.REPLY_SUBJECT_PREFIX} $subject")
					.text(content).build())

	fun copy(subjectN: String) = ReceivedEmail(id, subjectN, from, size, sentDate, seen, content)
}

class ReceivedEmailSet(val allMessageCount: Int, val unreadMessageCount: Int, val messages: List<ReceivedEmail>)