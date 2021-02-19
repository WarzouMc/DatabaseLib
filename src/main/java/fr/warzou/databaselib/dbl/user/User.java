package fr.warzou.databaselib.dbl.user;

import fr.warzou.databaselib.dbl.db.database.Database;

import java.util.Optional;

public interface User {

    String getUsername();

    String getPassword();

    String getHost();

    Optional<Database> getDatabase(String name, String host);

}
