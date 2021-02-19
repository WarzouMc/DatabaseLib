package fr.warzou.databaselib.dbl.db.query;

import fr.warzou.databaselib.dbl.drivers.Driver;

public interface Query {

    QueryExecutor executor();

    Driver support();

    boolean isSupport();

}
