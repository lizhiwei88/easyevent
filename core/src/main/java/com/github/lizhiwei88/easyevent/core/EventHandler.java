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


import com.github.lizhiwei88.easyevent.event.InBoundEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 事件执行器
 * 线程安全
 *
 * @author lizhiwei
 **/
public class EventHandler<E> {

    private static final String SUBSCRIBE_NAME_DELIMITER = "-";

    private final ConcurrentMap<String, InBoundEvent<E>> observerMap = new ConcurrentHashMap<String, InBoundEvent<E>>();

    /**
     * 入站事件匹配执行
     *
     * @param name      事件名称
     * @param group     组名
     * @param client    当前客户
     * @param parameter 入站参数
     * @return 没有匹配到事件, 返回false
     * @throws Exception exception
     */
    public boolean onEvent(String name, String group, E client, Object parameter) throws Exception {
        return onEvent(getKey(name, group), client, parameter);
    }

    /**
     * 入站事件匹配执行
     *
     * @param name      事件名称
     * @param client    当前客户
     * @param parameter 入站参数
     * @return 没有匹配到事件, 返回false
     * @throws Exception exception
     */
    public boolean onEvent(String name, E client, Object parameter) throws Exception {
        InBoundEvent<E> event = observerMap.get(name);
        if (event == null) {
            return false;
        }
        event.execute(client, parameter);
        return true;
    }

    /**
     * 将监听事件加入到处理器中
     *
     * @param name  事件名称
     * @param event 事件
     */
    public void subscribe(String name, InBoundEvent<E> event) {
        observerMap.put(name, event);
    }

    /**
     * 将监听事件加入到处理器中, 增加分组名称
     *
     * @param name  事件名称
     * @param group 组名
     * @param event 事件
     */
    public void subscribe(String name, String group, InBoundEvent<E> event) {
        this.subscribe(getKey(name, group), event);
    }

    /**
     * 将事件从处理器中移除
     *
     * @param name 事件名称
     * @return 移除的事件
     */
    public InBoundEvent<E> unsubscribe(String name) {
        return observerMap.remove(name);
    }

    /**
     * 将指定分组事件从处理器中移除
     *
     * @param name  事件名称
     * @param group 组名
     * @return 移除的事件
     */
    public InBoundEvent<E> unsubscribe(String name, String group) {
        return this.unsubscribe(getKey(name, group));
    }

    /**
     * 事件是否存在
     *
     * @param name 事件名称
     * @return boolean
     */
    public boolean containsName(String name) {
        return observerMap.containsKey(name);
    }

    /**
     * 事件是否存在
     *
     * @param name  事件名称
     * @param group 组名
     * @return boolean
     */
    public boolean containsName(String name, String group) {
        return this.containsName(getKey(name, group));
    }

    /**
     * 字符串是否为空
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    /**
     * 获取拼接后的事件名称
     *
     * @param name  事件名称
     * @param group 组名
     * @return 真实的事件名称
     */
    private String getKey(String name, String group) {
        if (!isEmpty(group)) {
            return name + SUBSCRIBE_NAME_DELIMITER + group;
        }
        return name;
    }
}
