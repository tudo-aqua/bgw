[BoardGameClientKDoc]: /docs/tools.aqua.bgw.net.client/-board-game-client/index.html
[NetworkLoggingKDoc]: /docs/tools.aqua.bgw.net.client/-network-logging/index.html
[GameActionKDoc]: /docs/tools.aqua.bgw.net.common/-game-action/index.html

<!-- GH-Pages Doc -->

[JsonDoc]: /guides/network/json
[ApplicationDoc]: /guides/concepts/application
[ObservableDoc]: /guides/concepts/observable

# Network communication

<tldr>Add online multiplayer with bgw-net</tldr>

## Introduction

The **bgw-net** modules connect your game to a BGW server, so players on different computers can play with and against each other. The server only forwards messages between the players of a game session and validates them, **your game logic stays in your application**: every client sends what a player did, and every other client applies it.

| Part | Role |
| --- | --- |
| Server | Hosts game sessions, authenticates clients with a secret and forwards messages. It validates messages against JSON schemas. |
| Client | The `BoardGameClient` in your application. It connects to the server, creates or joins sessions and sends and receives messages. |
| Messages | Classes that you declare, one per kind of action in your game. They extend `GameAction`. |

---

## Prerequisites

Add the client artifacts to your build. Both are required for client applications. With the BGW Gradle plugin, enable `network` in the `bgw` block and the plugin adds both in the same version as `bgw-gui`:

<tabs group="gradleMaven">
    <tab title="Gradle" group-key="gradle">
        <code-block lang="apache">
bgw {
    network = true
}
        </code-block>
    </tab>
    <tab title="Maven" group-key="maven">
        <code-block lang="xml">
            &lt;dependency&gt;
    &lt;groupId&gt;tools.aqua&lt;/groupId&gt;
    &lt;artifactId&gt;bgw-net-common&lt;/artifactId&gt;
    &lt;version&gt;0.11.1&lt;/version&gt;
&lt;/dependency&gt;
&lt;dependency&gt;
    &lt;groupId&gt;tools.aqua&lt;/groupId&gt;
    &lt;artifactId&gt;bgw-net-client&lt;/artifactId&gt;
    &lt;version&gt;0.11.1&lt;/version&gt;
&lt;/dependency&gt;
        </code-block>
    </tab>
</tabs>

---

## Creating a client

The [BoardGameClient][BoardGameClientKDoc] is the central class on the client side. Its constructor is `protected`, so you create **your own subclass**. It is the place to react to everything that the server tells you.

```kotlin
class DiceDuelClient(
    playerName: String,
    host: String,
    secret: String
) : BoardGameClient(
    playerName = playerName,
    host = host,
    secret = secret,
    networkLoggingBehavior = NetworkLogging.INFO
)
```

<signature>
<code-block lang="kotlin" copy="false">
BoardGameClient(
    playerName: String,
    host: String,
    secret: String,
    networkLoggingBehavior: NetworkLogging = NetworkLogging.NO_LOGGING
)
</code-block>

`playerName` identifies the player and must be unique within a session. `host` is the address of the server **including port and path**, without protocol, for example `"127.0.0.1:8080/connect"`. The client connects using a WebSocket (`ws://`), and the endpoint of a BGW server is called `connect`. `secret` is the network secret that the server expects. None of them can be changed later on.

</signature>

The [NetworkLogging][NetworkLoggingKDoc] defines how verbose the client logs to the console:

<table orientation="horizontal">
    <tr>
        <td width="22%"><code>VERBOSE</code></td>
        <td>Every step including the serialized JSON.</td>
    </tr>
    <tr>
        <td><code>INFO</code></td>
        <td>Incoming and outgoing messages as one-liners.</td>
    </tr>
    <tr>
        <td><code>ERRORS</code></td>
        <td>Only errors.</td>
    </tr>
    <tr>
        <td><code>NO_LOGGING</code></td>
        <td>Nothing. This is the default.</td>
    </tr>
</table>

---

## Connecting

Open and close the connection to the server.

<signature>
<code-block lang="kotlin" copy="false">
connect(): Boolean
disconnect()
isOpen: Boolean
</code-block>

`connect()` opens the connection and **blocks** until it was established or timed out. It returns `true` on success and throws an `IllegalStateException` if the client is already connected. `disconnect()` closes the connection and `isOpen` tells the current state.

</signature>

Because `connect()` blocks, call it from a place where a short wait is acceptable. If you call it from an event handler of a component, the window freezes until the connection attempt is over.

```kotlin
val client = DiceDuelClient(
    playerName = "Alice",
    host = "127.0.0.1:8080/connect",
    secret = "SECRET"
)

if (!client.connect()) {
    println("Could not connect to the server")
}
```

<signature>
<code-block lang="kotlin" copy="false">
open fun onOpen()
open fun onClose(code: Int, reason: String, remote: Boolean)
open fun onError(throwable: Throwable)
</code-block>

Override these functions to react to the connection state. `onError` only logs by default.

</signature>

---

## Hosting and joining sessions

Players meet in **sessions**. One player creates a session and others join it by its ID.

<signature>
<code-block lang="kotlin" copy="false">
createGame(gameID: String, sessionID: String, greetingMessage: String)
createGame(gameID: String, greetingMessage: String)
</code-block>

Creates a session and adds this client to it. The `gameID` must be registered on the server, because it selects the message schemas of your game, see [JSON][JsonDoc]. The first variant picks the session ID yourself. In the second, the server creates one and returns it in the response. The `greetingMessage` is sent to everybody who joins.

<br>

<code-block lang="kotlin" copy="false">
joinGame(sessionID: String, greetingMessage: String)
spectateGame(sessionID: String, greetingMessage: String)
leaveGame(goodbyeMessage: String)
</code-block>

Joins an existing session as player or as spectator, who can see but not send messages, or leaves the current session. The messages are broadcast to the other participants.

</signature>

```kotlin
// Alice hosts
client.createGame(gameID = "DiceDuel", sessionID = "Alice vs. Bob", greetingMessage = "Welcome!")

// Bob joins
client.joinGame(sessionID = "Alice vs. Bob", greetingMessage = "Hi, I am Bob!")
```

### Responses and notifications

The server answers every request. Override the matching function in your client to react. `status` tells whether the request worked:

```kotlin
override fun onCreateGameResponse(response: CreateGameResponse) {
    when (response.status) {
        CreateGameResponseStatus.SUCCESS ->
            println("Created session ${response.sessionID}")
        CreateGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME ->
            error("Leave the current game first.")
        CreateGameResponseStatus.SESSION_WITH_ID_ALREADY_EXISTS ->
            error("This session ID is taken.")
        CreateGameResponseStatus.GAME_ID_DOES_NOT_EXIST ->
            error("The server does not know this game.")
        CreateGameResponseStatus.SERVER_ERROR ->
            error("Something went wrong on the server.")
    }
}

override fun onJoinGameResponse(response: JoinGameResponse) {
    when (response.status) {
        JoinGameResponseStatus.SUCCESS -> {
            println("Joined. Opponents: ${response.opponents}")
            println("Greeting: ${response.message}")
        }
        JoinGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME ->
            error("You are already in a game.")
        JoinGameResponseStatus.INVALID_SESSION_ID ->
            error("This session does not exist.")
        JoinGameResponseStatus.PLAYER_NAME_ALREADY_TAKEN ->
            error("Choose another player name.")
        JoinGameResponseStatus.SERVER_ERROR ->
            error("Something went wrong on the server.")
    }
}
```

| Function | Invoked when |
| --- | --- |
| `onCreateGameResponse` | The server answered `createGame`. |
| `onJoinGameResponse`, `onSpectatorJoinGameResponse` | The server answered `joinGame` or `spectateGame`. |
| `onLeaveGameResponse` | The server answered `leaveGame`. |
| `onGameActionResponse` | The server answered a sent message. `status` is e.g. `INVALID_JSON` if the message did not match its schema. |
| `onPlayerJoined`, `onSpectatorJoined`, `onPlayerLeft` | Somebody else joined or left the session. The `notification` contains the `sender` and the greeting or goodbye `message`. |

> **All callbacks are invoked asynchronously**, not in the context of your user interface. Changes to components must therefore be wrapped in `BoardGameApplication.runOnGUIThread { ... }`, see [Application][ApplicationDoc].
> {style="warning"}

---

## Sending messages

The server does not know the rules of your game, and it does not keep track of whose turn it is. It forwards every message to all other participants of the session, and you decide what a message means.

For each kind of message, declare a class that extends [GameAction][GameActionKDoc] and is annotated with `@GameActionClass`. A `data class` is ideal, because the fields are serialized as JSON and `toString()` is generated. Dice Duel needs two messages:

```kotlin
@GameActionClass
data class RollGameAction(
    val playerSide: Int,
    val opponentSide: Int
) : GameAction() {
    override fun formatMessage(): String? = null
}

@GameActionClass
data class GameOverGameAction(
    val winner: String
) : GameAction() {
    override fun formatMessage(): String = "$winner has won the game!"
}
```

`formatMessage()` returns a human readable description for logs and debugging, or `null` to fall back to `toString()`. Send a message with `sendGameActionMessage`:

```kotlin
client.sendGameActionMessage(RollGameAction(playerSide = 5, opponentSide = 2))
```

The server answers each message in `onGameActionResponse`.

> A spectator can not send messages. The response status of such an attempt is `SPECTATOR_ONLY`.
> {style="note"}

---

## Receiving messages

For every incoming message, BGW looks for a function in your client that takes the matching `GameAction` class. Annotate such functions with `@GameActionReceiver`. They take two parameters: the message and the name of the sending player.

```kotlin
class DiceDuelClient(/* ... */) : BoardGameClient(/* ... */) {

    @GameActionReceiver
    private fun onRoll(message: RollGameAction, sender: String) {
        BoardGameApplication.runOnGUIThread {
            // apply the roll to the game scene
        }
    }

    @GameActionReceiver
    private fun onGameOver(message: GameOverGameAction, sender: String) {
        BoardGameApplication.runOnGUIThread {
            // show the winner
        }
    }
}
```

The names of the functions and parameters as well as their visibility are up to you. Declare **exactly one** receiver per class: if two functions accept the same class, only one of them is used, and which one is not stable.

If no dedicated receiver exists for a message, it ends up in the fallback function `onGameActionReceived(message: GameAction, sender: String)`, which you can override:

```kotlin
override fun onGameActionReceived(message: GameAction, sender: String) {
    println("Unhandled message from $sender: ${message.formatMessageSafe()}")
}
```

Messages are delivered **in the order they were received**, even though their receivers are resolved asynchronously.

> All players must declare the **exact same** message classes, with the same package and name, so that the messages can be serialized and read by every client. Moreover, every message class needs a matching [JSON schema][JsonDoc] on the server.
> {style="warning"}
