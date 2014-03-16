package evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class Evaluation {
	private HashMap<Integer, HashSet<Integer>> criTable;
	private int brandsNumInCri;
	
	public Evaluation(String criFileName) {
		criTable = new HashMap<Integer, HashSet<Integer>>();
		brandsNumInCri = loadFile(criFileName, criTable);
	}
	
	private int loadFile(String fileName, HashMap<Integer, HashSet<Integer>> table) {
		int totalNum = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			String line;
			while((line = br.readLine()) != null) {
				String arr[] = line.split("\t");
				int userID = Integer.parseInt(arr[0]);
				HashSet<Integer> brands = new HashSet<Integer>();
				for(String str : arr[1].split(",")) {
					brands.add(Integer.parseInt(str));
					totalNum ++;
				}
				table.put(userID, brands);
			}
			br.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return totalNum;
	}
	
	public void evaluate(String predictFile) {
		HashMap<Integer, HashSet<Integer>> preTable = new HashMap<Integer, HashSet<Integer>>();
		int brandsNumInPre = loadFile(predictFile, preTable);
		int hitNum = 0;
		for(Entry<Integer, HashSet<Integer>> buyer : preTable.entrySet()) {
			if(criTable.containsKey(buyer.getKey())) {
				HashSet<Integer> criBrands = criTable.get(buyer.getKey());
				for(int brandID : buyer.getValue()) {
					if(criBrands.contains(brandID))
						hitNum ++;
				}
			}
		}
		double precision = hitNum*100.0/brandsNumInPre;
		double recall = hitNum*100.0/brandsNumInCri;
		double fvalue = 2*precision*recall/(precision+recall);
		System.out.println(String.format("Precision: %.2f%%", precision));
		System.out.println(String.format("Recall:    %.2f%%", recall));
		System.out.println(String.format("F-value:   %.2f%%", fvalue));
	}
}
