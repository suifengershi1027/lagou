package com.lagou.edu.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Inherited
@Documented
public @interface CustomAutowired {

    boolean value() default true;
}
