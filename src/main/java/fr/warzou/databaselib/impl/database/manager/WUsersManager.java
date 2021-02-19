package fr.warzou.databaselib.impl.database.manager;

import fr.warzou.databaselib.dbl.user.User;
import fr.warzou.databaselib.dbl.user.UsersManager;
import fr.warzou.databaselib.impl.database.WDatabase;
import fr.warzou.databaselib.impl.database.WUser;
import fr.warzou.databaselib.impl.error.user.AlreadyRegisteredUserException;
import fr.warzou.databaselib.impl.error.user.NonRegisteredUserException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WUsersManager implements UsersManager {

    private final Map<String[], User> users = new HashMap<>();

    @Override
    public User registerUser(String username, String password, String host) {
        try {
            tryToAddUser(username, host);
            User user = new WUser(username, password, host);
            users.put(new String[] {username, host}, user);
            return user;
        } catch (AlreadyRegisteredUserException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void unregisterUser(User user) throws NonRegisteredUserException {
        if (!getUser(user.getUsername(), user.getHost()).isPresent())
            throw new NonRegisteredUserException(user.getUsername(), user.getHost());
        ((WUser) user).getLinkedDatabases().forEach(database -> ((WDatabase) database).makeUnusable());
        Optional<String[]> optionalKey = this.users.entrySet().stream()
                .filter(userEntry -> userEntry.getKey()[0].equals(user.getUsername()) && userEntry.getKey()[1].equals(user.getHost()))
                .findFirst()
                .map(Map.Entry::getKey);
        if (!optionalKey.isPresent())
            return;
        String[] key = optionalKey.get();
        this.users.remove(key);
    }

    @Override
    public Optional<User> getUser(String username, String host) {
        return this.users.entrySet().stream()
                .filter(userEntry -> userEntry.getKey()[0].equals(username) && userEntry.getKey()[1].equals(host))
                .findFirst()
                .map(Map.Entry::getValue);
    }

    @Override
    public boolean isRegistered(String username, String host) {
        return getUser(username, host).isPresent();
    }

    private void tryToAddUser(String username, String host) throws AlreadyRegisteredUserException {
        if (getUser(username, host).isPresent())
            throw new AlreadyRegisteredUserException(getUser(username, host).get());
    }
}
