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

package com.github.lizhiwei88.easyevent.example.websocket.event;

import com.github.lizhiwei88.easyevent.annotation.EasyEvent;
import com.github.lizhiwei88.easyevent.evnet.InBoundEvent;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author lizhiwei
 * @date 2020/12/15 11:07
 **/
@EasyEvent("login")
public class LoginEasyEvent implements InBoundEvent<WebSocketSession> {

    @Override
    public void execute(WebSocketSession client, Object parameter) {
        try {
            client.sendMessage(new TextMessage("login event"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
