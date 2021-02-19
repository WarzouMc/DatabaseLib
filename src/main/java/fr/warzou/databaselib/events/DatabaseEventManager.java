package fr.warzou.databaselib.events;

import fr.warzou.databaselib.events.event.DatabaseEvent;
import fr.warzou.databaselib.events.utils.DatabaseListener;

public interface DatabaseEventManager {

    void registerListener(DatabaseListener databaseListener);

    void unregisterListener(DatabaseListener databaseListener);

    void callEvent(DatabaseEvent databaseEvent);

}
