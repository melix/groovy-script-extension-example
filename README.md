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

