package com.cloud.demo.hystrix.dashboard.rxjava;

import rx.Observable;
import rx.Subscriber;

/**
 * @author : cuixiuyin
 * @date : 2019/7/4
 */
public class Demo1 {

    // 1.Observable (观察者、事件源)用来向订阅者Subscriber（订阅者）对象发布事件，Subscriber对象则在接收到事件后对其进行处理，而在这里所指的事件通常就是对依赖服务的调用。
    // 2.一个 Observable 可以发出多个事件，直到结束或是发生异常。
    // 3.Observable 对象每发出一个事件，就会调用对应订阅者 Subscriber 对象的onNext()方法。
    // 4.每一个 Observable 的执行，最后一定会通过调用 Subscriber. onCompleted()或者Subscriber.onError()来结束该事件的操作流。
    public static void main(String[] args) {

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                // 这里放入同步或者异步请求
                subscriber.onNext("Hello RxJava");
                subscriber.onNext("JMCui");
                // 通过调用 Subscriber.onCompleted() 或者 Subscriber.onError() 来结束该事件的操作流,二者是互斥的，只能调用一个
                subscriber.onCompleted();
            }
        });


        Subscriber<String> subscriber = new Subscriber<String>() {

            // 队列事件完结。当不会有新的 omNext() 发出时，需要触发 onCompleted() 方法作为标志。
            @Override
            public void onCompleted() {

            }

            // 队列事件异常时触发。队列自动终止，不允许再有事件发生。
            @Override
            public void onError(Throwable e) {

            }


            @Override
            public void onNext(String s) {
                // 这里获取结果
                System.out.println("Subscriber:" + s);
            }
        };

        // 订阅
        // 通过subscribe()方法订阅后，生产者中的方法被执行
        observable.subscribe(subscriber);
    }
}
