package UnFound;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

class DBAppException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DBAppException(String x) {
		super(x);
	}

}

class DBEngineException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DBEngineException() {
		super();
	}

}

public class DBApp {

	public static Page readPage(String tableName, String id) throws Exception {
		FileInputStream x = new FileInputStream("classes/UnFound/" + tableName
				+ id + ".class");
		ObjectInputStream y = new ObjectInputStream(x);
		Page z = (Page) y.readObject();
		x.close();
		y.close();
		return z;
	}

	public static void writePage(String tableName, String id, Page p)
			throws Exception {

		FileOutputStream x = new FileOutputStream("classes/UnFound/"
				+ tableName + id + ".class");
		ObjectOutputStream y = new ObjectOutputStream(x);
		y.writeObject(p);
		x.close();
		y.close();
	}

	public static ArrayList<String[]> readMetaData() throws Exception {
		ArrayList<String[]> data = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(
				"data/metadata.csv"));
		String line = "";
		while ((line = br.readLine()) != null) {
			data.add(line.split(","));
		}
		br.close();
		return data;
	}

	public static void writeMetaData(String strTableName,
			Hashtable<String, String> htblColNameType,
			Hashtable<String, String> htblColNameRefs, String strKeyColName)
			throws Exception {

		// Table Name, Column Name, Column Type, Key, Indexed, References
		Enumeration<String> e = htblColNameType.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			FileWriter br = new FileWriter("data/metadata.csv", true);
			br.append(strTableName + ",");
			br.append(key + ",");

			if (!(htblColNameType.get(key).equals("String")
					|| htblColNameType.get(key).equals("Integer")
					|| htblColNameType.get(key).equals("Double")
					|| htblColNameType.get(key).equals("Boolean") || htblColNameType
					.get(key).equals("Date"))) {
				br.flush();
				br.close();
				throw new DBAppException("Wrong Formate");
			}

			if (htblColNameType.get(key).equals("Date"))
				br.append("java.util" + htblColNameType.get(key) + ",");
			else
				br.append("java.lang" + htblColNameType.get(key) + ",");

			if (key.equals(strKeyColName))
				br.append("True,");
			else
				br.append("False,");
			br.append("False,");

			String b = "null";
			Enumeration<String> w = htblColNameRefs.keys();
			while (w.hasMoreElements()) {
				String k = (String) w.nextElement();
				if (k.equals(key))
					b = htblColNameRefs.get(k);
			}

			br.append(b);
			br.append('\n');
			br.flush();
			br.close();
		}

	}

	@SuppressWarnings("unchecked")
	public static void writeTableData(String strTableName, String strKeyColName)
			throws IOException {
		FileInputStream x = null;
		ObjectInputStream y = null;
		ArrayList<String[]> z = null;
		try {
			x = new FileInputStream("classes/UnFound/Tables.class");
			y = new ObjectInputStream(x);
			z = (ArrayList<String[]>) y.readObject();
		} catch (Exception e) {
			z = new ArrayList<>();
		}
		String[] k = { strTableName, "0", strKeyColName };
		z.add(k);
		FileOutputStream a = new FileOutputStream(
				"classes/UnFound/Tables.class");
		ObjectOutputStream b = new ObjectOutputStream(a);
		b.writeObject(z);
		a.close();
		b.close();
	}

	public static String getStrKey(String strTableName) throws Exception {
		FileInputStream x = new FileInputStream("classes/UnFound/Tables.class");
		ObjectInputStream y = new ObjectInputStream(x);
		@SuppressWarnings("unchecked")
		ArrayList<String[]> z = (ArrayList<String[]>) y.readObject();
		for (int i = 0; i < z.size(); i++) {
			if (z.get(i)[0].equals(strTableName)) {
				x.close();
				y.close();
				return z.get(i)[2];
			}
		}
		x.close();
		y.close();
		return "";
	}

	public static void editNoPages(String name, String n) throws Exception {
		FileInputStream x = new FileInputStream("classes/UnFound/Tables.class");
		ObjectInputStream y = new ObjectInputStream(x);
		@SuppressWarnings("unchecked")
		ArrayList<String[]> z = (ArrayList<String[]>) y.readObject();
		for (int i = 0; i < z.size(); i++) {
			if (z.get(i)[0].equals(name))
				z.get(i)[1] = n + "";
		}
		x.close();
		y.close();
		FileOutputStream a = new FileOutputStream(
				"classes/UnFound/Tables.class");
		ObjectOutputStream b = new ObjectOutputStream(a);
		b.writeObject(z);
		a.close();
		b.close();
	}

	public static int noPages(String name) throws Exception {

		FileInputStream x = new FileInputStream("classes/UnFound/Tables.class");
		ObjectInputStream y = new ObjectInputStream(x);
		@SuppressWarnings("unchecked")
		ArrayList<String[]> z = (ArrayList<String[]>) y.readObject();
		for (int i = 0; i < z.size(); i++) {
			if (z.get(i)[0].equals(name)) {
				x.close();
				y.close();
				return Integer.parseInt(z.get(i)[1]);
			}
		}
		x.close();
		y.close();
		return -1;
	}

	@SuppressWarnings("unchecked")
	public static boolean checkDuplicate(String name) throws Exception {

		FileInputStream x = null;
		ObjectInputStream y = null;
		ArrayList<String[]> z = null;
		try {
			x = new FileInputStream("classes/UnFound/Tables.class");
			y = new ObjectInputStream(x);
			z = (ArrayList<String[]>) y.readObject();
		} catch (Exception e) {
			return true;
		}
		for (int i = 0; i < z.size(); i++) {
			if (z.get(i)[0].equals(name)) {
				x.close();
				y.close();
				return false;
			}
		}
		x.close();
		y.close();
		return true;
	}

	public void init() {

	}

	public void createTable(String strTableName,
			Hashtable<String, String> htblColNameType,
			Hashtable<String, String> htblColNameRefs, String strKeyColName)
			throws DBAppException {
		try {
			if (!checkDuplicate(strTableName))
				return;
			writeMetaData(strTableName, htblColNameType, htblColNameRefs,
					strKeyColName);
			writeTableData(strTableName, strKeyColName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createIndex(String strTableName, String strColName)
			throws DBAppException {

	}

	// public static ArrayList<Pair> referencedAttributes(String tableName)
	// throws IOException{
	// ArrayList<Pair> referenced = new ArrayList<>();
	// ArrayList<String []> metaData = readMetaData();
	// for (int i = 0; i < metaData.size(); i++) {
	// if(metaData.get(i)[0].equals(tableName) &&
	// !metaData.get(i)[5].equals("null")) {
	// referenced.add(new Pair(metaData.get(i)[1], metaData.get(i)[5]));
	// }
	// }
	// return referenced;
	// }

	// public static boolean containsValue(String tableName, String key, Object
	// value) {
	// boolean flag = false;
	// // to be completed
	// return flag;
	// }

	// public static boolean checkValidInsert(String tableName,
	// Hashtable<String,Object> htblColNameValue) {
	// boolean flag = false;
	// // to be completed
	// return flag;
	// }

	public void insertIntoTable(String strTableName,
			Hashtable<String, Object> htblColNameValue) {

		// referencing still needs to be handled

		// to insert into a table we need to check the number of pages a table
		// has
		// if a table doesn't have any pages we create a new page and insert the
		// record into it
		// if a table has pages we insert in the last page
		// if the last page is full we create a new page and increment the
		// number of pages of that table
		try {
			htblColNameValue.put("D", "F");
			int n = noPages(strTableName);
			if (n == 0) {
				Page p = new Page();
				p.addRecord(htblColNameValue);
				writePage(strTableName, n + "", p);
				editNoPages(strTableName, (n + 1) + "");
			} else {
				Page p = readPage(strTableName, (n - 1) + "");
				if (!p.isFull()) {
					// ArrayList<Pair> references =
					// referencedAttributes(strTableName);
					// if(references.size() == 0) {
					p.addRecord(htblColNameValue);
					writePage(strTableName, (n - 1) + "", p);
					// }else {
					// Enumeration<String> toBeInserted =
					// htblColNameValue.keys();
					// boolean flag = true;
					// for (int i = 0; i < references.size(); i++) {
					// Pair cur = references.get(i);
					// while(toBeInserted.hasMoreElements()) {
					// String k = (String) toBeInserted.nextElement();
					// if(k.equals(cur.f)) {
					// if(!containsValue(strTableName, k,
					// htblColNameValue.get(k)))
					// {
					// flag = false;
					// throw new DBAppException(k
					// +" is referenced in another table and the value you entered cannot be found");
					// }
					// }
					// }
					// }
					// if(flag) {
					// p.addRecord(htblColNameValue);
					// writePage(strTableName, (n-1)+"", p);
					// }
					// }
				} else {
					Page p1 = new Page();
					p1.addRecord(htblColNameValue);
					writePage(strTableName, n + "", p1);
					editNoPages(strTableName, (n + 1) + "");
				}
			}
		} catch (Exception e) {

		}
	}

	public Iterator<Hashtable<String, Object>> selectFromTable(String strTable,
			Hashtable<String, Object> htblColNameValue, String strOperator) {
		try {
			ArrayList<Hashtable<String, Object>> a = new ArrayList<>();
			int n = noPages(strTable);
			if (strOperator.equals("AND")) {
				for (int i = 0; i < n; i++) {
					Page p = readPage(strTable, i + "");
					for (int j = 0; j < p.page.size(); j++) {
						Enumeration<String> keyValues = htblColNameValue.keys();
						boolean flag1 = true;
						while (keyValues.hasMoreElements()) {
							String k = (String) keyValues.nextElement();
							Enumeration<String> cur = p.page.get(j).keys();
							while (cur.hasMoreElements()) {
								String val = (String) cur.nextElement();
								if (val.equals(k)) {
									Object o1 = p.page.get(j).get(k);
									Object o2 = htblColNameValue.get(k);
									if (!o1.equals(o2)) {
										flag1 = false;
										break;
									}
								}
							}
						}
						if (flag1 && p.page.get(j).get("D").equals("F"))
							a.add(p.page.get(j));
					}
				}
			}
			if (strOperator.equals("OR")) {
				for (int i = 0; i < n; i++) {
					Page p = readPage(strTable, i + "");
					for (int j = 0; j < p.page.size(); j++) {
						Enumeration<String> keyValues = htblColNameValue.keys();
						boolean flag1 = false;
						while (keyValues.hasMoreElements()) {
							String k = (String) keyValues.nextElement();
							Enumeration<String> cur = p.page.get(j).keys();
							while (cur.hasMoreElements()) {
								String val = (String) cur.nextElement();
								if (val.equals(k)) {
									Object o1 = p.page.get(j).get(k);
									Object o2 = htblColNameValue.get(k);
									if (o1.equals(o2)) {
										flag1 = true;
										break;
									}
								}
							}
						}
						if (flag1 && p.page.get(j).get("D").equals("F"))
							a.add(p.page.get(j));
					}
				}
			}
			Iterator<Hashtable<String, Object>> itr = a.iterator();
			return itr;
		} catch (Exception e) {
			return null;
		}
	}

	public void updateTable(String strTableName, String strKey,
			Hashtable<String, Object> htblColNameValue) throws DBAppException,
			Exception {
		int n = noPages(strTableName);
		String z = getStrKey(strTableName);
		boolean f = false;
		for (int i = 0; i < n; i++) {
			Page p = readPage(strTableName, i + "");
			for (int j = 0; j < p.page.size(); j++) {
				String a = p.page.get(j).get(z).toString();
				if (a.equals(strKey)  && p.page.get(j).get("D").equals("F")) {
					Enumeration<String> keyValues = htblColNameValue.keys();
					while (keyValues.hasMoreElements()) {
						String k = (String) keyValues.nextElement();
						p.page.get(j).put(k, htblColNameValue.get(k));
					}
					writePage(strTableName, i + "", p);
					f = true;
				}
			}
		}
		if (!f)
			throw new DBAppException("Row not Found");
	}

	public void deleteFromTable(String strTableName,
			Hashtable<String, Object> htblColNameValue, String strOperator)
			throws Exception {

		int n = noPages(strTableName);
		if (strOperator.equals("AND")) {
			for (int i = 0; i < n; i++) {
				Page p = readPage(strTableName, i + "");
				for (int j = 0; j < p.page.size(); j++) {
					Enumeration<String> keyValues = htblColNameValue.keys();
					boolean flag1 = true;
					while (keyValues.hasMoreElements()) {
						String k = (String) keyValues.nextElement();
						Enumeration<String> cur = p.page.get(j).keys();
						while (cur.hasMoreElements()) {
							String val = (String) cur.nextElement();
							if (val.equals(k)) {
								Object o1 = p.page.get(j).get(k);
								Object o2 = htblColNameValue.get(k);
								if (!o1.equals(o2)) {
									flag1 = false;
									break;
								}
							}
						}
					}
					if (flag1 && p.page.get(j).get("D").equals("F"))
						 p.page.get(j).put("D", "T");
				}
				writePage(strTableName, i + "", p);
			}
		}
		if (strOperator.equals("OR")) {
			for (int i = 0; i < n; i++) {
				Page p = readPage(strTableName, i + "");
				for (int j = 0; j < p.page.size(); j++) {
					Enumeration<String> keyValues = htblColNameValue.keys();
					boolean flag1 = false;
					while (keyValues.hasMoreElements()) {
						String k = (String) keyValues.nextElement();
						Enumeration<String> cur = p.page.get(j).keys();
						while (cur.hasMoreElements()) {
							String val = (String) cur.nextElement();
							if (val.equals(k)) {
								Object o1 = p.page.get(j).get(k);
								Object o2 = htblColNameValue.get(k);
								if (o1.equals(o2)) {
									flag1 = true;
									break;
								}
							}
						}
					}
					if (flag1)
						p.page.get(j).put("D", "T");
				}
				writePage(strTableName, i + "", p);
			}
		}

	}

	public static void main(String[] args) throws DBAppException,
			DBEngineException {
		// create a new DBApp
		DBApp myDB = new DBApp();

		// initialize it
		myDB.init();

		 // creating table "Faculty"
		
		// Hashtable<String, String> fTblColNameType = new Hashtable<String,
		// String>();
		// fTblColNameType.put("ID", "Integer");
		// fTblColNameType.put("Name", "String");
		//
		// Hashtable<String, String> fTblColNameRefs = new Hashtable<String,
		// String>();
		//
		// myDB.createTable("Faculty", fTblColNameType, fTblColNameRefs, "ID");
		//
		// // creating table "Major"
		//
		// Hashtable<String, String> mTblColNameType = new Hashtable<String,
		// String>();
		// fTblColNameType.put("ID", "Integer");
		// fTblColNameType.put("Name", "String");
		// fTblColNameType.put("Faculty_ID", "Integer");
		//
		// Hashtable<String, String> mTblColNameRefs = new Hashtable<String,
		// String>();
		// mTblColNameRefs.put("Faculty_ID", "Faculty.ID");
		//
		// myDB.createTable("Major", mTblColNameType, mTblColNameRefs, "ID");
		//
		// // creating table "Course"
		//
		// Hashtable<String, String> coTblColNameType = new Hashtable<String,
		// String>();
		// coTblColNameType.put("ID", "Integer");
		// coTblColNameType.put("Name", "String");
		// coTblColNameType.put("Code", "String");
		// coTblColNameType.put("Hours", "Integer");
		// coTblColNameType.put("Semester", "Integer");
		// coTblColNameType.put("Major_ID", "Integer");
		//
		// Hashtable<String, String> coTblColNameRefs = new Hashtable<String,
		// String>();
		// coTblColNameRefs.put("Major_ID", "Major.ID");
		//
		// myDB.createTable("Course", coTblColNameType, coTblColNameRefs, "ID");
		//
		// // creating table "Student"
		//
		// Hashtable<String, String> stTblColNameType = new Hashtable<String,
		// String>();
		// stTblColNameType.put("ID", "Integer");
		// stTblColNameType.put("First_Name", "String");
		// stTblColNameType.put("Last_Name", "String");
		// stTblColNameType.put("GPA", "Double");
		// stTblColNameType.put("Age", "Integer");
		//
		// Hashtable<String, String> stTblColNameRefs = new Hashtable<String,
		// String>();
		//
		// myDB.createTable("Student", stTblColNameType, stTblColNameRefs,
		// "ID");
		//
		// // creating table "Student in Course"
		//
		// Hashtable<String, String> scTblColNameType = new Hashtable<String,
		// String>();
		// scTblColNameType.put("ID", "Integer");
		// scTblColNameType.put("Student_ID", "Integer");
		// scTblColNameType.put("Course_ID", "Integer");
		//
		// Hashtable<String, String> scTblColNameRefs = new Hashtable<String,
		// String>();
		// scTblColNameRefs.put("Student_ID", "Student.ID");
		// scTblColNameRefs.put("Course_ID", "Course.ID");
		//
		// myDB.createTable("Student_in_Course", scTblColNameType,
		// scTblColNameRefs, "ID");
		//
		// // insert in table "Faculty"
		//
		// Hashtable<String, Object> ftblColNameValue1 = new Hashtable<String,
		// Object>();
		// ftblColNameValue1.put("ID", Integer.valueOf("1"));
		// ftblColNameValue1.put("Name", "Media Engineering and Technology");
		// myDB.insertIntoTable("Faculty", ftblColNameValue1);
		//
		// Hashtable<String, Object> ftblColNameValue2 = new Hashtable<String,
		// Object>();
		// ftblColNameValue2.put("ID", Integer.valueOf("2"));
		// ftblColNameValue2.put("Name", "Management Technology");
		// myDB.insertIntoTable("Faculty", ftblColNameValue2);
		//
		// for (int i = 0; i < 1000; i++) {
		// Hashtable<String, Object> ftblColNameValueI = new Hashtable<String,
		// Object>();
		// ftblColNameValueI.put("ID", Integer.valueOf(("" + (i + 2))));
		// ftblColNameValueI.put("Name", "f" + (i + 2));
		// myDB.insertIntoTable("Faculty", ftblColNameValueI);
		// }
		//
		// // insert in table "Major"
		//
		// Hashtable<String, Object> mtblColNameValue1 = new Hashtable<String,
		// Object>();
		// mtblColNameValue1.put("ID", Integer.valueOf("1"));
		// mtblColNameValue1.put("Name", "Computer Science & Engineering");
		// mtblColNameValue1.put("Faculty_ID", Integer.valueOf("1"));
		// myDB.insertIntoTable("Major", mtblColNameValue1);
		//
		// Hashtable<String, Object> mtblColNameValue2 = new Hashtable<String,
		// Object>();
		// mtblColNameValue2.put("ID", Integer.valueOf("2"));
		// mtblColNameValue2.put("Name", "Business Informatics");
		// mtblColNameValue2.put("Faculty_ID", Integer.valueOf("2"));
		// myDB.insertIntoTable("Major", mtblColNameValue2);
		//
		// for (int i = 0; i < 1000; i++) {
		// Hashtable<String, Object> mtblColNameValueI = new Hashtable<String,
		// Object>();
		// mtblColNameValueI.put("ID", Integer.valueOf(("" + (i + 2))));
		// mtblColNameValueI.put("Name", "m" + (i + 2));
		// mtblColNameValueI
		// .put("Faculty_ID", Integer.valueOf(("" + (i + 2))));
		// myDB.insertIntoTable("Major", mtblColNameValueI);
		// }
		//
		// // insert in table "Course"
		//
		// Hashtable<String, Object> ctblColNameValue1 = new Hashtable<String,
		// Object>();
		// ctblColNameValue1.put("ID", Integer.valueOf("1"));
		// ctblColNameValue1.put("Name", "Data Bases II");
		// ctblColNameValue1.put("Code", "CSEN 604");
		// ctblColNameValue1.put("Hours", Integer.valueOf("4"));
		// ctblColNameValue1.put("Semester", Integer.valueOf("6"));
		// ctblColNameValue1.put("Major_ID", Integer.valueOf("1"));
		// myDB.insertIntoTable("Course", mtblColNameValue1);
		//
		// Hashtable<String, Object> ctblColNameValue2 = new Hashtable<String,
		// Object>();
		// ctblColNameValue2.put("ID", Integer.valueOf("1"));
		// ctblColNameValue2.put("Name", "Data Bases II");
		// ctblColNameValue2.put("Code", "CSEN 604");
		// ctblColNameValue2.put("Hours", Integer.valueOf("4"));
		// ctblColNameValue2.put("Semester", Integer.valueOf("6"));
		// ctblColNameValue2.put("Major_ID", Integer.valueOf("2"));
		// myDB.insertIntoTable("Course", mtblColNameValue2);
		//
		// for (int i = 0; i < 1000; i++) {
		// Hashtable<String, Object> ctblColNameValueI = new Hashtable<String,
		// Object>();
		// ctblColNameValueI.put("ID", Integer.valueOf(("" + (i + 2))));
		// ctblColNameValueI.put("Name", "c" + (i + 2));
		// ctblColNameValueI.put("Code", "co " + (i + 2));
		// ctblColNameValueI.put("Hours", Integer.valueOf("4"));
		// ctblColNameValueI.put("Semester", Integer.valueOf("6"));
		// ctblColNameValueI.put("Major_ID", Integer.valueOf(("" + (i + 2))));
		// myDB.insertIntoTable("Course", ctblColNameValueI);
		// }
		//
		// // insert in table "Student"
		//
		// for (int i = 0; i < 1000; i++) {
		// Hashtable<String, Object> sttblColNameValueI = new Hashtable<String,
		// Object>();
		// sttblColNameValueI.put("ID", Integer.valueOf(("" + i)));
		// sttblColNameValueI.put("First_Name", "FN" + i);
		// sttblColNameValueI.put("Last_Name", "LN" + i);
		// sttblColNameValueI.put("GPA", Double.valueOf("0.7"));
		// sttblColNameValueI.put("Age", Integer.valueOf("20"));
		// myDB.insertIntoTable("Student", sttblColNameValueI);
		// // changed it to student instead of course
		// }
		//
		// // selecting
		//
		// Hashtable<String, Object> stblColNameValue = new Hashtable<String,
		// Object>();
		// stblColNameValue.put("ID", Integer.valueOf("550"));
		// stblColNameValue.put("Age", Integer.valueOf("20"));
		//
		// long startTime = System.currentTimeMillis();
		// Iterator<?> myIt = myDB.selectFromTable("Student", stblColNameValue,
		// "AND");
		// long endTime = System.currentTimeMillis();
		// long totalTime = endTime - startTime;
		// System.out.println(totalTime);
		// while (myIt.hasNext()) {
		// System.out.println(myIt.next());
		// }

		// feel free to add more tests
		// Hashtable<String, Object> stblColNameValue3 = new Hashtable<String,
		// Object>();
		// stblColNameValue3.put("Name", "m7");
		// stblColNameValue3.put("Faculty_ID", Integer.valueOf("7"));
		//
		// long startTime2 = System.currentTimeMillis();
		// Iterator myIt2 = myDB
		// .selectFromTable("Major", stblColNameValue3, "AND");
		// long endTime2 = System.currentTimeMillis();
		// long totalTime2 = endTime2 - startTime2;
		// System.out.println(totalTime2);
		// while (myIt2.hasNext()) {
		// System.out.println(myIt.next());
		// }

		// update will update the age of the student with id=550 to 23

		Hashtable<String, Object> stblColNameValue = new Hashtable<String, Object>();
		stblColNameValue.put("Last_Name", Integer.valueOf("23"));
		try {
			myDB.updateTable("Student", "550", stblColNameValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
