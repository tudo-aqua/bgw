---
title: Network
nav_order: 7
---

[BoardGameClientKDoc]: ../../bgw-net-client-kdoc/bgw-net-client/tools.aqua.bgw.net.client/-board-game-client/index.html
[NetworkLoggingKDoc]: ../../bgw-net-client-kdoc/bgw-net-client/tools.aqua.bgw.net.client/-network-logging/index.html
<!-- Start Page -->

# Network communication

{: .no_toc}

<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

This section deals with the integration of network communication to establish multiplayer modes over the internet.

## Prerequisites

In order to use network integration you first have to integrate bgw-net components into your buildscript, e.g. gradle
etc. For client use both artifacts _bgw-net-common_ and _bgw-net-client_ are required. You can obtain the dependencies
from maven central:

### Gradle Kotlin DSL

````kotlin
implementation("tools.aqua:bgw-net-common:latest")
implementation("tools.aqua:bgw-net-client:latest")
````

### Gradle Groovy DSL

````groovy
implementation 'tools.aqua:bgw-net-common:latest'
implementation 'tools.aqua:bgw-net-client:latest'
````

### Maven

````xml
<dependency>
	<groupId>tools.aqua</groupId>
	<artifactId>bgw-net-common</artifactId>
	<version>latest</version>
</dependency>
````

````xml
<dependency>
	<groupId>tools.aqua</groupId>
	<artifactId>bgw-net-client</artifactId>
	<version>latest</version>
</dependency>
````

## Getting started

On the client side, the central communication interface is the [BoardGameClient][BoardGameClientKDoc]. Create your own
communication class inheriting from this class.

````kotlin
class MauMauBoardGameClient(
	playerName: String,
	host: String,
	secret: String,
) : BoardGameClient(
	playerName = playerName,
	host = host,
	secret = secret,
	networkLoggingBehavior = NetworkLogging.VERBOSE)
````

Upon creation the server address, port and secret has to be passed and cannot be changed later on. Additionally,
[BoardGameClient][BoardGameClientKDoc] takes the player's name for identification purposes. 
[BoardGameClient][BoardGameClientKDoc] implements dedicated network logging to the standard console which can be turned
on and controlled via the ``netwokLoggingBehaviour`` parameter. The following options are available (see 
[NetworkLogging][NetworkLoggingKDoc]): 
 * VERBOSE: Verbose logging printing each step to the console including serialized Json.
 * INFO: Log incoming and outgoing messages in human-readable format to keep track of network traffic.
 * ERRORS: Only log errors during communication.
 * NO_LOGGING: Completely deactivate logging.

*The default value is NO_LOGGING.*

## Establishing a connection

Once set up you may call ``connect`` on the [BoardGameClient][BoardGameClientKDoc] in order to start a connection. This
method will block the current thread until a connection was established or the request timed out. You may want to think
about multithreading in this situation. The function will return ``true`` iff the connection was established 
successfully.

A connection may be closed safely by calling ``disconnnect``.

The connection state may be checked via ``isOpen`` property.

## Hosting and Joining game sessions.

A game session may be started by calling ``createGame``. Each session gets identified by a unique ``sessionID`` that
has to be passed to the function. Calling ``createGame`` automatically adds this client to the newly created game on the
server side. Additionally, the gameId must match the registered gameId on the server in order to identify the
correct schema set for this game.

````kotlin
val client = MauMauBoardGameClient(playerName = "Alice", host = "localhost",  secret = "SECRET"	)

if(client.connect())
  client.createGame(gameID = "MauMau", sessionID = "Alice vs. Bob")
````

To join an existing session, you may call ``joinGame`` passing the sessionID to join to.

````kotlin
val client = MauMauBoardGameClient(playerName = "Bob", host = "localhost",  secret = "SECRET"	)

if(client.connect())
  client.joinGame(sessionID = "Alice vs. Bob")
````

After creating or joining to a game, the server will respond with dedicated Responses which will invoke
``onCreateGameResponse`` and ``onJoinGameResponse``. Override these methods to react to the different status codes:

````kotlin
override fun onCreateGameResponse(response: CreateGameResponse) {
	when (response.status) {
		CreateGameResponseStatus.SUCCESS ->
			prinln("Successfully created game.")

		CreateGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME ->
			error("Leave current game first.")

		CreateGameResponseStatus.SESSION_WITH_ID_ALREADY_EXISTS ->
			error("Session id already exists.")

		CreateGameResponseStatus.GAME_ID_DOES_NOT_EXIST ->
			error("GameID does not exist.")

		CreateGameResponseStatus.SERVER_ERROR ->
			error(response)
	}
}
````

````kotlin
override fun onJoinGameResponse(response: JoinGameResponse) {
	when (response.status) {
		JoinGameResponseStatus.SUCCESS ->
			prinln("Successfully joined game.")

		JoinGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME ->
			error("You are already in a game.")

		JoinGameResponseStatus.INVALID_SESSION_ID ->
			error("SessionID invalid.")

		JoinGameResponseStatus.PLAYER_NAME_ALREADY_TAKEN ->
			error("Player name already exists.")

		JoinGameResponseStatus.SERVER_ERROR ->
			error(response)
	}
}
````
All connected players will get notified when a player joins or leaves the session via ``onPlayerJoined`` and 
``onPlayerLeft``.

**Please note** that all response functions and event handlers get called asynchronously. It is therefore necessary to 
call ``BoardGameApplication.runOnGUIThread{ ... }`` to resync to the GUI thread when performing view changes.

## Sending messages

Once registered in a game session you may now send messages to your opponents. The function ``sendGameActionMessage``
takes a ``GameAction`` instance and sends it to all connected opponents. For each type of game action that you want to
communicate you have to declare a separate (data) class inheriting from ``GameAction``. For our MauMau example let's
assume that there are three types of game actions:
* _MauMauInitGameAction_ - First initialization containing the card stacks and hand cards.
* _MauMauPlayCardAction_ - You played a card, and it's the next players turn.
* _MauMauEndGameAction_ - You played your last card and won the game.

As mentioned all game action classes have to inherit from ``GameAction``. In addition to that they have to be annotated 
with ``@GameActionClass``. For our three sample classes this looks like this:

````kotlin
@GameActionClass
data class MauMauInitGameAction(
    val hostCards: List<String>,
    val yourCards: List<String>,
    val drawStack: List<String>,
    val gameStack: String,
) : GameAction()
````
````kotlin
@GameActionClass
data class MauMauPlayCardAction(
    val card: String,
) : GameAction()
````
````kotlin
@GameActionClass
data class MauMauEndGameAction() : GameAction()
````

After sending a game action, the server will respond in ``onGameactionResponse``.

## Receiving messages

Received messages get propagated through ``onGameActionReceived`` by default. Consider this method as a fallback 
solution as it gets a parameter of type ``GameAction``. It's therefore necessary to cast the object down by 
instanceof-switching. Instead, bgw-net allows you to declare dedicated functions for each object type that may get
received. These functions have to be declared inside your ``BoardGameClient`` implementation or further down the 
inheritance hierarchy. The function must declare two formal parameters

* A ``GameAction`` instance
* A String for the sending player's identification

Additionally, each receiver function must be annotated with ``@GameActionReceiver``. For our example these functions may
look as follows:

````kotlin
@GameActionReceiver
private fun onInitGameReceived(message: MauMauInitGameAction, sender: String) {
	//Init game
}

@GameActionReceiver
private fun onPlayCardReceived(message: MauMauPlayCardAction, sender: String) {
	//Process played card 
}

@GameActionReceiver
private fun onEndGameReceived(message: MauMauEndGameAction, sender: String) {
	//End game
}
````

The name of the function and parameters are free to choose as well as the visibility modifier. Although when two 
functions with the same parameter types get detected, only the first will be used, and you will get a warning printed to
console. **Note** that the order of scanning and therefor the distinction which of these redundant declarations will be
used is not stable and may vary between execution cycles. It is therefore highly recommended, to declare exactly one 
receiver function for each ``GameAction`` instance.

**Please note** that all connected players have to declare the exact same classes  in order to ensure correct 
serialization and deserialization.