package tools.aqua.bgw.examples.maumau.main

import kotlinx.coroutines.runBlocking
import tools.aqua.bgw.examples.maumau.view.MauMauViewController
import tools.aqua.bgw.net.client.BoardGameClient

fun main() {
	val client = BoardGameClient()
	runBlocking {
		MauMauViewController(client.startGame("127.0.0.1", 8080))
	}
	client.close()
}