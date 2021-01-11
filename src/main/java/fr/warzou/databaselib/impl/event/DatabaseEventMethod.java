package fr.warzou.databaselib.impl.event;

import fr.warzou.databaselib.events.event.DatabaseEvent;
import fr.warzou.databaselib.events.utils.DatabaseEventHandler;
import fr.warzou.databaselib.events.utils.DatabaseListener;
import fr.warzou.databaselib.impl.error.event.DatabaseInvalidEventMethodException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DatabaseEventMethod {

    private final Method method;

    private Class<? extends DatabaseEvent> targetEvent;
    private final DatabaseListener listener;
    private DatabaseEventHandler.DatabaseEventPriority priority;

    protected DatabaseEventMethod(@NotNull DatabaseListener databaseListener, @NotNull Method method) throws DatabaseInvalidEventMethodException {
        this.listener = databaseListener;
        this.method = method;
        buildParameters();
    }

    private void buildParameters() throws DatabaseInvalidEventMethodException {
        if (this.method.getParameterCount() != 1)
            throw new DatabaseInvalidEventMethodException("Methods with the \"@DatabaseEventHandler\" annotation must have only one argument.\n" +
                    "Methode name : " + this.method.getName() + "\n" +
                    "Class name : " + this.listener.getClass().getName());

        Class<?> parameters = this.method.getParameterTypes()[0];
        if (!DatabaseEvent.class.isAssignableFrom(parameters))
            throw new DatabaseInvalidEventMethodException(parameters.getName() + " is not a DatabaseEvent.\n" +
                    "Methode name : " + this.method.getName() + "\n" +
                    "Class name : " + this.listener.getClass().getName());

        this.targetEvent = parameters.asSubclass(DatabaseEvent.class);
        DatabaseEventHandler databaseEventHandler = this.method.getAnnotation(DatabaseEventHandler.class);
        this.priority = databaseEventHandler.priority();
    }

    protected Method getMethod() {
        return this.method;
    }

    protected final Class<? extends DatabaseEvent> getTargetEvent() {
        return this.targetEvent;
    }

    protected DatabaseListener getListener() {
        return this.listener;
    }

    protected DatabaseEventHandler.DatabaseEventPriority getPriority() {
        return this.priority;
    }

    protected void invoke(DatabaseEvent databaseEvent) {
        if (!databaseEvent.getClass().isAssignableFrom(this.targetEvent))
            return;
        this.method.setAccessible(true);
        try {
            this.method.invoke(this.listener, databaseEvent);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        this.method.setAccessible(false);
    }
}
