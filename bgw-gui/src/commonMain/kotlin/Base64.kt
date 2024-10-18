
object Base64 {
    private fun encode(bytes: ByteArray): String {
        return bytes.joinToString("") { it.toUByte().toString(16).padStart(2, '0') }
    }
    private fun decode(string: String): ByteArray {
        return string.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }

    fun encode(string: String): String {
        return encode(string.encodeToByteArray())
    }
    fun decode(string: String, charset: String = "UTF-8"): String {
        return decode(string).decodeToString()
    }
}