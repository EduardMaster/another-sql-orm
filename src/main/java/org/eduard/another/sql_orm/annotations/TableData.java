package org.eduard.another.sql_orm.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableData {
    String name() default "";
    int size() default 100;
    boolean unique() default false;
    boolean nullable() default false;
    String defaultValue() default "";
    String sqlType() default "";
    boolean reference() default false;
    boolean primary() default false;
    boolean json() default false;
}
