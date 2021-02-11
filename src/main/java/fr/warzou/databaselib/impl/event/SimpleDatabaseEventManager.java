package fr.warzou.databaselib.impl.event;

import fr.warzou.databaselib.events.DatabaseEventManager;
import fr.warzou.databaselib.events.event.DatabaseEvent;
import fr.warzou.databaselib.events.utils.DatabaseListener;
import fr.warzou.databaselib.events.utils.call.DatabaseEventCaller;
import org.jetbrains.annotations.NotNull;

public class SimpleDatabaseEventManager implements DatabaseEventManager {

    private final DatabaseEventCaller databaseEventCaller;
    private final DatabaseEventHandlerList databaseEventHandlerList;

    public SimpleDatabaseEventManager() {
        this.databaseEventHandlerList = new DatabaseEventHandlerList();
        this.databaseEventCaller = new SimpleDatabaseEventCaller(this.databaseEventHandlerList);
    }

    @Override
    public void registerListener(@NotNull DatabaseListener databaseListener) {
        this.databaseEventHandlerList.registerListener(databaseListener);
    }

    @Override
    public void unregisterListener(@NotNull DatabaseListener databaseListener) {
        this.databaseEventHandlerList.unregisterListener(databaseListener);
    }

    @Override
    public void callEvent(@NotNull DatabaseEvent databaseEvent) {
        this.databaseEventCaller.callEvent(databaseEvent);
    }
}
