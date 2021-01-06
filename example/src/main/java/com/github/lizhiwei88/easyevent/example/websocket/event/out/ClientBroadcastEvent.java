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

package com.github.lizhiwei88.easyevent.example.websocket.event.out;

import com.github.lizhiwei88.easyevent.event.OutBoundEvent;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * 指定客户事件
 *
 * @author lizhiwei
 **/
public class ClientBroadcastEvent implements OutBoundEvent<WebSocketSession> {

    private String p;

    public ClientBroadcastEvent(String p) {
        this.p = p;
    }

    @Override
    public void execute(WebSocketSession client) {
        try {
            client.sendMessage(new TextMessage("=== you are " + p + ". ClientBroadcastEvent"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
