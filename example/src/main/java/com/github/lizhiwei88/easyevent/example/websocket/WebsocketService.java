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

package com.github.lizhiwei88.easyevent.example.websocket;

import com.github.lizhiwei88.easyevent.core.EventDispatcher;
import com.github.lizhiwei88.easyevent.core.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author lizhiwei
 **/

@Component
public class WebsocketService extends TextWebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(WebsocketService.class);

    @Autowired
    private EventHandler eventHandler;

    @Autowired
    private EventDispatcher eventDispatcher;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("connection successfully");
        // 连接后加入默认组
        // 登录成功移动到vip组
        eventDispatcher.subscribe(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("connection closed");
        // 从vip组移除
        eventDispatcher.unsubscribe(session.getId(), "vip");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 分组形式
//        eventHandler.onEvent("login", "base", session, message);
        eventHandler.onEvent("login", session, message);
    }
}
