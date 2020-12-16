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

import com.github.lizhiwei88.easyevent.evnet.OutBoundEvent;

/**
 * @author lizhiwei
 * @date 2020/12/14 14:05
 **/
public class ParameterListenerEvent implements OutBoundEvent<ClientTest> {

    private String parameterId;

    private String parameterName;

    public ParameterListenerEvent(String parameterId, String parameterName) {
        this.parameterId = parameterId;
        this.parameterName = parameterName;
    }

    public void execute(ClientTest client) {
        if (client.getId().equals(this.parameterId)) {
            System.out.println("id: " + client.getId() + " match ....");
        }
    }
}
