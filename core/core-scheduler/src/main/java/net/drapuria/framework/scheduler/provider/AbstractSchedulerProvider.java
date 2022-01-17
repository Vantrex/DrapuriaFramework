package net.drapuria.framework.scheduler.provider;

import lombok.SneakyThrows;
import net.drapuria.framework.scheduler.Scheduler;
import net.drapuria.framework.scheduler.pool.SchedulerPool;
import net.drapuria.framework.scheduler.helper.SchedulerHelper;
import net.drapuria.framework.task.ITask;
import net.drapuria.framework.task.TaskAlreadyStartedException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractSchedulerProvider {

    protected final Map<Long, SchedulerPool<? extends ITask>> schedulerPools = new HashMap<>();
    protected final Map<Scheduler<?>, Long> scheduledSchedulers = new ConcurrentHashMap<>();
    protected ITask delayTask;

    @SneakyThrows
    public AbstractSchedulerProvider() {
        initPool();
    }

    public void tickPool() {
        scheduledSchedulers.entrySet().removeIf(entry -> {
            long delay = entry.getValue();
            if (--delay == 0) {
                entry.getKey().tick();
                addOrCreatePool(entry.getKey());
                return true;
            }
            entry.setValue(delay);
            return false;
        });
    }

    @SneakyThrows
    public void shutdown() {
        this.delayTask.shutdown();
        this.scheduledSchedulers.clear();
        this.schedulerPools.values().forEach(SchedulerPool::shutdown);
    }

    public void addSchedulerToPool(final Scheduler<?> scheduler) {
        if (scheduler.getDelay() <= 0) {
            scheduler.tick();
            if (!this.schedulerPools.containsKey(scheduler.getPeriod())) {
                createSchedulerPool(scheduler.getPeriod());
                addOrCreatePool(scheduler);
                return;
            }
        }
        if (this.schedulerPools.containsKey(scheduler.getPeriod())) {
            SchedulerPool<?> group = this.schedulerPools.get(scheduler.getPeriod());
            scheduler.setDelay(SchedulerHelper.getTicksFromDuration(System.currentTimeMillis() - group.getLastTickTime(), true));
        }
        this.scheduledSchedulers.put(scheduler, scheduler.getDelay());
    }

    public void addOrCreatePool(final Scheduler<?> scheduler) {
        if (!this.schedulerPools.containsKey(scheduler.getPeriod()))
            createSchedulerPool(scheduler.getPeriod());
        this.schedulerPools.get(scheduler.getPeriod()).addScheduler(scheduler);
    }

    public <T extends ITask> void removePool(SchedulerPool<T> pool) {
        this.schedulerPools.remove(pool.getPeriod());
        pool.shutdown();
    }

    protected abstract void initPool() throws TaskAlreadyStartedException;

    protected abstract void createSchedulerPool(long period);
}