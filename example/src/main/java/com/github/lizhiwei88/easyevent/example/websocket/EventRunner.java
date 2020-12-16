/*
 * Copyright 2020 lizhiwei
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.lizhiwei88.easyevent.example.websocket;

import com.github.lizhiwei88.easyevent.core.EventDispatcher;
import com.github.lizhiwei88.easyevent.example.websocket.event.BroadcastEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lizhiwei
 * @date 2020/12/15 14:41
 **/
@Component
public class EventRunner implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(EventRunner.class);

    @Autowired
    private EventDispatcher eventDispatcher;

    /**
     * 延时10秒向上线的所有人发送事件
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        BroadcastEvent broadcastEvent = new BroadcastEvent("custom parameter");

        ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(1, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });

        executorService.scheduleAtFixedRate(() -> {
            logger.info("schedule...");
            eventDispatcher.publish(broadcastEvent);
        }, 10, 3, TimeUnit.SECONDS);
    }
}
