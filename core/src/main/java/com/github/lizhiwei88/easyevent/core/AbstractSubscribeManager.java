/*
 * Copyright 2020 lizhiwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.lizhiwei88.easyevent.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lizhiwei
 * @date 2020/12/14 17:08
 **/
public abstract class AbstractSubscribeManager<E> implements SubscribeManager<E> {

    protected final ConcurrentMap<String, E> observerMap = new ConcurrentHashMap<String, E>();

    public void subscribe(String name, E object) {
        observerMap.put(name, object);
    }

    public void unsubscribe(String name) {
        observerMap.remove(name);
    }

    public boolean containsName(String name) {
        return observerMap.containsKey(name);
    }
}
