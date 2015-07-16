UPDATE: This project is brand new and I'm looking for people to help me test out and debug it.  So if you're interested in contributing please post in the forums.

This JDBC Driver will allow you to connect to your Google Docs spreadsheets just as if they were database tables.  It's read-only but you can perform any SQL statements you could with any other database.

Check out the [quickstart doc here](http://code.google.com/p/gdocjdbc/wiki/QuickStartGuide)

Also the [forums here](http://groups.google.com/group/gdocjdbc-discussion-group)

## Why would I use this? ##

If you have an application with a lot of data that changes frequently.  You can manage it ( or have others manage it) within Google Documents and leverage all the features Google Docs provides.

## How does it work? ##
Just connect to it just like you would any Datasource with a JDBC Driver:


`Class.forName("`**org.gdocjdbc.jdbcDriver**`").newInstance();`

`Connection con = DriverManager.getConnection("`**jdbc:gdocjdbc**`", `**_google doc username_**`, `**_google doc password_**`);`

`Statement stmt = con.createStatement();`

`ResultSet rs = stmt.executeQuery("SELECT * FROM MYDOC_SHEETNAME where WHATEVER=3");`

## Behind the scenes ##
When the driver initially connects to your google doc account it will go through all the spreadsheets and create a local in-memory DB.  Each sheet in each spreadsheet is a table.  The cells in the first row of the spreadsheet is used as the column names.  It uses HSQLSB to create the in memory DB.

## What's Missing ##
It should probably be able to use oauth or whatever Google Docs uses besides password authentication.