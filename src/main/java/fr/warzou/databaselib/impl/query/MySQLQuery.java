package fr.warzou.databaselib.impl.query;

import fr.warzou.databaselib.dbl.db.query.Query;
import fr.warzou.databaselib.dbl.db.query.QueryExecutor;
import fr.warzou.databaselib.dbl.db.query.QueryResult;
import fr.warzou.databaselib.dbl.db.table.DatabaseTable;
import fr.warzou.databaselib.dbl.drivers.Driver;

public class MySQLQuery implements Query {

    private final DatabaseTable databaseTable;
    private final QueryResult result;

    public MySQLQuery(DatabaseTable databaseTable) {
        this.databaseTable = databaseTable;
        this.result = null;
    }

    @Override
    public QueryExecutor executor() {
        return null;
    }

    @Override
    public Driver support() {
        return null;
    }

    @Override
    public boolean isSupport() {
        return true;
    }
}
