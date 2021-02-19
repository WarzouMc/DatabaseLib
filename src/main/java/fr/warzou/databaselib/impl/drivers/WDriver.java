package fr.warzou.databaselib.impl.drivers;

import fr.warzou.databaselib.dbl.db.query.Query;
import fr.warzou.databaselib.dbl.db.table.DatabaseTable;
import fr.warzou.databaselib.dbl.drivers.Driver;
import fr.warzou.databaselib.impl.query.AbstractQuery;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

class WDriver implements Driver {

    private final String name;
    private final String driverClassPath;
    private final String driverUrlPath;
    private final AbstractQuery query;

    private boolean hasDriverClass;

    WDriver(String name, String driverClassPath, String driverUrlPath, AbstractQuery query) {
        this.name = name;
        this.driverClassPath = driverClassPath;
        this.driverUrlPath = driverUrlPath;
        this.query = query;
    }

    @Override
    public void loadDriverClass() {
        try {
            Class.forName(this.driverClassPath);
            this.hasDriverClass = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            this.hasDriverClass = false;
        }
    }

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
        return "jdbc" + this.driverUrlPath;
    }

    @Override
    public Optional<? extends Query> getQuery(@NotNull DatabaseTable table) {
        return this.query.of(table);
    }

    @Override
    public boolean hasDriverClass() {
        return this.hasDriverClass;
    }
}
