package fr.warzou.databaselib.impl.error.user;

public class NonRegisteredUserException extends Exception {

    public NonRegisteredUserException(String username, String host) {
        super("The user '" + username + "' (" + host + ") is not registered");
    }

}
