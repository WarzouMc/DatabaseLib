package fr.warzou.databaselib.events.utils.call;

import fr.warzou.databaselib.events.event.DatabaseEvent;

public interface DatabaseEventCaller {

    void callEvent(DatabaseEvent event);

}
