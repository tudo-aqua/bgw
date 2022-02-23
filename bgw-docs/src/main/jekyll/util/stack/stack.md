---
parent: Util
title: Stack
nav_order: 3
---

<!-- KDoc -->
[StackKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/index.html
[peekKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/peek.html
[peekOrNullKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/peek-or-null.html
[peekAllKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/peek-all.html
[pushKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/push.html
[pushAllKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/push-all.html
[popKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/pop.html
[popOrNullKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/pop-or-null.html
[popAllKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/pop-all.html
[sizeKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/index.html
[isEmptyKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/is-empty.html
[isNotEmptyKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/is-not-empty.html
[clearKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/clear.html
[sortKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/sort.html
[shuffleKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/shuffle.html
[indexOfKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-stack/index-of.html

[NoSuchElementExceptionKDoc]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-no-such-element-exception/

<!-- Start Page -->
# Stack
{: .no_toc}

<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>


## Usage
The [Stack][StackKDoc] utility class is a kotlin implementation of the common stack collection. 
The stack may become useful for many board game applications like card stacks, money stacks, etc.

## Creation
The stack can be created with initial elements as a constructor parameter, either as collection or varargs, or as an empty stack with no constructor parameters.
The elements get added from the tail to the head such that the first element in the collection will be popped first and the last element gets the highest index.
````kotlin
val emptyStack: Stack<String> = Stack()
val collectionStack: Stack<String> = Stack(listOf("ONE", "SIX", "TEN"))
val varargsStack: Stack<String> = Stack("ONE", "SIX", "TEN")
````

## Modification
Adding and removing elements is only available for the topmost element as the stack represents a LIFO queue.

### Adding elements
Elements get added by calling [push][pushKDoc] for one element or [pushAll][pushAllKDoc] for multiple elements.
For multiple elements, they get pushed one by one from the first to the last element in the given collection.

### Retrieving elements
Elements get removed by calling [pop][popKDoc]. This removes the topmost element from the stack and returns it.
[popAll][popAllKDoc] can be used to pop all elements or, by specifying parameter *n*, the given number of elements.

[peek][peekKDoc] returns the topmost element without removing it.
[peekAll][popAllKDoc] works respectively.

The first index of any element in the stack can be obtained by calling [indexOf][indexOfKDoc].
This returns -1 if the element is not contained in the stack.

The stack can be cleared by calling [clear][clearKDoc].

Note that [pop][popKDoc]/[peek][peekKDoc] throws a [NoSuchElementException][NoSuchElementExceptionKDoc] if the stack is empty. 
Use [popOrNull][popOrNullKDoc]/[peekOrNull][peekOrNullKDoc] for safe access or check stack's [size][sizeKDoc] or call [isEmpty][isEmptyKDoc] / [isNotEmpty][isNotEmptyKDoc].

### Sorting / shuffling
The stack can be sorted by calling [sort][sortKDoc] and shuffled by calling [shuffle][shuffleKDoc].
