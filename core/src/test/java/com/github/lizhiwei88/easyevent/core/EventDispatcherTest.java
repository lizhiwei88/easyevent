package com.github.lizhiwei88.easyevent.core;

import com.github.lizhiwei88.easyevent.event.OutBoundEvent;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;


/**
 * @author lizhiwei
 **/
public class EventDispatcherTest {

    @Test
    public void publish() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe("123");
        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("123", client);
            }
        });

        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertNotEquals("222", client);
            }
        });

        // 没有执行说明正确
        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertNotEquals("123", client);
            }
        }, "group");
    }

    @Test
    public void groupPublish() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe("group", "123");
        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("123", client);
            }
        }, "group");

        // 没有执行说明正确
        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertNotEquals("123", client);
            }
        }, "0000");

        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertNotEquals("222", client);
            }
        }, "group");
    }

    @Test
    public void unsubscribe() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe("123");
        eventDispatcher.subscribe("456");

        eventDispatcher.unsubscribe("123");

        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("456", client);
            }
        });

        eventDispatcher.unsubscribe("456");

        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertNotEquals("456", client);
            }
        });

    }


    @Test
    public void groupUnsubscribe() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe("group", "123");
        eventDispatcher.subscribe("group", "456");

        eventDispatcher.unsubscribe("123");

        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                boolean contains = Arrays.asList("123", "456").contains(client);
                Assert.assertTrue(contains);
            }
        }, "group");

        eventDispatcher.unsubscribe("group", "456");

        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("123", client);
            }
        }, "group");
        eventDispatcher.unsubscribe("group", "123");

        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertTrue(false);
            }
        }, "group");

    }

    @Test(expected = NullPointerException.class)
    public void exception() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe("123");
        eventDispatcher.publish(null);
    }


    @Test(expected = NullPointerException.class)
    public void exceptionArgument() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe(null, null);
    }

}
