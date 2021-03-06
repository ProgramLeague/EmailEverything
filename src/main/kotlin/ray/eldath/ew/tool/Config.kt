package ray.eldath.ew.tool

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import org.simplejavamail.mailer.config.TransportStrategy
import ray.eldath.ew.tool.DataFaker.fakeReceivedEmail
import ray.eldath.ew.tool.DataFaker.fakeReceivedEmailSet
import ray.eldath.ew.util.EmailAddressConfig
import ray.eldath.ew.util.EmailHost
import ray.eldath.ew.util.EmailHostProtocol
import java.nio.file.Files
import java.nio.file.Paths

fun <T> List<T>.addSingle(entry: T): List<T> {
	val list = this.toMutableList()
	list.add(entry)
	return list
}

object Config {
	val CURRENT_PATH = System.getProperty("user.dir")!!

	private val OBJECT = JSONTokener(Files.newBufferedReader(Paths.get("$CURRENT_PATH/config.json"))).nextValue() as JSONObject
	private val HANDLER_CONFIG = OBJECT.getJSONObject("handler")!!

	val EMAIL_HOST_CONFIG = parseEmailHostConfig()
	val PERMITTED_ADDRESSES = parseStringList(OBJECT.getJSONArray("permitted_addresses")).addSingle("debug@debug.com")
	val DAEMON = OBJECT.getBoolean("daemon")
	val DEBUG = OBJECT.getBoolean("debug")
	val DEBUG_RECEIVED_MAIL_SET =
			fakeReceivedEmailSet(listOf(
					fakeReceivedEmail("heartbeat & ip"),
					fakeReceivedEmail("wake Ray Eldath's Desktop")
			))

	fun get(key: String): Any = OBJECT.get(key)

	fun getInteger(key: String): Int = OBJECT.getInt(key)

	fun getString(key: String): String = OBJECT.getString(key)

	fun getJSONObject(key: String): JSONObject = OBJECT.getJSONObject(key)

	fun getHandlerConfig(handler: String) = HANDLER_CONFIG.getJSONObject(handler)!!

	private fun parseEmailHostConfig(): EmailAddressConfig {
		val host = OBJECT.getJSONObject("email")
		return EmailAddressConfig(
				host.getString("name"),
				host.getString("address"),
				host.getString("password"),
				EmailHost(
						let {
							val jsonObject = host.getJSONObject("sender")
							EmailHost.Sender(
									jsonObject.getString("host"),
									jsonObject.getInt("port"),
									EmailHostProtocol.valueOf(jsonObject.getString("protocol").toUpperCase()),
									TransportStrategy.valueOf(jsonObject.getString("transport_strategy").toUpperCase()))
						},
						let {
							val jsonObject = host.getJSONObject("receiver")
							EmailHost.Receiver(
									jsonObject.getString("host"),
									jsonObject.getInt("port"),
									EmailHostProtocol.valueOf(jsonObject.getString("protocol").toUpperCase()),
									jsonObject.getBoolean("ssl"))
						})
		)
	}

	private fun parseStringList(jsonArray: JSONArray): List<String> {
		val list = ArrayList<String>()
		(0 until jsonArray.length()).mapTo(list) { jsonArray.getString(it) }
		return list
	}
}