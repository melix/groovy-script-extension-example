package com.acme.app

import com.acme.script.ScriptCompiler

def compiler = new ScriptCompiler()

def runExample = { String memo, String code ->
    println memo
    sleep 2000
    compiler.run(code)
    sleep 3000
}

runExample '''

First, just execute a script that defines a task. Everything is statically compiled.

''', '''
    task('hello', com.acme.tasks.Hello) {
        name = 'Chris'
    }
'''

runExample '''

Then, we invoke a method which is known to be valid. Everything is statically compiled, except from this dynamic method. It will
throw a runtime error, but that's fine.

''', '''
    task('hello', com.acme.tasks.Hello) {
        name = 'Chris'
    }
    validMissingMethod()
'''

runExample '''

Then, we invoke a method which is unknown, and not recognized by the type checking extension, so we will throw a compile time error.

''', '''
    task('hello', com.acme.tasks.Hello) {
        name = 'Chris'
    }
    invalidMissingMethodWillThrowACompileTimeError()
'''

runExample '''

Last example, trying to call a public method from the base script class, but that is not blessed.

''', '''

    thouShaltNotPass()

'''