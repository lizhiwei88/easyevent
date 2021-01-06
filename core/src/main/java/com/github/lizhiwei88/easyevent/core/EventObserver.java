package com.github.lizhiwei88.easyevent.core;

/**
 * @author lizhiwei
 **/
public interface EventObserver<E> {

    /**
     * 将监听者加入到容器中开始监听
     *
     * @param name   name
     * @param object 监听者
     */
    void subscribe(String name, E object);

    /**
     * 将监听者加入到容器中开始监听,并分组
     *
     * @param name   name
     * @param group  group
     * @param object 监听者
     */
    void subscribe(String name, String group, E object);

    /**
     * 取消监听者
     *
     * @param name name
     * @return object
     */
    E unsubscribe(String name);

    /**
     * 取消指定组的监听者
     *
     * @param name  name
     * @param group group
     * @return object
     */
    E unsubscribe(String name, String group);

}
