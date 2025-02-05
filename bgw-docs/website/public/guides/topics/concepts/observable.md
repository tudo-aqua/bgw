[ObservableKDoc]: /docs/tools.aqua.bgw.observable/-observable/index.html
[addListenerKDoc]: /docs/tools.aqua.bgw.observable/-value-observable/add-listener.html
[addListenerAndInvokeKDoc]: /docs/tools.aqua.bgw.observable/-value-observable/add-listener-and-invoke.html
[notifyUnchangedKDoc]: /docs/tools.aqua.bgw.observable/-property/notify-unchanged.html
[PropertyKDoc]: /docs/tools.aqua.bgw.observable/-property/index.html
[BooleanPropertyKDoc]: /docs/tools.aqua.bgw.observable/-boolean-property/index.html
[IntegerPropertyKDoc]: /docs/tools.aqua.bgw.observable/-integer-property/index.html
[DoublePropertyKDoc]: /docs/tools.aqua.bgw.observable/-double-property/index.html
[LimitedDoublePropertyKDoc]: /docs/tools.aqua.bgw.observable.properties/-limited-double-property/index.html
[StringPropertyKDoc]: /docs/tools.aqua.bgw.observable/-string-property/index.html
[ReadonlyPropertyKDoc]: /docs/tools.aqua.bgw.observable.properties/-readonly-property/index.html
[ReadonlyBooleanPropertyKDoc]: /docs/tools.aqua.bgw.observable/-readonly-boolean-property/index.html
[ReadonlyIntegerPropertyKDoc]: /docs/tools.aqua.bgw.observable/-readonly-integer-property/index.html
[ReadonlyDoublePropertyKDoc]: /docs/tools.aqua.bgw.observable/-readonly-double-property/index.html
[ReadonlyStringPropertyKDoc]: /docs/tools.aqua.bgw.observable/-readonly-string-property/index.html
[ObservableListKDoc]: /docs/tools.aqua.bgw.observable/-observable-list/index.html
[ObservableArrayListKDoc]: /docs/tools.aqua.bgw.observable/-observable-array-list/index.html
[ObservableLinkedListKDoc]: /docs/tools.aqua.bgw.observable/-observable-linked-list/index.html
[IllegalArgumentExceptionKDoc]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/

> This guide is currently being rewritten. Content may be incomplete, incorrect or subject to change.
> {style="danger"}

# Observable

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

- [BooleanProperty][BooleanPropertyKDoc]
- [IntegerProperty][IntegerPropertyKDoc]
- [DoubleProperty][DoublePropertyKDoc]
- [StringProperty][StringPropertyKDoc]

For generic types use the baseclass [Property<T>][PropertyKDoc].

The [LimitedDoubleProperty][LimitedDoublePropertyKDoc] behaves like a [DoubleProperty][DoublePropertyKDoc]
but throws an [IllegalArgumentException][IllegalArgumentExceptionKDoc] is a value is set that exceeds its bounds given as constructor parameter.

The framework uses equivalent [ReadonlyProperties][ReadonlyPropertyKDoc] for one way communication. These come in the same variety:

- [ReadonlyBooleanProperty][ReadonlyBooleanPropertyKDoc]
- [ReadonlyIntegerProperty][ReadonlyIntegerPropertyKDoc]
- [ReadonlyDoubleProperty][ReadonlyDoublePropertyKDoc]
- [ReadonlyStringProperty][ReadonlyStringPropertyKDoc]
- [ReadonlyProperty<T>][ReadonlyPropertyKDoc].

## Observable Lists

The [ObservableList][ObservableListKDoc] work exactly like Properties. It provides all standard list operations and notifies listeners every
time
the list changes. In the BGW framework there exist two implementations of the
abstract [ObservableList][ObservableListKDoc]:

- [ObservableArrayList][ObservableArrayListKDoc]
- [ObservableLinkedList][ObservableLinkedListKDoc]
