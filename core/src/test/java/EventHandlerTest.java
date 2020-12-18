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

import com.github.lizhiwei88.easyevent.core.EventHandler;
import com.github.lizhiwei88.easyevent.evnet.InBoundEvent;
import org.junit.jupiter.api.Test;

/**
 * @author lizhiwei
 * @date 2020/12/15 9:05
 **/
class EventHandlerTest {

    @Test
    void test() {
        EventHandler<ClientTest> eventHandler = new EventHandler<ClientTest>();

        eventHandler.subscribe("login", new InBoundEvent<ClientTest>() {
            @Override
            public void execute(ClientTest client, Object parameter) {
                System.out.println("login event");
                client.test();
            }
        });

        eventHandler.onEvent("login", new ClientTest("123"), "aaaa");

    }
}
