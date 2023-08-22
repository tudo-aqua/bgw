object IDGenerator {
    private var idCounter = 0
    fun generateID(): String = "bgw-id-${idCounter++}"
}