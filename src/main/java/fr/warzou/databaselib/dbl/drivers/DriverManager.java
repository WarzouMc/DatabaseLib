package fr.warzou.databaselib.dbl.drivers;

import fr.warzou.databaselib.impl.query.AbstractQuery;

import java.util.Optional;
import java.util.Set;

public interface DriverManager {

    void add(String name, String driverClassPath, String driverUrlPart, AbstractQuery query);

    Optional<Driver> getDriver(String name);

    Driver getSafeDriver(Drivers drivers);

    Set<String> getDrivers();

}
