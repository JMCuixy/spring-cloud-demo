package com.cloud.demo.ribbon.consumer.rxjava;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * @author : cuixiuyin
 * @date : 2019/7/5
 */
public class Demo3 {

    // RxJava 遵循的是线程不变的原则，
    // 即：在哪个线程调用 subscribe()，就在哪个线程生产事件；在哪个线程生产事件，就在哪个线程消费事件。
    public static void main(String[] args) {

        //1. 默认的 Scheduler。不指定线程，直接在当前线程执行
        Scheduler immediate = Schedulers.immediate();

        //2. 总是启用新线程，并在新线程执行操作。
        Scheduler newThread = Schedulers.newThread();

        //3.  I/O  操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，
        //    区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread()
        //    更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
        Scheduler io = Schedulers.io();

        //4. 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。
        //   这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，
        //   否则 I/O 操作的等待时间会浪费 CPU。
        Scheduler computation = Schedulers.computation();

        // 事件
        Observable<Integer> observable = Observable.just(1, 2, 3, 4);

        // 订阅
        observable
                // 指定发生的线程
                .subscribeOn(io)
                // 指定 Subscriber 的回调线程
                .observeOn(immediate)
                .subscribe(t -> {
                    System.out.println(t);
                });
    }
}
