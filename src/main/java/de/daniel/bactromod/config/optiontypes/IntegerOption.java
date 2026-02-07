package de.daniel.bactromod.config.optiontypes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface IntegerOption {
    int intMin() default Integer.MIN_VALUE;
    int intMax() default Integer.MAX_VALUE;
}
