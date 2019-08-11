package com.cloud.demo.gateway.reactor;


import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 调度器和线程模型
 *
 * @author : cuixiuyin
 * @date : 2019/8/11
 */
public class Demo4 {

    public static void main(String[] args) throws InterruptedException {

        // 1.默认的 Scheduler。不指定线程，直接在当前线程执行
        Scheduler immediate = Schedulers.immediate();
        // 2.可重用的单线程, 对所有调用者都提供同一个线程来使用，直到该调度器被废弃
        Scheduler single = Schedulers.single();
        // 3.不可重用的单线程，独占式
        Scheduler newSingle = Schedulers.newSingle("schedulers-single");
        // 4.弹性线程池，它根据需要创建一个线程池，重用空闲线程。线程池如果空闲时间过长（默认为 60s）就会被废弃。对于 I/O 阻塞的场
        // 景比较适用。Schedulers.elastic() 能够方便地给一个阻塞的任务分配它自己的线程，从而不会妨碍其他任务和资源；
        Scheduler elastic = Schedulers.elastic();
        // 5.固定线程池，所创建的线程池的大小与CPU个数等同；
        Scheduler parallel = Schedulers.parallel();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Mono.fromCallable(() -> getStringSync())
                // 使用 subscribeOn 将任务调度到 Schedulers 内置的弹性线程池执行，弹性线程池会为 Callable 的执行任务分配一个单独的线程。
                .subscribeOn(Schedulers.elastic())
                .subscribe(System.out::println, null, countDownLatch::countDown);
        countDownLatch.await(10, TimeUnit.SECONDS);

        // Reactor 提供了两种在响应式链中调整调度器 Scheduler 的方法：publishOn 和 subscribeOn。它们都接受一个 Scheduler 作为
        // 参数，从而可以改变调度器。但是 publishOn 在链中出现的位置是有讲究的，而 subscribeOn 则无所谓。
        // 1. publishOn 会影响链中其后的操作符。
        // 2. subscribeOn 无论出现在什么位置，都只影响源头的执行环境。

    }

    private static String getStringSync() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello, Reactor!";
    }
}
