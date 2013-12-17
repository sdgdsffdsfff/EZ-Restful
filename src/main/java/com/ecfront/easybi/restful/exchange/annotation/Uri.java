package com.ecfront.easybi.restful.exchange.annotation;

import java.lang.annotation.*;

/**
 * @author gudaoxuri
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Uri {

    String value() default "";

}
