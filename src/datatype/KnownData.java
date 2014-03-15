package datatype;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class KnownData {
	
	public static String DATA_PATH = "data/data.txt";
	
	private ArrayList<Record> data;
	private ArrayList<Integer> userIDList;
	private ArrayList<Integer> brandIDList;
	
	public KnownData()
	{
		data = new ArrayList<>();
		File file = new File(DATA_PATH);
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(file));
			String nextLine = null;
			
			while ((nextLine = br.readLine())!=null) {
				data.add(new Record(nextLine));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		HashSet<Integer> userIDHashSet = new HashSet<>();
		HashSet<Integer> brandIDHashSet = new HashSet<>();
		
		for (Record record : data) {
			userIDHashSet.add(record.getUserID());
			brandIDHashSet.add(record.getBrandID());
		}
		
		userIDList = new ArrayList<>(userIDHashSet);
		brandIDList = new ArrayList<>(brandIDHashSet);
	}

	public ArrayList<Integer> getUserIDList() {
		return userIDList;
	}

	public ArrayList<Integer> getBrandIDList() {
		return brandIDList;
	}
	
	public ArrayList<Record> getRecordsWithUserID(int userID)
	{
		ArrayList<Record> result = new ArrayList<>();
		
		for (Record record : data) {
			if (record.getUserID() == userID) {
				result.add(record);
			}
		}
		
		return result;
	}

	public ArrayList<Record> getRecordsAfterDate(int date)
	{
		ArrayList<Record> result = new ArrayList<>();
		
		for (Record record : data) {
			if (record.getDate() >= date) {
				result.add(record);
			}
		}
		
		return result;
	}

}
