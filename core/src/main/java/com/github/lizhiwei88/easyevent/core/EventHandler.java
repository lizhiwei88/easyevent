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

import com.github.lizhiwei88.easyevent.evnet.InBoundEvent;

/**
 * @author lizhiwei
 * @date 2020/12/14 16:27
 **/
public class EventHandler<E> extends AbstractSubscribeManager<InBoundEvent> {

    public void onEvent(String name, E client, Object parameter) {
        InBoundEvent event = observerMap.get(name);
        event.execute(client, parameter);

    }
}
