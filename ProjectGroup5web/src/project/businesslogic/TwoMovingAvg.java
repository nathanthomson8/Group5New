package project.businesslogic;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

import project.servlets.OrderManager;
import data.access.Dal;
import data.dataObjects.CompanyObject;
import data.dataObjects.StockObject;
import data.dataObjects.TradeHistoryObject;
import data.dataObjects.UserObject;
import yahooFeed.Feed;

public class TwoMovingAvg implements Runnable {
	Logger log = Logger.getLogger(TwoMovingAvg.class);
	
	public TwoMovingAvg() {
		super();
	}
	
	
	public TwoMovingAvg(String name){
		symbol = name;
	    log.info("Creating " +  symbol );
	}
	
	private final int QUANTITY = 15000;
	private final int VALUESHORTAVERAGE = 4;
	private final int VALUELONGAVERAGE = 20;
	private double shortMovingAverage = 0;
	private double longMovingAverage = 0;
	private double pricePaid = 0;
	private double priceGot = 0;
	private double runningTotal = 0;
	private double profitMarginOfInvestment = 3000;
	private double lossMarginOfInvestment = -3000;
	private boolean bought = false;
	private boolean sold = false;
	private boolean suspended = false;
	private boolean running = false;
	public Thread t;
	private String threadName;
	
	
	private LinkedList<Double> shortlist = new LinkedList<>();
	private LinkedList<Double> longlist = new LinkedList<>();
	private LinkedList<Double> differenceLongShort = new LinkedList<>();
	
	private StockObject stock = new StockObject();
		
	public void TwoMovingAverage(String compSymbol) throws InterruptedException, SQLException{
		
		TradeHistoryObject trade = null;
		OrderManager om = null;
		//UserObject user = null;
		
	while(true){
		
			Thread.sleep(1000);
			
			stock = Feed.feedConnection(compSymbol);
			log.info("Stock Object Created Using Feed Data");
			CompanyObject company = new CompanyObject();

			if (shortlist.size() == VALUESHORTAVERAGE) {
				shortlist.removeFirst();
				shortlist.add((stock.getAskPrice() + stock.getBidPrice())/2);
				shortMovingAverage = calcShortMovingAverage(shortlist);
			}
			else{
				shortlist.add((stock.getAskPrice() + stock.getBidPrice())/2);
			}
			if (longlist.size() == VALUELONGAVERAGE) {
				longlist.removeFirst();
				longlist.add((stock.getAskPrice() + stock.getBidPrice())/2);
				longMovingAverage = calcLongMovingAverage(longlist);
			}
			else{
				longlist.add((stock.getAskPrice() + stock.getBidPrice())/2);
			}
			
			//start of the profit loss
			
			differenceLongShort.add((longMovingAverage - shortMovingAverage));
			
			Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());
	        String strDate = now.toString();
			
			if(differenceLongShort.size() == 2){
				// difference was pos now neg.. e.g was above now below SELL
				if(differenceLongShort.get(0) > 0 && differenceLongShort.get(1) < 0){
					if(sold == false){
						log.info("SELLLINGGGGGG");
						sold = true;
						priceGot = stock.getBidPrice() * QUANTITY;
						
						om.sellOrder(symbol, stock.getBidPrice() , QUANTITY);
						
						Dal.addStock(stock);
						stock.setStockID(Dal.getStockId(compSymbol));
						
						trade = new TradeHistoryObject();
					
						trade.setBought(false);
						trade.setStockID(stock.getStockID());;
						trade.setTradeTime(now);
						//trade.setUserObject(user);
						
						Dal.addTradeHistory(trade);
						log.info("Trade added: "+trade.getTradeTime());
					}
					
				}
				// difference was neg now pos.. e.g was below now above BUY
				else if(differenceLongShort.get(0) < 0 && differenceLongShort.get(1) > 0){
					if(bought == false){
						log.info("BUYYYYYINGGGGGG");
						bought = true;
						pricePaid = stock.getAskPrice() * QUANTITY;
						
						om.buyOrder(symbol, stock.getAskPrice(), QUANTITY);
						
						Dal.addStock(stock);
						stock.setStockID(Dal.getStockId(compSymbol));
						
						trade = new TradeHistoryObject();
						
						trade.setBought(true);
						trade.setStockID(stock.getStockID());;
						trade.setTradeTime(now);
						//trade.setUserObject(user);
						
						Dal.addTradeHistory(trade);
						log.info("Trade added: "+trade.getTradeTime());
					}				
				}
				differenceLongShort.remove(0);
			}
			
			if(bought == true && sold == true){
				runningTotal += (pricePaid - priceGot);
				bought = false;
				sold = false;
				System.out.println("Running Total = " + runningTotal);
				
				if(runningTotal <= lossMarginOfInvestment || runningTotal >= profitMarginOfInvestment)
					log.info("1% Profit Or Loss Margin Met.. Two Moving Average Strategy Exited");
					break;
			}
			
			}
		}
	
	public double calcLongMovingAverage(LinkedList<Double> lList){
		double av = 0, total = 0;
		for(int i = 0;i<lList.size();i++)
		{
			total += lList.get(i);
		}
		av = total/VALUELONGAVERAGE;
        return av;
	}
	
	public double calcShortMovingAverage(LinkedList<Double> sList){
		double av = 0, total = 0;
		for(int i = 0;i<sList.size();i++)
		{
			total += sList.get(i);
		}
		av = total/VALUESHORTAVERAGE;
        return av;
	}
	private String symbol;

	public void setSymbol(String compSymbol) {
		symbol = compSymbol;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public void run() {
		log.info("Two Moving Average Started for: " + symbol);
		try {
			Dal.updateStrategy(symbol, 1);
		} catch (SQLException e1) {
			log.error("Setting strategy 1: " + e1.getMessage());
			e1.printStackTrace();
		}
		try {
			TwoMovingAverage(symbol);
		} catch (InterruptedException | SQLException e) {
			log.error("ANY number of problems"+e.getMessage());
		}
		try {
			Dal.updateStrategy(symbol, 0);
		} catch (SQLException e1) {
			log.error("Setting strategy 0: " + e1.getMessage());
			e1.printStackTrace();
		}
	}
	
	public void start (){
		  log.info("Starting thread" +  symbol );
		  if (t == null)
		  {
		     t = new Thread (this, symbol);
		     t.start ();
		     running = true;
		  }
	 }
	
	public boolean isRunning() {
		return running;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public void suspend() {
	     suspended = true;
	}
	public synchronized void resume() {
	      suspended = false;
	      notify();
	}
}