object IDGenerator {
    private var idCounter = 0
    private var visualIdCounter = 0
    private var animationIdCounter = 0

    fun generateID(): String = "bgw-id-${idCounter++}"
    fun generateVisualID(): String = "bgw-vis-${visualIdCounter++}"

    fun generateAnimationID(): String = "bgw-anim-${animationIdCounter++}"
}