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

/**
 * @author lizhiwei
 **/
public interface SubscribeManager<E> {

    /**
     * 订阅
     *
     * @param name 名称
     * @param object 对象
     */
    void subscribe(String name, E object);

    /**
     * 取消订阅
     *
     * @param name 名称
     */
    void unsubscribe(String name);

    /**
     * 是否订阅
     *
     * @param name 名称
     * @return 订阅状态
     */
    boolean containsName(String name);

}
