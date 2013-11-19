package com.ecfront.easybi.restful.exchange.annotation;

import java.lang.annotation.*;

/**
 * 
 * @author gudaoxuri
 */

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Get {

	String value();

}
