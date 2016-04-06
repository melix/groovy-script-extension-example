package com.acme.tasks

import groovy.transform.CompileStatic

@CompileStatic
class Hello extends Task {
    String greeting = 'Hello,'
    String name

    @Override
    def void execute() {
        println "$greeting $name"
    }
}
