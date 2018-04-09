package com.rxbus;

/**
 * Created by mrzhang on 2018/4/2.
 * DES:
 * DATA:2018/4/2
 * UPDATE:
 * Email:690084522@qq.com
 * Autor: mrzhang
 */
public interface IDataBus {
    void register(Object obj);
    void unRegister(Object obj);
    void send(Object obj);

}
