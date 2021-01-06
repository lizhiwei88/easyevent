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

package com.github.lizhiwei88.easyevent.autoconfigure;

import com.github.lizhiwei88.easyevent.annotation.EasyEvent;
import com.github.lizhiwei88.easyevent.core.EventHandler;
import com.github.lizhiwei88.easyevent.event.InBoundEvent;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

/**
 * @author lizhiwei
 **/
public class EasyEventSelector implements ApplicationContextAware, SmartInitializingSingleton {

    private ApplicationContext applicationContext;

    @Autowired
    private EventHandler eventHandler;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(EasyEvent.class);
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            EasyEvent annotation = bean.getClass().getAnnotation(EasyEvent.class);
            String eventName = annotation.value();
            String eventGroup = annotation.group() == "" ? null : annotation.group();
            if (StringUtils.isEmpty(eventName)) {
                eventName = bean.getClass().getSimpleName();
            }
            eventHandler.subscribe(eventName, eventGroup, (InBoundEvent) bean);
        }
    }


}
