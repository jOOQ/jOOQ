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
package org.jooq.debug.console.remote.messaging;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


/**
 * A convenient class to register objects to an ID.
 * @author Christopher Deckers
 */
class ObjectRegistry {

    private Thread cleanUpThread;

    private synchronized void startThread() {
        if(cleanUpThread != null) {
            return;
        }
        cleanUpThread = new Thread("Registry cleanup thread") {
            @Override
            public void run() {
                while(true) {
                    try {
                        sleep(5000);
                    } catch(Exception e) {
                    }
                    synchronized(ObjectRegistry.this) {
                        for(Integer instanceID: instanceIDToObjectReferenceMap.keySet().toArray(new Integer[0])) {
                            if(instanceIDToObjectReferenceMap.get(instanceID).get() == null) {
                                instanceIDToObjectReferenceMap.remove(instanceID);
                            }
                        }
                        if(instanceIDToObjectReferenceMap.isEmpty()) {
                            cleanUpThread = null;
                            return;
                        }
                    }
                }
            }
        };
        cleanUpThread.setDaemon(true);
        cleanUpThread.start();
    }

    private int nextInstanceID = 1;
    private Map<Integer, WeakReference<Object>> instanceIDToObjectReferenceMap = new HashMap<Integer, WeakReference<Object>>();

    /**
     * Construct an object registry.
     */
    public ObjectRegistry() {
    }

    /**
     * Add an object to the registry.
     * @param o the object to add.
     * @return an unused instance ID that is strictly greater than 0.
     */
    public synchronized int add(Object o) {
        while(true) {
            int instanceID = nextInstanceID++;
            if(!instanceIDToObjectReferenceMap.containsKey(instanceID)) {
                if(o == null) {
                    return instanceID;
                }
                instanceIDToObjectReferenceMap.put(instanceID, new WeakReference<Object>(o));
                startThread();
                return instanceID;
            }
        }
    }

    /**
     * Add an object to the registry, specifying its ID, which throws an exception if the ID is already in use.
     * @param o the object to add.
     * @param instanceID the ID to associate the object to.
     */
    public synchronized void add(Object o, int instanceID) {
        Object o2 = get(instanceID);
        if(o2 != null && o2 != o) {
            throw new IllegalStateException("An object is already registered with the id \"" + instanceID + "\" for object: " + o);
        }
        instanceIDToObjectReferenceMap.put(instanceID, new WeakReference<Object>(o));
        startThread();
    }

    /**
     * Get an object using its ID.
     * @return the object, or null.
     */
    public synchronized Object get(int instanceID) {
        WeakReference<Object> weakReference = instanceIDToObjectReferenceMap.get(instanceID);
        if(weakReference == null) {
            return null;
        }
        Object o = weakReference.get();
        if(o == null) {
            instanceIDToObjectReferenceMap.remove(instanceID);
        }
        return o;
    }

    /**
     * Remove an object from the registry using its instance ID.
     * @param instanceID the ID of the object to remove.
     */
    public synchronized void remove(int instanceID) {
        instanceIDToObjectReferenceMap.remove(instanceID);
    }

    /**
     * Get all the instance IDs that are used in this registry.
     * @return the instance IDs.
     */
    public int[] getInstanceIDs() {
        Object[] instanceIDObjects = instanceIDToObjectReferenceMap.keySet().toArray();
        int[] instanceIDs = new int[instanceIDObjects.length];
        for(int i=0; i<instanceIDObjects.length; i++) {
            instanceIDs[i] = (Integer)instanceIDObjects[i];
        }
        return instanceIDs;
    }

}
