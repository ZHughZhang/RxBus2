package com.rxbus;

import android.annotation.SuppressLint;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;

/**
 * Created by mrzhang on 2018/4/2.
 * DES:
 * DATA:2018/4/2
 * UPDATE:
 * Email:690084522@qq.com
 * Autor: mrzhang
 */
public class RxBus2 implements IDataBus {

    private static final String TAG = RxBus2.class.getSimpleName();
    private static volatile  RxBus2 instance;
    private Set<Object> subscribers;

    private RxBus2(){
        subscribers = new CopyOnWriteArraySet<>();
    }


    @SuppressLint("CheckResult")
    public void chainProcess(Function func){
        Observable.just("").subscribeOn(Schedulers.io())
                .map(func)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object data) {
                        for (Object obj : subscribers) {
                            callMethodByAnnotiation(obj,data);
                        }
                    }
                });
    }


    @Override
    public synchronized void register(Object obj) {
            subscribers.add(obj);
    }

    @Override
    public synchronized void unRegister(Object obj) {
            subscribers.remove(obj);
    }

    @Override
    public  void send(final Object obj) {
        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                Log.e(TAG,"call in tread:"+ Thread.currentThread().getName());
                emitter.onNext(obj);
            }
        });
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object data) {
                        for (Object obj : subscribers) {
                            callMethodByAnnotiation(obj,data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public static synchronized RxBus2 getInstance(){
        if (instance == null){
            synchronized (RxBus2.class){
                if (instance == null){
                    instance = new RxBus2();
                }
            }
        }
        return instance;
    }

    private void callMethodByAnnotiation(Object target,Object data){
        Method[] methofArray = target.getClass().getDeclaredMethods();
        for (int i = 0; i < methofArray.length; i++) {
            try {
                if (methofArray[i].isAnnotationPresent(RegisterBus.class)){
                    Class paramType = methofArray[i].getParameterTypes()[0];
                    if (data.getClass().getName().equals(paramType.getName())){
                        methofArray[i].invoke(target,new Object[]{data});
                    }
                }
            } catch (IllegalAccessException mE) {
                mE.printStackTrace();
            } catch (InvocationTargetException mE) {
                mE.printStackTrace();
            }
        }
    }

}
