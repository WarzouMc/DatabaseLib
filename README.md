[![Discord](https://img.shields.io/discord/577196219252604942.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/5xQPmD2)

# DatabaseLib stock information in code

This library is in 1.0.0, so someone update could come in the future.
You need dbcp2 to use this lib.

# How to register databases
* Register your tables
You must use the DatabaseTable#add(Object id, AbstractDatabaseTables abstractDatabaseTables)

* Load all tables

`

DatabaseAutoLoader dataBaseAutoLoader = new DatabaseAutoLoader();
        
dataBaseAutoLoader.init();
        
dataBaseAutoLoader.load(long save);

`

> All method of this class return one HashMap with key `DatabaseTable` and value `Database`.

Now read the docs.


Support on discord will the wiki is not present.
