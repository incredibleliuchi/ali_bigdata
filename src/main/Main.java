package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import tools.FileManager;
import datatype.KnownData;
import datatype.Record;
import evaluation.Evaluation;

public class Main {
	
	private static void naiveOne() {
		KnownData data = new KnownData();
		
		HashSet<Integer> probabBrand = new HashSet<>();
		
		ArrayList<Integer> userIDs = data.getUserIDList();
		
		for (Integer userID : userIDs) {
			
			ArrayList<Record> records = data.getRecordsWithUserID(userID);
			
			Collections.sort(records, new Comparator<Record>() {
				@Override
				public int compare(Record o1, Record o2) {
					if (o1.getBrandID() != o2.getBrandID()) {
						return o1.getBrandID() - o2.getBrandID();
					}
					return o1.getDate() - o2.getDate();
				}
			});
			
			for (int i = 0; i < records.size(); i++) {
				for (int j = i+1; j < records.size(); j++) {
					if (records.get(i).getBrandID() != records.get(j).getBrandID()) {
						break;
					}
					if (records.get(i).getAction() == Record.ACTION_COLLECT || records.get(i).getAction() == Record.ACTION_SHOPPINGCART || records.get(i).getAction() == Record.ACTION_CLICK) {
						if (records.get(j).getAction() == Record.ACTION_BUY && records.get(j).getDate() - records.get(i).getDate() < 30) {
							probabBrand.add(records.get(i).getBrandID());
							break;
						}
					}
				}
			}
		}
		
		ArrayList<Record> records = data.getRecordsBetweenDate(76, 106);
		HashMap<Integer, HashSet<Integer>> result = new HashMap<>();
		for (Record record : records) {
			if (probabBrand.contains(record.getBrandID())) {
				if (record.getAction() == Record.ACTION_COLLECT || record.getAction() == Record.ACTION_SHOPPINGCART || record.getAction() == Record.ACTION_CLICK) {
					if (!result.containsKey(record.getUserID())) {
						result.put(record.getUserID(), new HashSet<Integer>());
					}
					result.get(record.getUserID()).add(record.getBrandID());
				}
			}
		}
		
		FileManager.outputResultFile(result, "outFile.txt");
		
	}

	public static void main(String[] args) {
		
		naiveOne();

		Evaluation evaluation = new Evaluation("data/criterion.txt");
		evaluation.evaluate("outFile.txt");

	}

}
