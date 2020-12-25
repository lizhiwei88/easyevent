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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 事件分发器
 * 当调用publish时,不指定group会通知所有的监听者
 * 线程安全
 *
 * @author lizhiwei
 **/
public class EventDispatcher<E> {

    private static final String PUBLIC_GROUP = "public";

    private static final String GROUP_PREFIX = "group:";

    private final ConcurrentMap<String, List<E>> observerMap = new ConcurrentHashMap<String, List<E>>();

    /**
     * 发布事件给所有监听者
     *
     * @param event 事件
     */
    public void publish(OutBoundEvent<E> event) {
        publish(event, null);
    }

    /**
     * 发布事件给所有监听者
     *
     * @param event 事件
     * @param group 监听者组. null: 通知所有监听者
     */
    public void publish(OutBoundEvent<E> event, String group) {
        if (group == null) {
            for (Map.Entry<String, List<E>> groupEntry : observerMap.entrySet()) {
                handle(event, groupEntry.getValue());
            }
        } else {
            handle(event, observerMap.get(group));
        }
    }

    /**
     * 遍历通知组中所有监听者
     *
     * @param event      事件
     * @param clientList 监听者集合
     */
    private void handle(OutBoundEvent<E> event, List<E> clientList) {
        if (clientList == null || clientList.isEmpty()) {
            return;
        }
        for (E client : clientList) {
            event.execute(client);
        }
    }

    /**
     * 将监听者加入到默认组.此组为公共组,不能为其单独发布通知
     *
     * @param client 监听者
     */
    public void subscribe(E client) {
        doSubscribe(PUBLIC_GROUP, client);
    }

    /**
     * 将监听者加入到指定组中
     *
     * @param group  组名
     * @param client 监听者
     */
    public void subscribe(String group, E client) {
        if (group == null) {
            throw new NullPointerException();
        }
        doSubscribe(getKey(group), client);
    }

    private void doSubscribe(String group, E client) {
        if (client == null) {
            throw new NullPointerException();
        }
        List<E> clientList = Collections.synchronizedList(new LinkedList<E>());
        clientList.add(client);
        synchronized (this) {
            if (observerMap.containsKey(group)) {
                clientList = observerMap.get(group);
                if (clientList != null) {
                    clientList.add(client);
                }
            }
            observerMap.put(group, clientList);
        }
    }

    /**
     * 将监听者从默认组中移除
     *
     * @param client 监听者
     */
    public void unsubscribe(E client) {
        doUnsubscribe(PUBLIC_GROUP, client);
    }

    /**
     * 将监听者从指定组中移除
     *
     * @param group  组名
     * @param client 监听者
     */
    public void unsubscribe(String group, E client) {
        if (group == null) {
            throw new NullPointerException();
        }
        doUnsubscribe(getKey(group), client);
    }

    private void doUnsubscribe(String group, E client) {
        List<E> clientList = observerMap.get(group);
        if (clientList == null) {
            return;
        }
        synchronized (this) {
            Iterator<E> iterator = clientList.iterator();
            while (iterator.hasNext()) {
                E oldClient = iterator.next();
                if (oldClient.equals(client)) {
                    iterator.remove();
                }
            }
            if (clientList.isEmpty()) {
                observerMap.remove(group);
            }
        }
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
