package UnFound;

import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

public class Page implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ArrayList<Hashtable<String, Object>> page;
	public int max;
	
	public Page(){
		page = new ArrayList<>();
		File configFile = new File("config/DBApp.config");
		try {
		    FileReader reader = new FileReader(configFile);
		    Properties props = new Properties();
		    props.load(reader);
		    max = Integer.parseInt(props.getProperty("MaximumRowsCountinPage"));
		    reader.close();
		} catch (Exception ex) {
		    // I/O error
		}
	}

	public boolean addRecord(Hashtable<String, Object> r){
		if(!this.isFull()){
			page.add(r);
			return true;
		}
		return false;
	}

	public boolean isFull(){
		return page.size() == max;
	}
	
	public boolean isEmpty(){
		return page.size() == 0;
	}

	public static void main(String[] args) {
		
	}
	
}
