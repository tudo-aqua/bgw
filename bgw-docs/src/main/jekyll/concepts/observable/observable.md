---
parent: Concepts
title: Observable
nav_order: 4
layout: default
---

<!-- KDoc -->
[ObservableKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-observable/index.html
[addListenerKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-value-observable/add-listener.html
[addListenerAndInvokeKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-value-observable/add-listener-and-invoke.html
[notifyUnchangedKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-property/notify-unchanged.html

[PropertyKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-property/index.html
[BooleanPropertyKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-boolean-property/index.html
[IntegerPropertyKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-integer-property/index.html
[DoublePropertyKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-double-property/index.html
[LimitedDoublePropertyKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable.properties/-limited-double-property/index.html
[StringPropertyKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-string-property/index.html

[ReadonlyPropertyKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable.properties/-readonly-property/index.html
[ReadonlyBooleanPropertyKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-readonly-boolean-property/index.html
[ReadonlyIntegerPropertyKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-readonly-integer-property/index.html
[ReadonlyDoublePropertyKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-readonly-double-property/index.html
[ReadonlyStringPropertyKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-readonly-string-property/index.html

[ObservableListKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-observable-list/index.html
[ObservableArrayListKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-observable-array-list/index.html
[ObservableLinkedListKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.observable/-observable-linked-list/index.html

[IllegalArgumentExceptionKDoc]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/
<!-- Start Page -->
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

This section showcases the different types of observables in the BGW framework. [Observables][ObservableKDoc] are used
to enable listening on changes of element properties.

## Properties

Properties represent an attribute that notifies all listeners if it gets changed. A listener can be added
by [Property#addListener][addListenerKDoc]
or [Property#addListenerAndInvoke][addListenerAndInvokeKDoc]
which also invokes it with the given value. Note that the given value is not set to the backing field. Every time the
value of this observable is set it invokes all listeners if and only if the new value is not equal the old one. If 
all listeners should be invoked with the current value, use [Property#notifyUnchanged][notifyUnchangedKDoc].

For common data types there are dedicated classes:
* [BooleanProperty][BooleanPropertyKDoc]
* [IntegerProperty][IntegerPropertyKDoc]
* [DoubleProperty][DoublePropertyKDoc]
* [StringProperty][StringPropertyKDoc]

For generic types use the baseclass [Property<T>][PropertyKDoc].

The [LimitedDoubleProperty][LimitedDoublePropertyKDoc] behaves like a [DoubleProperty][DoublePropertyKDoc] 
but throws an [IllegalArgumentException][IllegalArgumentExceptionKDoc] is a value is set that exceeds its bounds given as constructor parameter.

The framework uses equivalent [ReadonlyProperties][ReadonlyPropertyKDoc] for one way communication. These come in the same variety:
* [ReadonlyBooleanProperty][ReadonlyBooleanPropertyKDoc]
* [ReadonlyIntegerProperty][ReadonlyIntegerPropertyKDoc]
* [ReadonlyDoubleProperty][ReadonlyDoublePropertyKDoc]
* [ReadonlyStringProperty][ReadonlyStringPropertyKDoc]
* [ReadonlyProperty<T>][ReadonlyPropertyKDoc].

## Observable Lists

The [ObservableList][ObservableListKDoc] work exactly like Properties. It provides all standard list operations and notifies listeners every 
time
the list changes. In the BGW framework there exist two implementations of the
abstract [ObservableList][ObservableListKDoc]:
* [ObservableArrayList][ObservableArrayListKDoc]
* [ObservableLinkedList][ObservableLinkedListKDoc]
