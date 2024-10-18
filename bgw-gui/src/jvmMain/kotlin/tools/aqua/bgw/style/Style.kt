package tools.aqua.bgw.style

class Style {
    private val declarations = mutableMapOf<String, StyleDeclaration>()

    fun getDeclarations() : Map<String, String> {
        return declarations.mapValues { it.value.toCSS() }
    }

    var borderRadius : BorderRadius = BorderRadius.NONE
        set(value) {
            field = value
            declarations["border-radius"] = value
        }
        get() = declarations["border-radius"] as BorderRadius
}

interface StyleDeclaration {
    fun toCSS(): String
}

class BorderRadius : StyleDeclaration {
    var topLeft: String = "0em"
    var topRight: String = "0em"
    var bottomRight: String = "0em"
    var bottomLeft: String = "0em"

    constructor(radius: Int) {
        topLeft = "${radius}em"
        topRight = "${radius}em"
        bottomRight = "${radius}em"
        bottomLeft = "${radius}em"
    }

    constructor(topLeft: Int, topRight: Int, bottomRight: Int, bottomLeft: Int) {
        this.topLeft = "${topLeft}em"
        this.topRight = "${topRight}em"
        this.bottomRight = "${bottomRight}em"
        this.bottomLeft = "${bottomLeft}em"
    }

    constructor(radius: String) : this(radius, radius, radius, radius)

    constructor(topLeft: String, topRight: String, bottomRight: String, bottomLeft: String) {
        this.topLeft = topLeft
        this.topRight = topRight
        this.bottomRight = bottomRight
        this.bottomLeft = bottomLeft
    }

    override fun toCSS(): String {
        if(topLeft == topRight && topLeft == bottomRight && topLeft == bottomLeft) {
            return topLeft
        }

        return "$topLeft $topRight $bottomRight $bottomLeft"
    }

    companion object {
        val NONE = BorderRadius(0)
        val XS = BorderRadius(2)
        val SMALL = BorderRadius(4)
        val MEDIUM = BorderRadius(8)
        val LARGE = BorderRadius(16)
        val XL = BorderRadius(32)
        val XXL = BorderRadius(64)
        val XXXL = BorderRadius(128)
        val FULL = BorderRadius("50%")
    }
}

class BackgroundRadius : StyleDeclaration {
    override fun toCSS(): String {
        TODO("Not yet implemented")
    }
}

class BorderWidth : StyleDeclaration {
    override fun toCSS(): String {
        TODO("Not yet implemented")
    }
}

class BorderColor : StyleDeclaration {
    override fun toCSS(): String {
        TODO("Not yet implemented")
    }
}

class Cursor : StyleDeclaration {
    override fun toCSS(): String {
        TODO("Not yet implemented")
    }
}

class BorderStyle : StyleDeclaration {
    override fun toCSS(): String {
        TODO("Not yet implemented")
    }
}