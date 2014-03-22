package datatype;

import java.util.ArrayList;
import java.util.HashSet;

import tools.FileManager;

public class KnownData {
	
	public static String DATA_PATH = "data/data.txt";
	public static String TRAINSET_PATH = "data/trainset.txt";
	
	private ArrayList<Record> data;
	private ArrayList<Integer> userIDList;
	private ArrayList<Integer> brandIDList;
	
	public KnownData()
	{
		data = FileManager.readKnownData(DATA_PATH);
		
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

	public ArrayList<Record> getRecordsWithBrandID(int BrandID)
	{
		ArrayList<Record> result = new ArrayList<>();
		
		for (Record record : data) {
			if (record.getBrandID() == BrandID) {
				result.add(record);
			}
		}
		
		return result;
	}

	public ArrayList<Record> getRecordsBetweenDate(int startDate, int endDate)
	{
		ArrayList<Record> result = new ArrayList<>();
		
		for (Record record : data) {
			if (record.getDate() >= startDate && record.getDate() < endDate) {
				result.add(record);
			}
		}
		
		return result;
	}

}
