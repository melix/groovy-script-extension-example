## Constrained Groovy scripts

This project demonstrates how to apply static compilation to a script, while on the same time:

- still allow dynamic method calls (and show how you can limit the scope of what you can call dynamically)
- restrict allowed method calls to a list of blessed interfaces

Method calls will be statically compiled, unless recognized as a dynamic one.

## Run

Execute:

```
./gradlew run
```

## Principles

A script compiler:

- applies the `@CompileStatic` transformation transparently
- sets the base script class to a class that implements blessed interfaces
- applies a type checking extension which recognizes some method calls as dynamic

The type checking extension is able to recognize invalid method calls, and turn them into dynamic calls, but also recognize valid method calls and perform additional checks, like checking that it's a blessed interface.
The benefit of this is that everything happens at compile time, so there's no cost at invocation time.

