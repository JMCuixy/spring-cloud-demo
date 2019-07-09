package com.cloud.demo.ribbon.consumer.rxjava;

import rx.Observable;

/**
 * @author : cuixiuyin
 * @date : 2019/7/5
 */
public class Demo2 {

    public static void main(String[] args) {

        // 定义事件源、被观察者
        Observable<String> observable = Observable.just("Hello RxJava");

        // 事件订阅
        observable.subscribe(t -> {
            System.out.println(t);
        });
    }
}
