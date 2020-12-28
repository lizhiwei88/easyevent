package com.github.lizhiwei88.easyevent.core;

import com.github.lizhiwei88.easyevent.event.InBoundEvent;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author lizhiwei
 **/
public class EventHandlerTest {

    @Test
    public void onEvent() throws Exception {
        EventHandler<Object> eventHandler = new EventHandler<Object>();

        InBoundEvent<Object> loginEvent = new InBoundEvent<Object>() {
            @Override
            public void execute(Object client, Object parameter) throws Exception {
                Assert.assertEquals(client, "123");
            }
        };

        InBoundEvent<Object> registerEvent = new InBoundEvent<Object>() {
            @Override
            public void execute(Object client, Object parameter) throws Exception {
                Assert.assertNotEquals(client, "123");
            }
        };

        InBoundEvent<Object> logoutEvent = new InBoundEvent<Object>() {
            @Override
            public void execute(Object client, Object parameter) throws Exception {
                Assert.assertTrue(false);
            }
        };

        eventHandler.subscribe("login", loginEvent);
        eventHandler.subscribe("register", registerEvent);
        eventHandler.subscribe("logout", "group", logoutEvent);

        eventHandler.onEvent("login", "123", null);
        eventHandler.onEvent("login", null, "123", null);
        eventHandler.onEvent("register", "456", null);
        eventHandler.onEvent("register", null, "456", null);

        eventHandler.onEvent("logout", "789", null);
        eventHandler.onEvent("logout", null, "789", null);
    }

    @Test
    public void groupOnEvent() throws Exception {
        EventHandler<Object> eventHandler = new EventHandler<Object>();

        InBoundEvent<Object> loginEvent = new InBoundEvent<Object>() {
            @Override
            public void execute(Object client, Object parameter) throws Exception {
                Assert.assertEquals(client, "123");
            }
        };

        InBoundEvent<Object> registerEvent = new InBoundEvent<Object>() {
            @Override
            public void execute(Object client, Object parameter) throws Exception {
                Assert.assertNotEquals(client, "123");
            }
        };

        InBoundEvent<Object> logoutEvent = new InBoundEvent<Object>() {
            @Override
            public void execute(Object client, Object parameter) throws Exception {
                Assert.assertTrue(false);
            }
        };

        eventHandler.subscribe("login", "group", loginEvent);
        eventHandler.subscribe("register", "group", registerEvent);
        eventHandler.subscribe("logout", logoutEvent);

        eventHandler.onEvent("login", "group", "123", null);
        eventHandler.onEvent("register", "group", "456", null);

        eventHandler.onEvent("logout", "group", "789", null);
    }

    @Test
    public void unsubscribe() throws Exception {
        EventHandler<Object> eventHandler = new EventHandler<Object>();

        InBoundEvent<Object> loginEvent = new InBoundEvent<Object>() {
            @Override
            public void execute(Object client, Object parameter) throws Exception {
                Assert.assertTrue(false);
            }
        };

        InBoundEvent<Object> registerEvent = new InBoundEvent<Object>() {
            @Override
            public void execute(Object client, Object parameter) throws Exception {
                Assert.assertEquals(client, "456");
            }
        };

        eventHandler.subscribe("login", loginEvent);
        eventHandler.subscribe("register", registerEvent);

        Assert.assertTrue(eventHandler.containsName("login"));
        Assert.assertTrue(eventHandler.containsName("register"));

        eventHandler.unsubscribe("login");

        eventHandler.onEvent("login", "123", null);
        eventHandler.onEvent("register", "456", null);

        Assert.assertTrue(eventHandler.containsName("register"));
        Assert.assertFalse(eventHandler.containsName("login"));
        Assert.assertFalse(eventHandler.containsName("register", "group"));

    }

    @Test
    public void groupUnsubscribe() throws Exception {
        EventHandler<Object> eventHandler = new EventHandler<Object>();

        InBoundEvent<Object> loginEvent = new InBoundEvent<Object>() {
            @Override
            public void execute(Object client, Object parameter) throws Exception {
                Assert.assertTrue(false);
            }
        };

        InBoundEvent<Object> registerEvent = new InBoundEvent<Object>() {
            @Override
            public void execute(Object client, Object parameter) throws Exception {
                Assert.assertEquals(client, "456");
            }
        };

        eventHandler.subscribe("login", "group", loginEvent);
        eventHandler.subscribe("register", "group", registerEvent);

        Assert.assertTrue(eventHandler.containsName("login", "group"));
        Assert.assertTrue(eventHandler.containsName("register", "group"));


        eventHandler.unsubscribe("login", "group");

        eventHandler.onEvent("login", "group", "123", null);
        eventHandler.onEvent("register", "group", "456", null);

        Assert.assertTrue(eventHandler.containsName("register", "group"));
        Assert.assertFalse(eventHandler.containsName("login", "group"));
        Assert.assertFalse(eventHandler.containsName("register"));

    }
}
