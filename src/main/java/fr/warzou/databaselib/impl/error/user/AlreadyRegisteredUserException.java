package fr.warzou.databaselib.impl.error.user;

import fr.warzou.databaselib.dbl.user.User;

public class AlreadyRegisteredUserException extends Exception {

    public AlreadyRegisteredUserException(User user) {
        System.out.println("The user '" + user.getUsername() + "' (" + user.getHost() + ") is already registered");
    }

}
