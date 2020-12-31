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

import com.github.lizhiwei88.easyevent.event.OutBoundEvent;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 事件分发器
 * 线程安全
 *
 * @author lizhiwei
 **/
public class EventDispatcher<E> implements EventObserver<E> {

    /**
     * 默认组名
     */
    private static final String DEFAULT_GROUP = "default";

    /**
     * 自定义组前缀
     */
    private static final String GROUP_PREFIX = "group:";

    /**
     * 监听者容器
     */
    private final ConcurrentMap<String, Map<String, E>> observerMap = new ConcurrentHashMap<String, Map<String, E>>();

    /**
     * 发布事件通知所有监听者
     *
     * @param event 事件
     */
    public void publishAll(OutBoundEvent<E> event) {
        for (Map.Entry<String, Map<String, E>> groupEntry : observerMap.entrySet()) {
            doPublish(null, groupEntry.getValue(), event);
        }
    }

    /**
     * 发布事件通知默认组监听者
     *
     * @param event 事件
     */
    public void publish(OutBoundEvent<E> event) {
        doPublish(null, observerMap.get(DEFAULT_GROUP), event);
    }

    /**
     * 发布事件通知指定监听组
     *
     * @param group 监听者组.
     * @param event 事件
     */
    public void publish(String group, OutBoundEvent<E> event) {
        if (group == null) {
            throw new NullPointerException();
        }
        doPublish(null, observerMap.get(getKey(group)), event);
    }

    /**
     * 发布事件通知指定监听组中指定监听者
     *
     * @param name  name
     * @param group 监听者组. null: 通知所有监听者
     * @param event 事件
     */
    public void publish(String name, String group, OutBoundEvent<E> event) {
        if (name == null || group == null) {
            throw new NullPointerException();
        }
        doPublish(name, observerMap.get(getKey(group)), event);
    }

    /**
     * 通知监听者
     *
     * @param key       key
     * @param clientMap 监听者集合
     * @param event     事件
     */
    private void doPublish(String key, Map<String, E> clientMap, OutBoundEvent<E> event) {
        if (clientMap == null || clientMap.isEmpty()) {
            return;
        }
        if (key == null) {
            for (Map.Entry<String, E> entry : clientMap.entrySet()) {
                event.execute(entry.getValue());
            }
        }
        if (clientMap.containsKey(key)) {
            event.execute(clientMap.get(key));
        }
    }

    /**
     * 将监听者加入到默认组.
     *
     * @param name   name
     * @param client 监听者
     */
    @Override
    public void subscribe(String name, E client) {
        doSubscribe(name, DEFAULT_GROUP, client);
    }

    /**
     * 将监听者加入到指定组中
     *
     * @param name   name
     * @param group  组名
     * @param client 监听者
     */
    @Override
    public void subscribe(String name, String group, E client) {
        if (group == null) {
            throw new NullPointerException();
        }
        doSubscribe(name, getKey(group), client);
    }

    /**
     * 加入到集合
     *
     * @param key    key
     * @param group  组名
     * @param client 监听者
     */
    private void doSubscribe(String key, String group, E client) {
        if (key == null || client == null) {
            throw new NullPointerException();
        }
        synchronized (this) {
            if (observerMap.containsKey(group)) {
                observerMap.get(group)
                        .put(key, client);
            } else {
                Map<String, E> clientMap = Collections.synchronizedMap(new LinkedHashMap<String, E>());
                clientMap.put(key, client);
                observerMap.put(group, clientMap);
            }
        }
    }

    /**
     * 将监听者从默认组中移除
     *
     * @param name name
     * @return client
     */
    @Override
    public E unsubscribe(String name) {
        return doUnsubscribe(name, DEFAULT_GROUP);
    }

    /**
     * 将监听者从指定组中移除
     *
     * @param name  name
     * @param group 组名
     * @return client
     */
    @Override
    public E unsubscribe(String name, String group) {
        if (group == null) {
            throw new NullPointerException();
        }
        return doUnsubscribe(name, getKey(group));
    }

    /**
     * 移除监听者
     *
     * @param key   key
     * @param group 组名
     * @return client
     */
    private E doUnsubscribe(String key, String group) {
        Map<String, E> clientMap = observerMap.get(group);
        if (clientMap == null || clientMap.isEmpty()) {
            return null;
        }
        synchronized (this) {
            E client = clientMap.remove(key);
            if (clientMap.isEmpty()) {
                observerMap.remove(group);
            }
            return client;
        }
    }

    /**
     * 移除默认组所有监听者
     *
     * @return 返回该组所有监听者
     */
    public Map<String, E> unsubscribeDefaultGroup() {
        return observerMap.remove(DEFAULT_GROUP);
    }

    /**
     * 移除指定组所有监听者
     *
     * @param group 组名
     * @return 返回该组所有监听者
     */
    public Map<String, E> unsubscribeGroup(String group) {
        if (group == null) {
            throw new NullPointerException();
        }
        return observerMap.remove(getKey(group));
    }

    /**
     * 获取带前缀的组名
     *
     * @param group 组名
     * @return 前缀组名
     */
    private String getKey(String group) {
        return GROUP_PREFIX + group;
    }
}
