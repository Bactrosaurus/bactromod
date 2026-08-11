package de.daniel.bactromod.config.optiontypes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IntegerOption {
    int intMin() default Integer.MIN_VALUE;
    int intMax() default Integer.MAX_VALUE;
}
