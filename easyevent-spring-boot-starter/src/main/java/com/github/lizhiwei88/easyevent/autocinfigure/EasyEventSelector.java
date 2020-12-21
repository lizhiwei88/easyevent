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

package com.github.lizhiwei88.easyevent.autocinfigure;

import com.github.lizhiwei88.easyevent.annotation.EasyEvent;
import com.github.lizhiwei88.easyevent.core.EventHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author lizhiwei
 **/
public class EasyEventSelector implements ApplicationContextAware, SmartInitializingSingleton {

    private ApplicationContext applicationContext;

    @Autowired
    private EventHandler eventHandler;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beansWithAnnotationMap = this.applicationContext.getBeansWithAnnotation(EasyEvent.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()) {
            Object object = entry.getValue();
            String eventName = entry.getKey();
            if (StringUtils.isEmpty(eventName)) {
                eventName = object.getClass().getSimpleName();
            }
            eventHandler.subscribe(eventName, object);
        }
    }


}
