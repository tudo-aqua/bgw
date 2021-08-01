---
title: Observable
nav_order: 1
layout: default
---

# Observable

{: .no_toc}
<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

In this section we are going to showcase the differend types of observales in the BGW framework.
Observables are used to enable listening on changes of element properties.

## Properties
Properties represent an attribute that notifies all listeners if it gets changed.
A listener can be added by [Property#addListener](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-value-observable/add-listener.html) or [Property#addListenerAndInvoke](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-value-observable/add-listener-and-invoke.html) wich also invokes it vith the given value. Note that the given value is not set to the backing field.
Every time the value of this observable is set it invokes all listeners if and only if the new value does not equal the old one. If you need to invoke all listeners with the current value use [Property#notifyUnchanged](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-property/notify-unchanged.html).

For common data types there are dedicated classes
* [BooleanProperty](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-boolean-property/index.html)
* [IntegerProperty](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-integer-property/index.html)
* [DoubleProperty](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-double-property/index.html)
* [StringProperty](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-string-property/index.html)

For generic types use baseclass [Property<T>](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-property/index.html).

## Observable Lists
Observable list work exaktly like Properties. It provides all standard list operations and notifies listeners every time the list changes.
In the BGW framework there exist two implementations of the abstract [ObservableList](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-observable-list/index.html):
* [ObservableArrayList](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-observable-array-list/index.html) and
* [ObservableLinkedList](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-observable-linked-list/index.html)
