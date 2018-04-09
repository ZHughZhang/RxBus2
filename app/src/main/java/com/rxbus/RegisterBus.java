package com.rxbus;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mrzhang on 2018/4/2.
 * DES:自定义注解，用于标记观察的方法
 * DATA:2018/4/2
 * UPDATE:
 * Email:690084522@qq.com
 * Autor: mrzhang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RegisterBus {


}
