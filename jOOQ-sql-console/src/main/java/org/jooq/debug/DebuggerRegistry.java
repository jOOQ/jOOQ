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
package org.jooq.debug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Christopher Deckers
 */
public class DebuggerRegistry {

	private DebuggerRegistry() {}

	private static final Object LOCK = new Object();
	private static final List<Debugger> debuggerList = new ArrayList<Debugger>();

	public static void addSqlQueryDebugger(Debugger sqlQueryDebugger) {
		synchronized(LOCK) {
			debuggerList.add(sqlQueryDebugger);
			for(DebuggerRegistryListener l: debuggerRegisterListenerList) {
				l.notifyDebuggerListenersModified();
			}
		}
	}

	public static void removeSqlQueryDebugger(Debugger sqlQueryDebugger) {
		synchronized(LOCK) {
			debuggerList.remove(sqlQueryDebugger);
			for(DebuggerRegistryListener l: debuggerRegisterListenerList) {
				l.notifyDebuggerListenersModified();
			}
		}
	}

	private static final List<DebuggerRegistryListener> debuggerRegisterListenerList = new ArrayList<DebuggerRegistryListener>(1);

	public static void addDebuggerRegisterListener(DebuggerRegistryListener listener) {
		synchronized(LOCK) {
			debuggerRegisterListenerList.add(listener);
		}
	}

	public static void removeDebuggerRegisterListener(DebuggerRegistryListener listener) {
		synchronized(LOCK) {
			debuggerRegisterListenerList.remove(listener);
		}
	}

	/*
	 * @return an immutable list of all the debuggers currently registered.
	 */
	public static List<Debugger> getDebuggerList() {
		synchronized(LOCK) {
			if(debuggerList.isEmpty()) {
				// No cost when no loggers
				return Collections.emptyList();
			}
			// Small cost: copy collection and make it immutable.
			// Generally, no more than one or two listeners in the list.
			return Collections.unmodifiableList(new ArrayList<Debugger>(debuggerList));
		}
	}

}
