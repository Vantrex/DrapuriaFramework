package net.drapuria.framework.bukkit.impl.scheduler.pool;

import lombok.SneakyThrows;
import net.drapuria.framework.FrameworkMisc;
import net.drapuria.framework.bukkit.impl.task.DrapuriaAsyncBukkitTask;
import net.drapuria.framework.scheduler.ISchedulerService;
import net.drapuria.framework.scheduler.Scheduler;
import net.drapuria.framework.scheduler.SchedulerService;
import net.drapuria.framework.scheduler.provider.SchedulerProvider;


public class BukkitAsyncSchedulerPool extends BukkitSchedulerPool {

    private static final ISchedulerService service;

    static {
        service = SchedulerService.getService;
    }

    public BukkitAsyncSchedulerPool(long period) {
        super(period);
    }

    @SneakyThrows
    @Override
    public void start() {
        (super.task = new DrapuriaAsyncBukkitTask()).start(super.period, super.period, this::handle);
    }

    @Override
    public void addScheduler(Scheduler<?> scheduler) {
        FrameworkMisc.TASK_SCHEDULER.runSync(() -> {
           super.addScheduler(scheduler);
        });
    }

    @Override
    public void removeScheduler(Scheduler<?> scheduler) {
        FrameworkMisc.TASK_SCHEDULER.runSync(() -> {
            super.removeScheduler(scheduler);
        });
    }

    @Override
    public void handle() {
        this.lastTickTime = System.currentTimeMillis();
        this.schedulers.removeIf(Scheduler::tick);
        if (this.schedulers.isEmpty()) {
            FrameworkMisc.TASK_SCHEDULER.runSync(() -> {
                SchedulerProvider.getPool().removePool(BukkitAsyncSchedulerPool.this);
            });
        }
    }
}