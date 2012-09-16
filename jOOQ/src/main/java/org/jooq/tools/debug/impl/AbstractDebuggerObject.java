/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 *                          Christopher Deckers, chrriis@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.tools.debug.impl;

import java.util.List;
import java.util.UUID;

import org.jooq.tools.debug.DebuggerObject;

/**
 * A common base class for {@link DebuggerObject}
 *
 * @author Christopher Deckers
 * @author Lukas Eder
 */
abstract class AbstractDebuggerObject implements DebuggerObject {

    /**
     * Generated UID
     */
    private static final long                serialVersionUID = 794370947159841039L;

    private final UUID                       uuid;
    private transient AbstractDebuggerObject delegate;

    AbstractDebuggerObject() {
        this.uuid = UUID.randomUUID();
    }

    @Override
    public final UUID getId() {
        return uuid;
    }

    /**
     * Call through to delegates in order to synchronise {@link DebuggerObject}
     * state between client and server
     */
    void apply() {
        if (delegate != null) {
            delegate.apply();
        }
    }

    @Override
    public void remove() {
        if (delegate != null) {
            delegate.remove();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AbstractDebuggerObject) {
            return uuid.equals(((AbstractDebuggerObject) o).uuid);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    /**
     * Set a delegate object that handles {@link #apply()} and {@link #remove()}
     * calls.
     */
    void setDelegate(AbstractDebuggerObject delegate) {
        this.delegate = delegate;
    }

    /**
     * Create a delegate object
     */
    static <P extends AbstractDebuggerObject, C extends AbstractDebuggerObject> C delegate(P parent, C child,
        List<C> siblings) {

        synchronized (parent) {
            child.setDelegate(new Delegate<P, C>(parent, child, siblings));
            siblings.add(child);
        }

        return child;
    }

    /**
     * A delegate object
     */
    static class Delegate<P extends AbstractDebuggerObject, C extends AbstractDebuggerObject> extends
        AbstractDebuggerObject {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 8305051097073149775L;

        private final P           parent;
        private final C           child;
        private final List<C>     siblings;

        Delegate(P parent, C child, List<C> siblings) {
            this.parent = parent;
            this.child = child;
            this.siblings = siblings;
        }

        @Override
        void apply() {
            parent.apply();
        }

        @Override
        public void remove() {
            synchronized (parent) {
                siblings.remove(child);
                apply();
            }
        }
    }
}
