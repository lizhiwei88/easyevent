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

import com.github.lizhiwei88.easyevent.evnet.OutBoundEvent;
import com.github.lizhiwei88.easyevent.exception.EventTypeException;

import java.util.Map;

/**
 * 事件分发器
 *
 * @author lizhiwei
 **/
public class EventDispatcher<E> extends AbstractSubscribeManager<E> {

    /**
     * 发布事件给所有监听者
     *
     * @param event 事件
     */
    public void publish(OutBoundEvent event) {
        if (!(event instanceof OutBoundEvent)) {
            throw new EventTypeException("event type does not match");
        }
        for (Map.Entry<String, E> entry : observerMap.entrySet()) {
            E client = entry.getValue();
            event.execute(client);
        }
    }
}
