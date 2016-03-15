# SimpleDatabaseEngine

#Description
#Overview
You are going to build the first component of a simple database engine in
Java. The required functionalities are creating tables, building indices, storing data, deleting data,
and querying. Following is a description of each aspect of the assignment – numbered in point
format for easy communication between you and the course staff.
#User Tables
1) Each user table will be stored in one or more pages (depending on the size of the table).

2) You are required to use java binary object file (.class) for emulating a page (to avoid having
you work with file system pages, which is not the scope of this course).

3) A single tuple will be stored in a separate object inside the binary file.

4) A file has a predetermined maximum number (N) of rows. For example, if a table has 40000
tuples, and N=200, the table will be stored in 20 binary files.

5) Deletion of data will result in fragmentation within a page; do not worry about that in your
solution.

6) Supported types for a column is one of: java.lang.Integer, java.lang.String, java.lang.Double,
java.lang.Boolean and java.util.Date

7) Each table should have an additional column beside those specified by the user. The additional
column must be called TouchDate and is initialized with the date/time of row insertion and
updated with current date/time every time a row is updated.

#Pages
8) You need to postpone the loading of a page until the tuples in that page are actually needed for
further processing. Note that the purpose of using pages is to avoid loading the entire table’s
content into memory. Hence, it defeats the purpose to load all the pages of all tables upon
program startups.

#Meta-Data File
9) Each user table has meta data associated with it; number of columns, data type of columns,
which column is a key, references between table columns and columns in other tables (to enforce
constraints),...

10) You will need to store the meta-data in a file/data-structure. This structure should have the
following layout:


Table Name, Column Name, Column Type, Key, Indexed, References
For example, if a user creates two tables, Employee and Department, specifying several
attributes with their types, etc... the file will be:

Table Name, Column Name, Column Type, Key, Indexed, References

Employee, ID, java.lang.Integer, True, True, null

Employee, Name, java.lang.String, False, False, null,

Employee, Dept, java.lang.String, False, False, Department.ID

Employee, Start_Date, java.util.Date, False, False, null

Department, ID, java.lang.Integer, True, True, null

Department, Name, java.lang.String, False, False, null

Department, Location, java.lang.String, False, False, null

11) For simplicity, you can store this structure in a single file called metadata.csv. Do not worry
about its size in your solution.

#Indices
12) You are required to use B+ trees to support creating primary and secondary dense indices.
Note that a B+ tree stores in its’ leafs pointers (handles in Java terminology) to the data objects.

13) You are going to implement your own B+ Tree data structure.

14) Once a table is created, you need to create a primary index on the key of that table.

15) You should update existing relevant B+ trees when a tuple is inserted/deleted.

16) If a secondary index is created after a table has been populated, you have no option but to
scan the whole table.

17) Upon application startup; to avoid having to scan all tables to build existing indices, you
should save the index itself to disk and load it when the application starts next time.

18) If a select is executed (by calling selectFromTable method below), and a column is
referenced in the select that has been already indexed, then you should search in the index.

#Main App Class
19) Your main class should be called DBApp and should have the following methods/signature;

public void init( );

public void createTable(String strTableName,
Hashtable<String,String> htblColNameType,
Hashtable<String,String> htblColNameRefs,
String strKeyColName)
throws DBAppException

public void createIndex(String strTableName,
String strColName)
throws DBAppException

public void insertIntoTable(String strTableName,
Hashtable<String,Object> htblColNameValue)
throws DBAppException

public void updateTable(String strTableName,
String strKey,
Hashtable<String,Object> htblColNameValue
)
throws DBAppException

public void deleteFromTable(String strTableName,
Hashtable<String,Object> htblColNameValue,
String strOperator)
throws DBEngineException

public Iterator selectFromTable(String strTable,
Hashtable<String,Object> htblColNameValue,
String strOperator)
throws DBEngineException

