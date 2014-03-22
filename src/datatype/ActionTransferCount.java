package datatype;

public class ActionTransferCount {
	
	private int ID;
	private int clicks;
	private int collects;
	private int shoppingcarts;
	private int buys;
	private int clickTransfer;
	private int collectTransfer;
	private int shoppingcartTransfer;
	private int buyTransfer;
	
	private double clickTransferRadio;
	private double collectTransferRadio;
	private double shoppingcartTransferRadio;
	private double buyTransferRadio;

	public ActionTransferCount(int ID) {
		this.ID = ID;
		clicks = 0;
		collects = 0;
		shoppingcarts = 0;
		buys = 0;
		clickTransfer = 0;
		collectTransfer = 0;
		shoppingcartTransfer = 0;
		buyTransfer = 0;
	}
	
	public void calTransferRadio() {
		if (clicks != 0)
			clickTransferRadio = (double)clickTransfer / (double)clicks;
		if (collects != 0)
			collectTransferRadio = (double)collectTransfer / (double)collects;
		if (shoppingcarts != 0)
			shoppingcartTransferRadio = (double)shoppingcartTransfer / (double)shoppingcarts;
		if (buys != 0)
			buyTransferRadio = (double)buyTransfer / (double)buys;
	}
	
	public void increaseClicks() {
		clicks++;
	}
	
	public void increaseCollects() {
		collects++;
	}
	
	public void increaseShoppingcarts() {
		shoppingcarts++;
	}
	
	public void increaseBuys() {
		buys++;
	}
	
	public void increaseClickTransfer() {
		clickTransfer++;
	}
	
	public void increaseCollectTransfer() {
		collectTransfer++;
	}
	
	public void increaseShoppingcartTransfer() {
		shoppingcartTransfer++;
	}
	
	public void increaseBuyTransfer() {
		buyTransfer++;
	}
	
	public int getID() {
		return ID;
	}

	public int getClicks() {
		return clicks;
	}

	public int getCollects() {
		return collects;
	}

	public int getShoppingcarts() {
		return shoppingcarts;
	}

	public int getBuys() {
		return buys;
	}

	public int getClickTransfer() {
		return clickTransfer;
	}

	public int getCollectTransfer() {
		return collectTransfer;
	}

	public int getShoppingcartTransfer() {
		return shoppingcartTransfer;
	}

	public int getBuyTransfer() {
		return buyTransfer;
	}

	public double getClickTransferRadio() {
		return clickTransferRadio;
	}

	public double getCollectTransferRadio() {
		return collectTransferRadio;
	}

	public double getShoppingcartTransferRadio() {
		return shoppingcartTransferRadio;
	}

	public double getBuyTransferRadio() {
		return buyTransferRadio;
	}

}
