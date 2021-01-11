package fr.warzou.databaselib.impl.logger;

import fr.warzou.databaselib.dbl.user.User;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.*;

public class DatabaseLogger extends Logger {

    private final long CREATE_DATE = new Date().getTime();
    private String name;

    private boolean loggable = true;

    public DatabaseLogger(User user, String databaseName, String host) {
        super("DatabaseLib-" + user.getUsername() + "/" + databaseName + "/" + host, null);
        boolean isCreated = createFile(user.getUsername(), databaseName, host);

        databaseName = host.replace(".", "-").replace(":", "_") + "-" + databaseName;

        if (!isCreated) {
            System.err.println("DatabaseLib cannot create the log file for '" + databaseName + "'");
            return;
        }

        File logFile = new File("databaselib/logs/" + databaseName + "/" + user.getUsername() + "/log-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
                .format(this.CREATE_DATE) + ".txt");
        try {
            Handler handler = new FileHandler(logFile.getPath());
            handler.setFormatter(new DatabaseLoggerFormat(databaseName));
            addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new File(logFile.getPath() + ".lck").delete();
        setUseParentHandlers(false);

        this.name = databaseName;

        logMessage(new LogRecord(Level.INFO, "Logger for '" + databaseName + "' is now created"));
    }

    private boolean createFile(String username, String databaseName, String host) {
        boolean createSourceFolder;
        boolean createLogFolder;
        boolean createDatabaseLogFolder;
        boolean createUserFolder;
        boolean createDatabaseLogFile = false;
        databaseName = host.replace(".", "-").replace(":", "_") + "-" + databaseName;
        File sourceFolder = new File("databaselib");
        if (!(createSourceFolder = sourceFolder.exists()))
            createSourceFolder = sourceFolder.mkdir();

        File logFolder = new File("databaselib/logs");
        if (!(createLogFolder = logFolder.exists()) && createSourceFolder)
            createLogFolder = logFolder.mkdir();

        File databaseLogFolder = new File("databaselib/logs/" + databaseName);
        if (!(createDatabaseLogFolder = databaseLogFolder.exists()) && createLogFolder)
            createDatabaseLogFolder = databaseLogFolder.mkdir();

        File userFolder = new File("databaselib/logs/" + databaseName + "/" + username);
        if (!(createUserFolder = userFolder.exists()) && createDatabaseLogFolder)
            createUserFolder = userFolder.mkdir();

        File databaseLogFile = new File("databaselib/logs/" + databaseName + "/" + username + "/log-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
                .format(this.CREATE_DATE) + ".txt");

        if (createUserFolder)
            try {
                createDatabaseLogFile = databaseLogFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return createDatabaseLogFile;
    }

    public void logMessage(LogRecord logRecord) {
        if (!this.loggable)
            return;
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(logRecord.getLevel());
        handler.setFormatter(new DatabaseLoggerFormat(this.name));
        addHandler(handler);

        log(logRecord);
        removeHandler(handler);
    }

    public void logError(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        logMessage(new LogRecord(Level.SEVERE, stringWriter.toString()));
    }

    protected void close() {
        logMessage(new LogRecord(Level.INFO, getName() + " logger is now closed"));
        Handler[] handlers = getHandlers().clone();
        Arrays.asList(handlers).forEach(this::removeHandler);
        this.loggable = false;
    }

    private static class DatabaseLoggerFormat extends Formatter {

        private final String name;

        public DatabaseLoggerFormat(String name) {
            this.name = name;
        }

        @Override
        public String format(LogRecord record) {
            return "[" +
                    new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(record.getMillis()) +
                    " " + name +
                    "] " +
                    "[" +
                    record.getLevel() +
                    "] " +
                    record.getMessage() +
                    "\n";
        }
    }

}
