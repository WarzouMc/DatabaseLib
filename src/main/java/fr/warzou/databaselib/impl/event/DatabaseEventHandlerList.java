package fr.warzou.databaselib.impl.event;

import fr.warzou.databaselib.events.utils.DatabaseEventHandler;
import fr.warzou.databaselib.events.utils.DatabaseListener;
import fr.warzou.databaselib.impl.error.event.DatabaseInvalidEventMethodException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

public final class DatabaseEventHandlerList {

    private static final Collection<DatabaseEventMethod> handlerList = new ArrayList<>();

    protected static void registerListener(@NotNull DatabaseListener databaseListener) {
        if (containListener(databaseListener))
            return;
        Method[] methods = databaseListener.getClass().getMethods();
        for (Method method : methods) {
            if (method.isSynthetic() || method.isBridge())
                continue;

            DatabaseEventHandler databaseEventHandler = method.getAnnotation(DatabaseEventHandler.class);
            if (databaseEventHandler == null)
                continue;

            DatabaseEventMethod databaseEventMethod = null;
            try {
                databaseEventMethod = new DatabaseEventMethod(databaseListener, method);
            } catch (DatabaseInvalidEventMethodException databaseInvalidEventMethodException) {
                databaseInvalidEventMethodException.printStackTrace();
            }
            if (databaseEventMethod == null)
                return;

            handlerList.add(databaseEventMethod);
        }
    }

    protected static void unregisterListener(@NotNull DatabaseListener databaseListener) {
        if (!containListener(databaseListener))
            return;
        handlerList.removeIf(databaseEventMethod -> databaseEventMethod.getListener().getClass().getName()
                .equals(databaseListener.getClass().getName()));
    }

    private static boolean containListener(DatabaseListener databaseListener) {
        return handlerList.stream().anyMatch(databaseEventMethod -> databaseEventMethod.getListener().getClass().getName()
                .equals(databaseListener.getClass().getName()));
    }

    protected static Collection<DatabaseEventMethod> handlerList() {
        return handlerList;
    }

}
