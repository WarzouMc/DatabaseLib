package fr.warzou.databaselib.impl.event;

import fr.warzou.databaselib.events.event.DatabaseEvent;
import fr.warzou.databaselib.events.utils.call.DatabaseEventCaller;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleDatabaseEventCaller implements DatabaseEventCaller {

    private final DatabaseEventHandlerList databaseEventHandlerList;

    public SimpleDatabaseEventCaller(DatabaseEventHandlerList handlerList) {
        this.databaseEventHandlerList = handlerList;
    }

    @Override
    public void callEvent(DatabaseEvent event) {
        Collection<DatabaseEventMethod> handlerList = this.databaseEventHandlerList.handlerList();
        List<DatabaseEventMethod> targetMethods = handlerList.stream()
                .filter(databaseEventMethod -> event.getClass().isAssignableFrom(databaseEventMethod.getTargetEvent()))
                .sorted((o1, o2) -> Integer.compare(o2.getPriority().power(), o1.getPriority().power()))
                .collect(Collectors.toList());
        Collections.reverse(targetMethods);
        targetMethods.forEach(method -> method.invoke(event));
    }
}
