package com.best.gwms.data.util;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * ThreadLocal 碰上 ThreadPool 有问题，集中起来，方便在每次request结束后清空 ThreadLocal 中的本地线程变量
 *
 * @author Created by maz on 2018/2/05.
 */
public class ThreadLocalUtil {

    protected static final List<ThreadLocal<?>> TL_LIST = Collections.synchronizedList(Lists.newArrayList());

    private ThreadLocalUtil() {}

    public static void refreshThreadLocal() {
        for (ThreadLocal<?> tl : TL_LIST) {
            tl.set(null);
        }
    }

    public static void addThreadLocal2List(ThreadLocal<?> tl) {
        TL_LIST.add(tl);
    }

    public static List<ThreadLocal<?>> getTlList() {
        return TL_LIST;
    }
}
