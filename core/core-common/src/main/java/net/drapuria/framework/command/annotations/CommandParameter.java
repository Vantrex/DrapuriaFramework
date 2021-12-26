package net.drapuria.framework.command.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface CommandParameter {

    String defaultValue() default "";

    String[] tabCompleteFlags() default {};

    boolean wildcard() default false;

}
