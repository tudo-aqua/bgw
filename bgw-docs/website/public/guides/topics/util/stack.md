[StackKDoc]: /docs/tools.aqua.bgw.util/-stack/index.html
[CardStackKDoc]: /docs/tools.aqua.bgw.components.container/-card-stack/index.html
[NoSuchElementExceptionKDoc]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-no-such-element-exception/
[IllegalArgumentExceptionKDoc]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/

<!-- GH-Pages Doc -->

[ContainerDoc]: /guides/components/container

# Stack

<tldr>A last in, first out collection for your game model</tldr>

## Introduction

The [Stack][StackKDoc] is a Kotlin implementation of the well known LIFO collection (_last in, first out_): the element that was added last is the first one that is taken. Many board game elements behave exactly like this: a draw pile, a discard pile, a stack of coins.

> The `Stack` is a plain data structure for your **game model**. It has nothing to do with the display of components. To show a pile of cards on the board, use a [CardStack][CardStackKDoc] container, see [Container][ContainerDoc].
> {style="note"}

---

## Creating a stack

A stack is created empty, from a collection or from vararg elements. When created from existing elements, the **last** element ends up on top:

```kotlin
val emptyStack: Stack<String> = Stack()
val collectionStack: Stack<String> = Stack(listOf("ONE", "SIX", "TEN"))
val varargsStack: Stack<String> = Stack("ONE", "SIX", "TEN")

varargsStack.peek() // "TEN"
```

---

## Adding elements

Put elements on top of the stack.

<signature>
<code-block lang="kotlin" copy="false">
push(element: T)
pushAll(elements: Collection&lt;T&gt;)
pushAll(vararg elements: T)
</code-block>

Puts one element or several elements on top of the stack. Multiple elements are pushed one by one from the first to the last, so the **last** one ends up on top.

</signature>

---

## Taking elements

Remove elements from the top.

<signature>
<code-block lang="kotlin" copy="false">
pop(): T
popOrNull(): T?
</code-block>

Removes the topmost element and returns it. `pop()` throws a [NoSuchElementException][NoSuchElementExceptionKDoc] if the stack is empty, `popOrNull()` returns `null` instead.

<br>

<code-block lang="kotlin" copy="false">
popAll(numToPop: Int = size): List&lt;T&gt;
</code-block>

Pops the `numToPop` topmost elements, by default all of them. The returned list starts with the element that was on top, so the last popped element is at the highest index. Throws an [IllegalArgumentException][IllegalArgumentExceptionKDoc] if `numToPop` is negative or larger than the size of the stack.

<br>

<code-block lang="kotlin" copy="false">
clear(): List&lt;T&gt;
</code-block>

Removes **all** elements and returns them with the former top element first.

</signature>

---

## Looking at elements

Inspect the stack without changing it.

<signature>
<code-block lang="kotlin" copy="false">
peek(): T
peekOrNull(): T?
peekAll(numToPeek: Int = size): List&lt;T&gt;
</code-block>

Like `pop`, but the elements stay on the stack. `peek()` throws a [NoSuchElementException][NoSuchElementExceptionKDoc] for an empty stack, `peekOrNull()` returns `null`. `peekAll` returns the topmost elements as a list, beginning with the top element.

<br>

<code-block lang="kotlin" copy="false">
size: Int
isEmpty(): Boolean
isNotEmpty(): Boolean
indexOf(element: T): Int
</code-block>

The number of elements and whether there are any. `indexOf` returns the first index of an element or `-1` if it is not contained. The bottom element has index `0`.

</signature>

---

## Reordering

Shuffle or sort the stack.

<signature>
<code-block lang="kotlin" copy="false">
shuffle()
sort(comparator: Comparator&lt;in T&gt;)
</code-block>

Shuffles the stack randomly, which is the digital version of shuffling a deck, or sorts it with the given comparator.

</signature>

---

## Example

A draw pile for a card game, that is shuffled once and then drawn from:

```kotlin
val drawPile = Stack(deckOfCards)
drawPile.shuffle()

val hand = drawPile.popAll(5)        // the player draws five cards

if (drawPile.isEmpty()) {
    println("The draw pile is empty")
}
val next = drawPile.popOrNull()      // null if the pile ran out
```
