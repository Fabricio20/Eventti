package io.github.fabricio20.eventti;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {

    boolean ignoreCancelled() default false;

    ListenerPriority priority() default ListenerPriority.NORMAL;

}
