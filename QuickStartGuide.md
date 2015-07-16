## Making a connection and running a SELECT statement ##
Download the package and unzip it.  Make sure you have at least Java 1.5 and Ant in your path.

Create or login to your google doc account and click "Upload" in the top left.  Upload the file doc/Simpsons Characters.csv.

then run the default ant task:
ant

enter the username/password when prompted, if you get a log exception you probably entered this information incorrectly.

It will prompt you for what SQL you want to run, just hit enter to except the default.

It will then make a JDBC connection using this driver, initialize the local cache of the data and using the standard ant SQL task run the select statement and print the result.

## Using the driver in your own Java code ##
You would use this driver just like any other JDBC Driver:

Class.forName("org.gdocjdbc.jdbcDriver").newInstance();

Connection con = DriverManager.getConnection("jdbc:gdocjdbc", username, password);

Statement stmt = con.createStatement();

ResultSet rs = stmt.executeQuery("SELECT `*` FROM MYDOC\_SHEETNAME where WHATEVER=3");

while (rs.next()) {

//do something with the results

}