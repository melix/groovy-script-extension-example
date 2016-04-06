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

import com.acme.tasks.Task;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.Script;

import java.util.HashMap;
import java.util.Map;

public abstract class MyBaseScript extends Script implements Blessed {

    private final Map<String, Task> tasks = new HashMap<String, Task>();

    public <T extends Task> void task(String name, @DelegatesTo.Target Class<T> taskType, @DelegatesTo(genericTypeIndex = 0) Closure<?> configAction) {
        tasks.put(name, configureTask(taskType, configAction));
    }

    private <T extends Task> T configureTask(Class<T> taskType, @DelegatesTo(genericTypeIndex = 0) Closure<?> configAction) {
        T t = null;
        try {
            t = taskType.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
        configAction.setDelegate(t);
        configAction.call();
        return t;
    }

    public void thoughShaltNotPass() {
        throw new IllegalStateException("THOUGH SHALT NOT PASS!");
    }

    public void execute() {
        for (Task task : tasks.values()) {
            task.execute();
        }
    }

    @Override
    public Object invokeMethod(final String name, final Object args) {
        System.out.println("Invoking name = " + name + " with args " + args);
        return super.invokeMethod(name, args);
    }
}
