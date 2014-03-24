package main;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import tools.FileManager;
import datatype.KnownData;
import datatype.ActionTransferCount;
import datatype.Record;
import evaluation.Evaluation;

public class Main {

	private static void giveAll() {
		KnownData data = new KnownData();
		
		ArrayList<Record> records = data.getRecordsBetweenDate(0, 107);
		HashSet<Integer> brandIDs = new HashSet<>();
		for (Record record : records) {
			brandIDs.add(record.getBrandID());
		}
		HashMap<Integer, HashSet<Integer>> result = new HashMap<>();
		for (Record record : records) {
			if (!result.containsKey(record.getUserID())) {
				result.put(record.getUserID(), brandIDs);
			}
		}
		FileManager.outputResultFile(result, "all.txt");
	}
	
	private static void naiveOne(boolean isTest) {
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
				if (isTest && records.get(i).getDate() > 106) {
					continue;
				}
				for (int j = i+1; j < records.size(); j++) {
					if (isTest && records.get(j).getDate() > 106) {
						continue;
					}
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

		ArrayList<Record> records = null;
		if (isTest) {
			records = data.getRecordsBetweenDate(76, 107);
		} else {
			records = data.getRecordsBetweenDate(107, 138);
		}
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

	private static void test() {
		KnownData data = new KnownData();
		System.out.println(data.getBrandIDList().size());
		System.out.println(data.getUserIDList().size());
		try {
			System.setOut(new PrintStream("recordPersonTimes.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		};

		ArrayList<Integer> users = data.getUserIDList();
		for (Integer user : users) {
			ArrayList<Record> records = data.getRecordsWithUserID(user);
			int alltime = records.size();
			int buytime = 0;
			int clicktime = 0;
			int collecttime = 0;
			int shoppintcarttime = 0;
			for (Record record : records) {
				if (record.getAction() == Record.ACTION_BUY) {
					buytime++;
				}
				if (record.getAction() == Record.ACTION_CLICK) {
					clicktime++;
				}
				if (record.getAction() == Record.ACTION_COLLECT) {
					collecttime++;
				}
				if (record.getAction() == Record.ACTION_SHOPPINGCART) {
					shoppintcarttime++;
				}
			}
			System.out.println(
					Integer.toString(alltime) + "\t" + 
							Integer.toString(buytime) + "\t" + 
							Integer.toString(clicktime) + "\t" + 
							Integer.toString(collecttime) + "\t" + 
							Integer.toString(shoppintcarttime));
		}
	}

	private static ArrayList<ActionTransferCount> calProductTransRadios(KnownData data, boolean isTest) {
		ArrayList<Integer> productIDs = data.getBrandIDList();
		HashMap<Integer, ActionTransferCount> productTransMap = new HashMap<>();
		for (Integer productID : productIDs) {
			ArrayList<Record> records = data.getRecordsWithBrandID(productID);
			Collections.sort(records, new Comparator<Record>() {
				@Override
				public int compare(Record o1, Record o2) {
					return o1.getDate() - o2.getDate();
				}
			});

			for (int i = 0; i < records.size(); i++) {
				
				if (isTest) {
					if (records.get(i).getDate() > 106) {
						continue;
					}
				}

				if (!productTransMap.containsKey(records.get(i).getBrandID())) {
					productTransMap.put(records.get(i).getBrandID(), new ActionTransferCount(records.get(i).getBrandID()));
				}

				if (records.get(i).getAction() == Record.ACTION_CLICK) {
					productTransMap.get(records.get(i).getBrandID()).increaseClicks();
				}
				if (records.get(i).getAction() == Record.ACTION_COLLECT) {
					productTransMap.get(records.get(i).getBrandID()).increaseCollects();
				}
				if (records.get(i).getAction() == Record.ACTION_SHOPPINGCART) {
					productTransMap.get(records.get(i).getBrandID()).increaseShoppingcarts();
				}
				if (records.get(i).getAction() == Record.ACTION_BUY) {
					productTransMap.get(records.get(i).getBrandID()).increaseBuys();
					for (int j = i-1; j >=0; j--) {
						if (records.get(i).getDate() - records.get(j).getDate() > 30) {
							break;
						}
						if (records.get(i).getUserID() == records.get(j).getUserID()) {
							if (records.get(j).getAction() == Record.ACTION_CLICK) {
								productTransMap.get(records.get(i).getBrandID()).increaseClickTransfer();
							}
							if (records.get(j).getAction() == Record.ACTION_COLLECT) {
								productTransMap.get(records.get(i).getBrandID()).increaseCollectTransfer();
							}
							if (records.get(j).getAction() == Record.ACTION_SHOPPINGCART) {
								productTransMap.get(records.get(i).getBrandID()).increaseShoppingcartTransfer();
							}
							if (records.get(j).getAction() == Record.ACTION_BUY) {
								productTransMap.get(records.get(i).getBrandID()).increaseBuyTransfer();
							}
						}
					}
				}

			}
		}
		ArrayList<ActionTransferCount> productTransRadios = new ArrayList<>(productTransMap.values());
		for (ActionTransferCount actionTransferCount : productTransRadios) {
			actionTransferCount.calTransferRadio();
		}
		
		return productTransRadios;
	}
	
	private static ArrayList<ActionTransferCount> calUserTransRadios(KnownData data, boolean isTest) {
		ArrayList<Integer> userIDs = data.getUserIDList();
		HashMap<Integer, ActionTransferCount> userTransMap = new HashMap<>();
		for (Integer userID : userIDs) {
			ArrayList<Record> records = data.getRecordsWithUserID(userID);
			Collections.sort(records, new Comparator<Record>() {
				@Override
				public int compare(Record o1, Record o2) {
					return o1.getDate() - o2.getDate();
				}
			});

			for (int i = 0; i < records.size(); i++) {
				
				if (isTest) {
					if (records.get(i).getDate() > 106) {
						continue;
					}
				}

				if (!userTransMap.containsKey(records.get(i).getUserID())) {
					userTransMap.put(records.get(i).getUserID(), new ActionTransferCount(records.get(i).getUserID()));
				}

				if (records.get(i).getAction() == Record.ACTION_CLICK) {
					userTransMap.get(records.get(i).getUserID()).increaseClicks();
				}
				if (records.get(i).getAction() == Record.ACTION_COLLECT) {
					userTransMap.get(records.get(i).getUserID()).increaseCollects();
				}
				if (records.get(i).getAction() == Record.ACTION_SHOPPINGCART) {
					userTransMap.get(records.get(i).getUserID()).increaseShoppingcarts();
				}
				if (records.get(i).getAction() == Record.ACTION_BUY) {
					userTransMap.get(records.get(i).getUserID()).increaseBuys();
					for (int j = i-1; j >=0; j--) {
						if (records.get(i).getDate() - records.get(j).getDate() > 30) {
							break;
						}
						if (records.get(i).getBrandID() == records.get(j).getBrandID()) {
							if (records.get(j).getAction() == Record.ACTION_CLICK) {
								userTransMap.get(records.get(i).getUserID()).increaseClickTransfer();
							}
							if (records.get(j).getAction() == Record.ACTION_COLLECT) {
								userTransMap.get(records.get(i).getUserID()).increaseCollectTransfer();
							}
							if (records.get(j).getAction() == Record.ACTION_SHOPPINGCART) {
								userTransMap.get(records.get(i).getUserID()).increaseShoppingcartTransfer();
							}
							if (records.get(j).getAction() == Record.ACTION_BUY) {
								userTransMap.get(records.get(i).getUserID()).increaseBuyTransfer();
							}
						}
					}
				}

			}
		}
		ArrayList<ActionTransferCount> userTransRadios = new ArrayList<>(userTransMap.values());
		for (ActionTransferCount actionTransferCount : userTransRadios) {
			actionTransferCount.calTransferRadio();
		}
		
		return userTransRadios;
	}
	
	private static void naiveTwo(boolean isTest) {
		KnownData data = new KnownData();
		
		ArrayList<ActionTransferCount> userTransRadios = calUserTransRadios(data, isTest);
		HashMap<Integer, ActionTransferCount> userTransRadiosMap = new HashMap<>();
		for (ActionTransferCount actionTransferCount : userTransRadios) {
			userTransRadiosMap.put(actionTransferCount.getID(), actionTransferCount);
		}
		ArrayList<ActionTransferCount> productTransRadios = calProductTransRadios(data, isTest);
		HashMap<Integer, ActionTransferCount> productTransRadiosMap = new HashMap<>();
		for (ActionTransferCount actionTransferCount : productTransRadios) {
			productTransRadiosMap.put(actionTransferCount.getID(), actionTransferCount);
		}
		
		HashMap<Integer, HashMap<Integer, Double>> nextMonthBuyRadio = new HashMap<>();
		
		ArrayList<Record> records = null;
		if (isTest) {
			records = data.getRecordsBetweenDate(76, 107);
		} else {
			records = data.getRecordsBetweenDate(107, 138);
		}
		
		for (Record record : records) {
			int productID = record.getBrandID();
			int userID = record.getUserID();
			int action = record.getAction();

			double score = 0;
			double userScore = 0;
			double productScore = 0;
			if (action == Record.ACTION_CLICK) {
				userScore = userTransRadiosMap.get(userID).getClickTransferRadio();
				productScore = productTransRadiosMap.get(productID).getClickTransferRadio();
			}
			if (action == Record.ACTION_BUY) {
				userScore = userTransRadiosMap.get(userID).getBuyTransferRadio();
				productScore = productTransRadiosMap.get(productID).getBuyTransferRadio();
			}
			if (action == Record.ACTION_COLLECT) {
				userScore = userTransRadiosMap.get(userID).getCollectTransferRadio();
				productScore = productTransRadiosMap.get(productID).getCollectTransferRadio();
			}
			if (action == Record.ACTION_SHOPPINGCART) {
				userScore = userTransRadiosMap.get(userID).getShoppingcartTransferRadio();
				productScore = productTransRadiosMap.get(productID).getShoppingcartTransferRadio();
			}
			
			score = userScore + productScore;
			if (!nextMonthBuyRadio.containsKey(userID)) {
				nextMonthBuyRadio.put(userID, new HashMap<Integer, Double>());
			}
			if (!nextMonthBuyRadio.get(userID).containsKey(productID)) {
				nextMonthBuyRadio.get(userID).put(productID, 0.0);
			}
			double newScore = nextMonthBuyRadio.get(userID).get(productID) + score;
			nextMonthBuyRadio.get(userID).remove(productID);
			nextMonthBuyRadio.get(userID).put(productID, newScore);
		}
		

		HashMap<Integer, HashSet<Integer>> result = new HashMap<>();
		
		Iterator<Entry<Integer, HashMap<Integer, Double>>> iterator = nextMonthBuyRadio.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<java.lang.Integer, java.util.HashMap<java.lang.Integer, java.lang.Double>> entry = (Entry<java.lang.Integer, java.util.HashMap<java.lang.Integer, java.lang.Double>>) iterator
					.next();
			result.put(entry.getKey(), new HashSet<Integer>());
			Iterator<Entry<Integer, Double>> innerIterator = entry.getValue().entrySet().iterator();
			while (innerIterator.hasNext()) {
				Entry<java.lang.Integer, java.lang.Double> entry2 = (Entry<java.lang.Integer, java.lang.Double>) innerIterator
						.next();
				if (entry2.getValue() > 1.8) {
					result.get(entry.getKey()).add(entry2.getKey());
				}
			}
		}
		
		FileManager.outputResultFile(result, "outFile2.txt");

	}

	public static void main(String[] args) {

		//naiveTwo(false);
		giveAll();
		//test();

		//naiveOne();

		Evaluation evaluation = new Evaluation("data/criterion.txt");
		evaluation.evaluate("all.txt");

	}

}
