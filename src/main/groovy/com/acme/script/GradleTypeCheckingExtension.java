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

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCall;
import org.codehaus.groovy.transform.stc.AbstractTypeCheckingExtension;
import org.codehaus.groovy.transform.stc.StaticTypeCheckingVisitor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GradleTypeCheckingExtension extends AbstractTypeCheckingExtension {
    private static final List<MethodNode> NOT_FOUND = Collections.emptyList();
    private static final Set<String> BLESSED_INTERFACES = Collections.singleton(Blessed.class.getName());
    private static final String BASE_SCRIPT = MyBaseScript.class.getName();

    public GradleTypeCheckingExtension(final StaticTypeCheckingVisitor typeCheckingVisitor) {
        super(typeCheckingVisitor);
    }

    @Override
    public List<MethodNode> handleMissingMethod(final ClassNode receiver, final String name, final ArgumentListExpression argumentList, final ClassNode[] argumentTypes, final MethodCall call) {
        // here you can decide what to do wrt to the missing method. Check if it's a valid extension, wire it directly so that it's statically compiled, ...
        // in this case we will just tell the type checker not to worry if the method name starts with "valid"
        if (name.startsWith("valid")) {
            setHandled(true);
            return Collections.singletonList(makeDynamic(call));
        } else {
            return NOT_FOUND;
        }
    }

    @Override
    public void onMethodSelection(final Expression expression, final MethodNode target) {
        ClassNode declaringClass = target.getDeclaringClass();
        if (declaringClass.isDerivedFrom(classNodeFor(BASE_SCRIPT))) {
            for (ClassNode classNode : declaringClass.getInterfaces()) {
                if (BLESSED_INTERFACES.contains(classNode.getName())) {
                    if (classNode.hasDeclaredMethod(target.getName(), target.getParameters())) {
                        return;
                    }
                }
            }
            addStaticTypeError("Well tried, but you are not allowed to call this method", expression);
        }
    }
}
