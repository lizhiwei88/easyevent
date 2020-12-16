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

import com.github.lizhiwei88.easyevent.core.EventDispatcher;
import com.github.lizhiwei88.easyevent.evnet.OutBoundEvent;
import org.junit.jupiter.api.Test;

/**
 * @author lizhiwei
 * @date 2020/12/14 13:42
 **/
class EventDispatcherTest {

    @Test
    void test() {
        ClientTest clientTest1 = new ClientTest("1");
        ClientTest clientTest2 = new ClientTest("2");
        ClientTest clientTest3 = new ClientTest("3");

        EventDispatcher<ClientTest> eventDispatcher = new EventDispatcher<ClientTest>();
        eventDispatcher.subscribe(clientTest1.getId(), clientTest1);
        eventDispatcher.subscribe(clientTest2.getId(), clientTest2);
        eventDispatcher.subscribe(clientTest3.getId(), clientTest3);

        OutBoundEvent outBoundEvent = new OutBoundEvent<ClientTest>() {

            private String parameter = "parameter";

            public void execute(ClientTest client) {
                System.out.println("execute parameter: " + parameter + " clientId: " + client.getId());
            }
        };

        eventDispatcher.publish(outBoundEvent);

        ParameterListenerEvent pa = new ParameterListenerEvent("2", "name");

        eventDispatcher.publish(pa);
    }
}
