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
    public void publishNoneGroup() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe("100", "client-1");
        eventDispatcher.subscribe("200", "testGroup", "client-1");
        eventDispatcher.publishAll(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                boolean contains = Arrays.asList("client-1", "client-2").contains(client);
                Assert.assertTrue(contains);
            }
        });
        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("client-1", client);
            }
        });
        eventDispatcher.publish("group", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertTrue(false);
            }
        });
    }

    @Test
    public void publishGroup() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe("100", "client-1");
        eventDispatcher.subscribe("200", "group", "client-2");
        eventDispatcher.publish("group", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("client-2", client);
            }
        });
        eventDispatcher.publish("testGroup", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertTrue(false);
            }
        });
    }

    @Test
    public void publishWithNameAndGroup() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe("100", "group", "client-1");
        eventDispatcher.subscribe("200", "group1", "client-2");

        eventDispatcher.publish("100", "group", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("client-1", client);
            }
        });
        eventDispatcher.publish("200", "group1", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("client-2", client);
            }
        });
        eventDispatcher.publish("group", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("client-1", client);
            }
        });
        eventDispatcher.publish("group1", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("client-2", client);
            }
        });
        eventDispatcher.publishAll(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                boolean contains = Arrays.asList("client-1", "client-2").contains(client);
                Assert.assertTrue(contains);
            }
        });
        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertTrue(false);
            }
        });
        eventDispatcher.publish("group3", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertTrue(false);
            }
        });
    }

    @Test
    public void unsubscribeNoneGroup() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe("100", "client-1");
        eventDispatcher.subscribe("200", "client-2");

        eventDispatcher.unsubscribe("100");

        eventDispatcher.publish("group", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertTrue(false);
            }
        });

        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("client-2", client);
            }
        });

        eventDispatcher.unsubscribe("200");

        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertTrue(false);
            }
        });

    }


    @Test
    public void unsubscribe() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe("100", "group", "client-1");
        eventDispatcher.subscribe("200", "group1", "client-2");

        // 不能被取消, 全局组没有100
        eventDispatcher.unsubscribe("100");

        eventDispatcher.publishAll(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                boolean contains = Arrays.asList("client-1", "client-2").contains(client);
                Assert.assertTrue(contains);
            }
        });

        eventDispatcher.publish("group", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("client-1", client);
            }
        });

        eventDispatcher.publish("group1", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("client-2", client);
            }
        });

        eventDispatcher.unsubscribe("100", "group");

        eventDispatcher.publishAll(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("client-2", client);
            }
        });

        eventDispatcher.unsubscribe("200", "group1");

        eventDispatcher.publishAll(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertTrue(false);
            }
        });

    }


    @Test
    public void unsubscribeAllGlobalGroup() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe("100", "client-1");
        eventDispatcher.subscribe("200", "group", "client-2");

        eventDispatcher.unsubscribeDefaultGroup();
        eventDispatcher.publish("group", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("client-2", client);
            }
        });
        eventDispatcher.publishAll(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("client-2", client);
            }
        });
    }

    @Test
    public void unsubscribeGroup() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe("100", "client-1");
        eventDispatcher.subscribe("200", "group", "client-2");

        eventDispatcher.unsubscribeGroup("group");
        eventDispatcher.publish("group", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertTrue(false);
            }
        });
        eventDispatcher.publishAll(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertEquals("client-1", client);
            }
        });
    }


    @Test(expected = NullPointerException.class)
    public void exception() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe("100", "client-1");
        eventDispatcher.publish(null);
    }


    @Test(expected = NullPointerException.class)
    public void exceptionArgument() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe(null, "client-1");
        eventDispatcher.subscribe(null, null, "client-1");
        eventDispatcher.subscribe("100", null, "client-1");
        eventDispatcher.subscribe("100", null, null);
        eventDispatcher.subscribe("100", "group", null);
        eventDispatcher.subscribe("100", null);
        eventDispatcher.publish(new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertTrue(false);
            }
        });
    }

    @Test()
    public void publishNull() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.publish("notGroup", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertTrue(false);
            }
        });
        eventDispatcher.publish("notKey", "notGroup", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertTrue(false);
            }
        });
    }

    @Test()
    public void publishNullKey() {
        EventDispatcher<Object> eventDispatcher = new EventDispatcher<Object>();
        eventDispatcher.subscribe("100", "group", "client-1");
        eventDispatcher.publish("notGroup", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertTrue(false);
            }
        });
        eventDispatcher.publish("notKey", "group", new OutBoundEvent() {
            @Override
            public void execute(Object client) {
                Assert.assertTrue(false);
            }
        });
    }

}
