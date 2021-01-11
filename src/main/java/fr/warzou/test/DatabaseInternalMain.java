package fr.warzou.test;

import fr.warzou.databaselib.DatabaseLib;
import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.data.builder.SQLDataBuilder;
import fr.warzou.databaselib.dbl.data.subdata.string.StringData;
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
import fr.warzou.databaselib.impl.error.database.TooManyConnectionOnDatabaseException;
import fr.warzou.databaselib.impl.error.user.NonRegisteredUserException;

import java.util.Optional;

public class DatabaseInternalMain {

    public static void main(String[] args) {
        DatabaseLib.getEventManager().registerListener(new Test());
        DatabaseLib.getUsersManager().registerUser("root", "root", "localhost:3308");
        Database database = null;
        try {
            Optional<User> optionalUser = DatabaseLib.getUsersManager().getUser("root", "localhost:3308");
            if (!optionalUser.isPresent())
                return;
            database = DatabaseLib.addDatabase(optionalUser.get(), "skyexpander", "localhost:3308", "UTC", true,
                    DatabaseLib.getDriverManager().getSafeDriver(Drivers.MYSQL));
            database.connect();
        } catch (TooManyConnectionOnDatabaseException | IncompatibleUserHostException | NonRegisteredUserException exception) {
            exception.printStackTrace();
        }

        if (database == null)
            return;

        System.out.println("camarche ?");

        ColumnBuilder<CharData> columnBuilder = DatabaseLib.createColumnBuilder("test", CharData.class);
        Column<CharData> column = columnBuilder.withDefaultValueOption(ColumnProperties.NULL_DEFAULT_VALUE)
                .withNullable(true)
                .build();

        System.out.println(column.getName() + " " + column.getDefault());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(5 * 1001);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String s = database.customQuery("SELECT * FROM data_user WHERE custom_uuid=" +
                "'3ba90273-6fbc-4e90-a50d-35708ccc0087'", String.class);
        /*String s = database.customQuery("SELECT * FROM data_user WHERE custom_uuid=" +
                "'uuid'", String.class);*/
        System.out.println(s);
        CharData data = new CharData();
        SQLDataBuilder<String, CharData> sqlData = data.createSQLData();
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
