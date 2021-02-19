package fr.warzou.test;

import fr.warzou.databaselib.DatabaseLib;
import fr.warzou.databaselib.dbl.db.columns.Column;
import fr.warzou.databaselib.dbl.db.columns.ColumnBuilder;
import fr.warzou.databaselib.dbl.db.columns.properties.ColumnProperties;
import fr.warzou.databaselib.dbl.db.database.Database;
import fr.warzou.databaselib.dbl.drivers.Drivers;
import fr.warzou.databaselib.dbl.user.User;
import fr.warzou.databaselib.events.event.connection.ConnectDatabaseEvent;
import fr.warzou.databaselib.events.event.connection.DisconnectDatabaseEvent;
import fr.warzou.databaselib.events.utils.DatabaseEventHandler;
import fr.warzou.databaselib.events.utils.DatabaseListener;
import fr.warzou.databaselib.impl.data.datatype.string.CharData;
import fr.warzou.databaselib.impl.error.database.IncompatibleUserHostException;
import fr.warzou.databaselib.impl.error.database.NoConnectedDatabaseException;
import fr.warzou.databaselib.impl.error.database.TooManyConnectionOnDatabaseException;
import fr.warzou.databaselib.impl.error.user.NonRegisteredUserException;
import fr.warzou.databaselib.utils.DatabaseLibSessionAccess;
import fr.warzou.databaselib.utils.DatabaseUtils;

import java.util.Optional;

public class DatabaseInternalMain {

    public static void main(String[] args) {
        DatabaseLib databaseLib = new DatabaseLib(DatabaseLibSessionAccess.PRIVATE);
        databaseLib.getDatabaseEventManager().registerListener(new Test());
        databaseLib.getUsersManager().registerUser("root", "root", "localhost:3308");
        Database database = null;
        try {
            Optional<User> optionalUser = databaseLib.getUsersManager().getUser("root", "localhost:3308");
            if (!optionalUser.isPresent())
                return;
            database = databaseLib.addDatabase(optionalUser.get(), "skyexpander", "localhost:3308", "UTC", true,
                    databaseLib.getDriverManager().getSafeDriver(Drivers.MYSQL));
            database.connect();
        } catch (TooManyConnectionOnDatabaseException | IncompatibleUserHostException | NonRegisteredUserException exception) {
            exception.printStackTrace();
        }

        if (database == null)
            return;

        System.out.println("camarche ?");

        ColumnBuilder<CharData> columnBuilder = DatabaseUtils.createColumnBuilder("test", CharData.class);
        Column<CharData> column = columnBuilder.withDefaultValueOption(ColumnProperties.NULL_DEFAULT_VALUE)
                .withNullable(true)
                .build();

        CharData data = new CharData((short) 20);

        String s = database.customQuery("SELECT * FROM data_user WHERE custom_uuid=" +
                    "'3ba90273-6fbc-4e90-a50d-35708ccc0087'", String.class);
            System.out.println(s);

        try {
            database.disconnect();
        } catch (NoConnectedDatabaseException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(8*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("end...");
    }

    public static class Test implements DatabaseListener {

        @DatabaseEventHandler
        public void onDatabaseConnect(ConnectDatabaseEvent event) {
            event.getDatabase().printMessage("Bonjour tt le monde");
        }

        @DatabaseEventHandler
        public void onDatabaseDisconnect(DisconnectDatabaseEvent event) {
            event.getDatabase().printMessage("En revoir tt le monde");
        }

    }

}
