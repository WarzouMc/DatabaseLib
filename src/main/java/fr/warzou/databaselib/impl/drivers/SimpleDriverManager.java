package fr.warzou.databaselib.impl.drivers;

import fr.warzou.databaselib.dbl.drivers.Driver;
import fr.warzou.databaselib.dbl.drivers.DriverManager;
import fr.warzou.databaselib.dbl.drivers.Drivers;
import fr.warzou.databaselib.impl.query.AbstractQuery;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SimpleDriverManager implements DriverManager {

    private final Map<String, Driver> drivers = new HashMap<>();
    private final Map<Drivers, Driver> safeDrivers = new HashMap<>();

    public SimpleDriverManager() {
        Arrays.asList(Drivers.values()).forEach(drivers -> safeDrivers.put(drivers, new WSafeDriver(drivers)));
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
