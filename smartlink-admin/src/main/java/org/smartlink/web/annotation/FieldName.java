package org.smartlink.web.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 属性字段名称
 *
 * @author L
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldName {
    boolean doRead() default true;
    String value() ;
}
