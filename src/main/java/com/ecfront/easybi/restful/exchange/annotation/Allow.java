package com.ecfront.easybi.restful.exchange.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Allow {

    String value();

}
