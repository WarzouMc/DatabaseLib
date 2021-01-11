package fr.warzou.databaselib.dbl.drivers;

import java.util.Arrays;
import java.util.Optional;

public enum Drivers {

    MYSQL("MySQL", "com.mysql.cj.jdbc.Driver", ":mysql:"),
    SQLITE("SQLite", "org.sqlite.JDBC", ":sqlite:"),
    POSTGRESQL("Postrges", "org.postgresql.Driver", ":postgresql:"),
    MARIADB("MariaDB", "org.mariadb.jdbc.Driver", ":mariadb:");


    private final String name;
    private final String driverPath;
    private final String driverUrlPart;

    Drivers(String name, String driverPath, String driverUrlPart) {
        this.name = name;
        this.driverPath = driverPath;
        this.driverUrlPart = driverUrlPart;
    }

    public static Optional<Drivers> fromName(String name) {
        return Arrays.stream(values()).filter(drivers -> drivers.getName().equals(name)).findFirst();
    }

    public static Optional<Drivers> fromDriverPath(String driverPath) {
        return Arrays.stream(values()).filter(drivers -> drivers.getDriverClassPath().equals(driverPath)).findFirst();
    }

    public static Optional<Drivers> fromDriverUrlPart(String driverUrlPart) {
        return Arrays.stream(values()).filter(drivers -> drivers.getDriverUrlPart().equals(driverUrlPart)).findFirst();
    }

    public String getDriverClassPath() {
        return this.driverPath;
    }

    public String getName() {
        return this.name;
    }

    public String getDriverUrlPart() {
        return this.driverUrlPart;
    }
}
