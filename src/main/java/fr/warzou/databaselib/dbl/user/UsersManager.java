package fr.warzou.databaselib.dbl.user;

import fr.warzou.databaselib.impl.error.user.NonRegisteredUserException;

import java.util.Optional;

public interface UsersManager {

    User registerUser(String username, String password, String host);

    void unregisterUser(User user) throws NonRegisteredUserException;

    Optional<User> getUser(String username, String host);

    boolean isRegistered(String username, String host);

}
