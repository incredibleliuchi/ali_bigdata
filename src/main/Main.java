package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import datatype.KnownData;
import datatype.Record;

public class Main {

	public static void main(String[] args) {

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
		
		ArrayList<Record> records = data.getRecordsAfterDate(106);
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
		
		File outFile = new File("outFile.txt");
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(outFile));
			
			Iterator<Entry<Integer, HashSet<Integer>>> iterator = result.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<java.lang.Integer, java.util.HashSet<java.lang.Integer>> entry = (Entry<java.lang.Integer, java.util.HashSet<java.lang.Integer>>) iterator
						.next();
				
				bw.write(entry.getKey().toString());
				bw.write("\t");
				
				Iterator<Integer> brandIterator = entry.getValue().iterator();
				while (brandIterator.hasNext()) {
					Integer integer = (Integer) brandIterator.next();
					bw.write(integer.toString());
					if (brandIterator.hasNext()) {
						bw.write(",");
					}
				}
				
				bw.write("\n");;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		

	}

}
