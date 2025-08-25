package de.daniel.bactromod.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigOption {
    int intMin() default Integer.MIN_VALUE;
    int intMax() default Integer.MAX_VALUE;
    boolean boolDefault() default false;
    int intDefault() default 0;
}
