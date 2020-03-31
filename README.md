# DataBase-stock-information-in-code

This library is in beta 0.0.1, so someone update could come in the future.

# How to register databases
* Register your tables
Go in enum `DataBaseTable` and add new value.
> First parameter is the name of the table, and the second is the list of le columns with type of variables.
 
 `MY_TABLE("my_table", Arrays.asList("my_first_column TEXT", "my_second_column VARCHAR(16)"))`

* Load all tables
On start of your program ypu need to create object `DataBaseAutoLoader`, and then you just need to do `.init()` and `.load()`
> All method of this class return one HashMap with key `DataBaseTable` and value `DataBase`.

Now read the docs.
