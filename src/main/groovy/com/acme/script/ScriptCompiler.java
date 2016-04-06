/*
 * Copyright 2003-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.script;

import groovy.lang.GroovyClassLoader;
import groovy.transform.CompileStatic;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;

import java.util.Collections;

public class ScriptCompiler {
    @SuppressWarnings("unchecked")
    public Class<? extends MyBaseScript> compile(String script) {
        GroovyClassLoader gcl = new GroovyClassLoader(this.getClass().getClassLoader(), createCompilerConfiguration());
        Class aClass = gcl.parseClass(script);
        return (Class<? extends MyBaseScript>) aClass;
    }

    public void run(String script) {
        try {
            MyBaseScript myBaseScript = compile(script).newInstance();
            try {
                myBaseScript.run();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            myBaseScript.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private CompilerConfiguration createCompilerConfiguration() {
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setScriptBaseClass(MyBaseScript.class.getName());
        compilerConfiguration.addCompilationCustomizers(staticCompilationWithExtension());
        return compilerConfiguration;
    }

    private ASTTransformationCustomizer staticCompilationWithExtension() {
        return new ASTTransformationCustomizer(Collections.singletonMap(
                "extensions", "com.acme.script.GradleTypeCheckingExtension"
        ), CompileStatic.class);
    }
}
