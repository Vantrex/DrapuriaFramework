/*
 * Copyright (c) 2022. Drapuria
 */

package net.drapuria.framework.velocity.impl;

import com.velocitypowered.api.proxy.ProxyServer;
import net.drapuria.framework.task.ITaskScheduler;

import java.util.concurrent.TimeUnit;

public class VelocityTaskScheduler implements ITaskScheduler {
    private final Object plugin;
    private final ProxyServer server;

    public VelocityTaskScheduler(Object plugin, ProxyServer server) {
        this.plugin = plugin;
        this.server = server;
    }

    @Override
    public int runAsync(Runnable runnable) {
        return server.getScheduler().buildTask(plugin, runnable)
                .schedule().status().ordinal();
    }

    @Override
    public int runAsyncScheduled(Runnable runnable, long time) {
        return server.getScheduler().buildTask(plugin, runnable)
                .delay(time / 20, TimeUnit.SECONDS)
                .schedule().status().ordinal();
    }

    @Override
    public int runAsyncRepeated(Runnable runnable, long time) {
        return server.getScheduler().buildTask(plugin, runnable)
                .repeat(time / 20, TimeUnit.SECONDS)
                .schedule().status().ordinal();
    }

    @Override
    public int runAsyncRepeated(Runnable runnable, long delay, long time) {
        return server.getScheduler().buildTask(plugin, runnable)
                .delay(delay / 20, TimeUnit.SECONDS)
                .repeat(time / 20, TimeUnit.SECONDS)
                .schedule().status().ordinal();
    }

    @Override
    public int runSync(Runnable runnable) {
        return server.getScheduler().buildTask(plugin, runnable)
                .schedule().status().ordinal();
    }

    @Override
    public int runScheduled(Runnable runnable, long time) {
        return server.getScheduler().buildTask(plugin, runnable)
                .delay(time / 20, TimeUnit.SECONDS)
                .schedule().status().ordinal();
    }

    @Override
    public int runRepeated(Runnable runnable, long time) {
        return server.getScheduler().buildTask(plugin, runnable)
                .repeat(time / 20, TimeUnit.SECONDS)
                .schedule().status().ordinal();
    }

    @Override
    public int runRepeated(Runnable runnable, long delay, long time) {
        return server.getScheduler().buildTask(plugin, runnable)
                .delay(delay / 20, TimeUnit.SECONDS)
                .repeat(time / 20, TimeUnit.SECONDS)
                .schedule().status().ordinal();
    }

    @Override
    public void cancel(int taskId) {
        throw new UnsupportedOperationException("Not yet supported on velocity implemetation");
    }
}
