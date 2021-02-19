package fr.warzou.databaselib.events.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseEventHandler {

    DatabaseEventPriority priority() default DatabaseEventPriority.LOW;

    enum DatabaseEventPriority {
        HIGH, MEDIUM, LOW;

        public int power() {
            return ordinal();
        }
    }
}
