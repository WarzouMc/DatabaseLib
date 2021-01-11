package fr.warzou.databaselib.impl.drivers;

import fr.warzou.databaselib.dbl.db.query.Query;
import fr.warzou.databaselib.dbl.db.table.DatabaseTable;
import fr.warzou.databaselib.dbl.drivers.Driver;
import fr.warzou.databaselib.dbl.drivers.Drivers;
import fr.warzou.databaselib.impl.query.MySQLQuery;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class WSafeDriver implements Driver {

    private final Drivers drivers;
    private final String name;
    private final String driverClassPath;
    private final String driverUrlPart;
    private final boolean hasDriverClass;

    WSafeDriver(@NotNull Drivers drivers) {
        this.drivers = drivers;
        this.name = drivers.getName();
        this.driverClassPath = drivers.getDriverClassPath();
        this.driverUrlPart = drivers.getDriverUrlPart();

        boolean hasLoadedClass;
        try {
            Class.forName(this.driverClassPath);
            hasLoadedClass = true;
        } catch (ClassNotFoundException ignore) {
            hasLoadedClass = false;
        }
        this.hasDriverClass = hasLoadedClass;
    }

    @Override
    public void loadDriverClass() {}

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDriverClassPath() {
        return this.driverClassPath;
    }

    @Override
    public String getDriverUrlPart() {
        return "jdbc" + this.driverUrlPart;
    }

    @Override
    public Optional<Query> getQuery(@NotNull DatabaseTable table) {
        switch (this.drivers) {
            case MYSQL:
                return Optional.of(new MySQLQuery(table));
            case SQLITE:
                return Optional.empty();
            case MARIADB:
                return Optional.empty();
            case POSTGRESQL:
                return Optional.empty();
            default:
                return Optional.empty();
        }
    }

    @Override
    public boolean hasDriverClass() {
        return this.hasDriverClass;
    }

}
