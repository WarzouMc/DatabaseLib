package fr.warzou.databaselib.dbl.db.columns.properties;

public final class ColumnProperties {

    public static final int SELF_DEFAULT_VALUE = 0;
    public static final int NULL_DEFAULT_VALUE = 1;
    public static final int CURRENT_TIMESTAMP_DEFAULT_VALUE = 2;


    public static final int ANY_ATTRIBUTES = 0;
    public static final int BINARY = 1;
    public static final int UNSIGNED = 2;
    public static final int UNSIGNED_ZEROFILL = 3;
    public static final int CURRENT_TIMESTAMP_ON_UPDATE = 4;


    public static final int VIRTUAL = 1;
    public static final int STORED = 2;


    public static final int DEFAULT_FORMAT = 0;
    public static final int FIXED_FORMAT = 1;
    public static final int DYNAMIC_FORMAT = 2;

}
