package fr.warzou.databaselib.impl.event;

import fr.warzou.databaselib.events.DatabaseEventManager;
import fr.warzou.databaselib.events.event.DatabaseEvent;
import fr.warzou.databaselib.events.utils.DatabaseListener;
import fr.warzou.databaselib.events.utils.call.DatabaseEventCaller;
import org.jetbrains.annotations.NotNull;

public class SimpleDatabaseEventManager implements DatabaseEventManager {

    private final DatabaseEventCaller databaseEventCaller;

    public SimpleDatabaseEventManager() {
        this.databaseEventCaller = new SimpleDatabaseEventCaller();
    }

    @Override
    public void registerListener(@NotNull DatabaseListener databaseListener) {
        DatabaseEventHandlerList.registerListener(databaseListener);
    }

    @Override
    public void unregisterListener(@NotNull DatabaseListener databaseListener) {
        DatabaseEventHandlerList.unregisterListener(databaseListener);
    }

    @Override
    public void callEvent(@NotNull DatabaseEvent databaseEvent) {
        this.databaseEventCaller.callEvent(databaseEvent);
    }
}
