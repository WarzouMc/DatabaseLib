package fr.warzou.databaselib.dbl.drivers;

import fr.warzou.databaselib.dbl.db.query.Query;
import fr.warzou.databaselib.dbl.db.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface Driver {

    void loadDriverClass();

    String getName();

    String getDriverClassPath();

    String getDriverUrlPart();

    Optional<? extends Query> getQuery(DatabaseTable table);

    boolean hasDriverClass();

}
