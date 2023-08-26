package tools.aqua.bgw.style

class Filter {
    private val declarations = mutableMapOf<String, FilterDeclaration>()

    fun getDeclarations() : Map<String, String?> {
        return declarations.mapValues {
            it.value.toFilter()
        }
    }

    var blur : BlurFilter = BlurFilter.NONE
        set(value) {
            field = value
            declarations["blur"] = value
        }
        get() = declarations["blur"] as BlurFilter

    var saturation : SaturationFilter = SaturationFilter.DEFAULT
        set(value) {
            field = value
            declarations["saturation"] = value
        }
        get() = declarations["saturation"] as SaturationFilter
}

interface FilterDeclaration {
    var value: String?
    fun toFilter(): String?
}

class BlurFilter(radius: Double) : FilterDeclaration {
    override var value: String? = ""

    init {
        value = if (radius > 0.0)
            "${radius}rem"
        else
            null
    }

    override fun toFilter(): String? {
        return if(value != null) "blur($value)" else null
    }

    companion object {
        val NONE = BlurFilter(0.0)
        val SMALL = BlurFilter(4.0)
        val MEDIUM = BlurFilter(8.0)
        val LARGE = BlurFilter(16.0)
    }
}

class SaturationFilter(saturation: Double) : FilterDeclaration {
    override var value: String? = ""

    init {
        value = if (saturation != 1.0)
            "$saturation"
        else
            null
    }

    override fun toFilter(): String? {
        return if(value != null) "saturate($value)" else null
    }

    companion object {
        val DEFAULT = SaturationFilter(1.0)
        val BLACK_WHITE = SaturationFilter(0.0)
    }
}

