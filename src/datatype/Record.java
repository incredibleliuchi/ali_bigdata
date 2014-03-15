package datatype;

public class Record {
	
	public static int ACTION_CLICK = 0;
	public static int ACTION_BUY = 1;
	public static int ACTION_COLLECT = 2;
	public static int ACTION_SHOPPINGCART = 3;
	
	private int userID;
	private int brandID;
	private int action;
	private int date;

	public Record(String recordString)
	{
		String[] strings = recordString.split("\t");
		userID = Integer.parseInt(strings[0]);
		brandID = Integer.parseInt(strings[1]);
		action = Integer.parseInt(strings[2]);
		
		date = 0;
		int month = Integer.parseInt(strings[3].split("月")[0]);
		int day = Integer.parseInt(strings[3].split("月")[1].split("日")[0]);
		switch (month) {
		case 4:
			date = 0 + day;
			break;

		case 5:
			date = 30 + day;
			break;

		case 6:
			date = 61 + day;
			break;

		case 7:
			date = 91 + day;
			break;

		case 8:
			date = 122 + day;
			break;

		default:
			break;
		}
	}
	
	public int getUserID() {
		return userID;
	}

	public int getBrandID() {
		return brandID;
	}

	public int getAction() {
		return action;
	}

	public int getDate() {
		return date;
	}

}
