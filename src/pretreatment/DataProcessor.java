package pretreatment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import datatype.Record;

public class DataProcessor {
	public static String DATA_PATH = "data/data.txt";
	public static String TRAINSET_PATH = "data/trainset.txt";
	public static String TESTSET_PATH = "data/testset.txt";
	public static String CRI_PATH = "data/criterion.txt";
	
	public static void splitData() {
		File file = new File(DATA_PATH);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			PrintWriter testout = new PrintWriter(TESTSET_PATH);
			PrintWriter trainout = new PrintWriter(TRAINSET_PATH);
			String line = null;
			
			while ((line = br.readLine())!=null) {
				String[] strings = line.split("\t");
				int month = Integer.parseInt(strings[3].split("月")[0]);
				int day = Integer.parseInt(strings[3].split("月")[1].split("日")[0]);
				if(month < 7 || (month == 7 && day < 15))
					trainout.println(line);
				else
					testout.println(line);
			}
			br.close();
			testout.close();
			trainout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void genCriterion() {
		File file = new File(DATA_PATH);
		BufferedReader br = null;
		HashMap<Integer, HashSet<Integer>> buyTable = new HashMap<Integer, HashSet<Integer>>();
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			
			while ((line = br.readLine())!=null) {
				String[] strings = line.split("\t");
				int action = Integer.parseInt(strings[2]);
				if(action != 1)
					continue;
				int userID = Integer.parseInt(strings[0]);
				int brandID = Integer.parseInt(strings[1]);
				int month = Integer.parseInt(strings[3].split("月")[0]);
				int day = Integer.parseInt(strings[3].split("月")[1].split("日")[0]);
				if(month > 7 || (month == 7 && day >= 15)) {
					if(!buyTable.containsKey(userID)) {
						buyTable.put(userID, new HashSet<Integer>());
					}
					buyTable.get(userID).add(brandID);
				}
			}
			br.close();
			
			PrintWriter fout = new PrintWriter(CRI_PATH);
			for(Entry<Integer, HashSet<Integer>> buyer : buyTable.entrySet()) {
				fout.print(buyer.getKey()+"\t");
				int num = 0;
				for(int brandID : buyer.getValue()) {
					if(num == 0)
						fout.print(brandID);
					else
						fout.print(","+brandID);
					num ++;
				}
				fout.println();
			}
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//DataProcessor.splitData();
		//DataProcessor.genCriterion();
	}
}
