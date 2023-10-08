object IDGenerator {
    private var idCounter = 0
    private var visualIdCounter = 0
    fun generateID(): String = "bgw-id-${idCounter++}"
    fun generateVisualID(): String = "bgw-vis-${visualIdCounter++}"
}