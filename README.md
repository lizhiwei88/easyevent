# Easy Event
一个易于处理事件的工具

## 介绍
EasyEvent用于在异步场景下,对监听对象进行事件的通知处理.  主要分为两个功能  

- 将事件通知到对应的客户. 即 `OutBoundEvent`
- 客户触发的事件交由相应的事件处理.即 `InBoundEvent`


### EventDispatcher
**EventDispatcher**是将事件通知到监听客户.

### EventHandler
**EventHandler**是客户触发的事件与参数发送给监听的事件处理对象处理


## 使用
`example`模块为websocket应用实例

### Base
```xml
<dependency>
    <groupId>com.github.lizhiwei88</groupId>
    <artifactId>easyevent-core</artifactId>
    <version>0.2</version>
</dependency>
```

客户订阅事件

```java
/**
* 发布给客户的事件
*/
public class CustomOutBoundEvent implements OutBoundEvent<Client> {
        
        /**
        * 业务参数
        */
        private int age;

        /**
        * 业务参数
        */
        private String name;

        /**
         * 事件业务
         *
         * @param client 客户
         */
        @Override
        void execute(CLient client) {
            // 业务处理
            // ...
            client.sendMessage("result");
        }   
}
```

处理客户触发的事件

```java
/**
* 定义监听的事件 通过注解自动加载到EventHandler中
*/
@EasyEvent("login")
public class LoginInBoundEvent implements InBoundEvent<WebSocketSession> {

    @Override
    public void execute(WebSocketSession client, Object parameter) {
        try {
            // 业务处理
            // 参数处理
            client.sendMessage(new TextMessage("login event"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

订阅对象
```java
/**
* demo
*/
public class DemoClass {

    private static final EventDispatcher<Client> eventDispatcher = new EventDispatcher<>();

    private static final EventHandler<Client> eventHandler = new EventHandler<>();

    /**
     * eventDispatcher加入监听客户
     * @param client 实现OutBoundEvent
     */
    public void addClientToEventDispatcher(Client client) {
        eventDispatcher.subscribe(client);
    }

    /**
     * 将事件分发给客户
     * @param outBoundEvent 自定义的事件
     */
    public void publishToClient(OutBoundEvent<Client> outBoundEvent) {
        eventDispatcher.publish(outBoundEvent);
    }

    /**
     * 订阅监听事件 等待客户触发
     * @param inBoundEvent
     */
    public void addEventToEventHandler(InBoundEvent<Client> inBoundEvent) {
        eventHandler.subscribe(inBoundEvent);
    }

    /**
     * 将客户请求的事件和参数发送到对象的事件处理业务上
     * @param eventType 事件类型
     * @param client 客户
     * @param parameter 参数
     */
    public void onEvent(String eventType, Client client, Object parameter) {
        eventHandler.onEvent(eventType ,client, parameter);
    }
}
```

### SpringBoot

```xml
<dependency>
    <groupId>com.github.lizhiwei88</groupId>
    <artifactId>easyevent-spring-boot-starter</artifactId>
    <version>0.2</version>
</dependency>
```

客户订阅事件

```java
/**
* 发布给客户的事件
*/
public class CustomOutBoundEvent implements OutBoundEvent<Client> {
        
        /**
        * 业务参数
        */
        private int age;

        /**
        * 业务参数
        */
        private String name;

        /**
         * 事件业务
         *
         * @param client 客户
         */
        @Override
        void execute(CLient client) {
            // 业务处理
            // ...
            client.sendMessage("result");
        }   
}
```

```java
@Component
public class Service {
    /**
    * 管理订阅客户Bean
    */
    @Autowired
    private EventDispatcher eventDispatcher;

    /**
    * 订阅. client客户. 例如: WebsocketSession 或 Netty下的Channel
    */
    public void subscribe(String name, CLient client) {
        eventDispatcher.subscribe(name, client);
    }
    
    /**
    * 取消订阅
    */
    public void unsubscribe(String name) {
        eventDispatcher.unsubscribe(name);
    }

    /**
    * 发布事件 
    * 将OutBoundEvent类型的事件通知到客户
    */
    public void publish() {
        CustomOutBoundEvent outBoundEvent = new CustomOutBoundEvent(18, "jack");
        eventDispatcher.publish(outBoundEvent);
    }

}
```

处理客户触发的事件

```java
/**
* 定义监听的事件 通过注解自动加载到EventHandler中
*/
@EasyEvent("login")
public class LoginInBoundEvent implements InBoundEvent<WebSocketSession> {

    @Override
    public void execute(WebSocketSession client, Object parameter) {
        try {
            // 业务处理
            // 参数处理
            client.sendMessage(new TextMessage("login event"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

```java
/**
* 事件接收
*/
@Component
public class WebsocketService {

    /**
    * 管理订阅客户Bean
    */
    @Autowired
    private EventHandler eventHandler;

    /**
    * 订阅. client客户. 例如: WebsocketSession 或 Netty下的Channel
    */
    public void onMessage(WebSocketSession client, Message message) {
        /**
        * 指定事件类型 触发事件
        */
        eventHandler.onEvent(message.eventType, session, message);
    }

}
```
