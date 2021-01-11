package fr.warzou.databaselib.impl.drivers;

import fr.warzou.databaselib.dbl.drivers.Driver;
import fr.warzou.databaselib.dbl.drivers.DriverManager;
import fr.warzou.databaselib.dbl.drivers.Drivers;
import fr.warzou.databaselib.impl.query.AbstractQuery;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SimpleDriverManager implements DriverManager {

    private final Map<String, Driver> drivers = new HashMap<>();
    private static final Map<Drivers, Driver> safeDrivers = new HashMap<>();

    static {
        for (Drivers value : Drivers.values()) {
            safeDrivers.put(value, new WSafeDriver(value));
        }
    }

    @Override
    public void add(String name, String driverClassPath, String driverUrlPart, AbstractQuery query) {
        Driver driver = new WDriver(name, driverClassPath, driverClassPath, query);
        this.drivers.put(name, driver);
        driver.loadDriverClass();
    }

    @Override
    public Optional<Driver> getDriver(String name) {
        if (!this.drivers.containsKey(name))
            return Optional.empty();
        return Optional.of(this.drivers.get(name));
    }

    @Override
    public Driver getSafeDriver(@NotNull Drivers drivers) {
        return safeDrivers.get(drivers);
    }

    @Override
    public Set<String> getDrivers() {
        return this.drivers.keySet();
    }
}
