package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import datatype.Record;

public class FileManager {
	
	public static ArrayList<Record> readKnownData(String fileName) {
		ArrayList<Record> data = new ArrayList<>();
		File file = new File(fileName);
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
		
		return data;
	}

	public static void outputResultFile(HashMap<Integer, HashSet<Integer>> result, String fileName) {
		File outFile = new File(fileName);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(outFile));
			
			Iterator<Entry<Integer, HashSet<Integer>>> iterator = result.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<java.lang.Integer, java.util.HashSet<java.lang.Integer>> entry = (Entry<java.lang.Integer, java.util.HashSet<java.lang.Integer>>) iterator
						.next();
				
				if (entry.getValue().size() == 0) {
					continue;
				}
				
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
