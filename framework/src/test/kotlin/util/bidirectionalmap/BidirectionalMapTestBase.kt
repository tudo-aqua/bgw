package util.bidirectionalmap

import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.util.BidirectionalMap

open class BidirectionalMapTestBase {
	
	protected lateinit var map: BidirectionalMap<Int, Int>
	
	@BeforeEach
	fun setUp() {
		map = BidirectionalMap(
			Pair(0, 1),
			Pair(2, 3)
		)
	}
}