package com.cloud.demo.ribbon.consumer.rxjava;

import rx.Observable;
import rx.Subscription;

/**
 * @author : cuixiuyin
 * @date : 2019/7/5
 */
public class Demo4 {

    public static void main(String[] args) {
        Observable<String> observable = Observable.just(1, 2, 3, 4).map(t -> String.valueOf(t));

        Subscription subscribe = observable.subscribe(t -> {
            System.out.println(t);
        });
    }
}
