package es.upm.grise.profundizacion.td3;

import java.util.Vector;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class ProductDelivery {
	private DBSource orderDataSource;
	
	private Vector<Order> orders = new Vector<Order>();

	public ProductDelivery(DBSource orderDataSource)throws DatabaseProblemException  {
		this.orderDataSource = orderDataSource;
		try {
			orders =orderDataSource.fetchOrders();
		} catch (Exception e) {
			throw new DatabaseProblemException();
		}
	}
	public int getHour(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH");	//6
		Timestamp timestap = new Timestamp(System.currentTimeMillis());//6
		return Integer.valueOf(sdf.format(timestap));//6
	}
	// Calculate the handling amount
	public double calculateHandlingAmount() throws MissingOrdersException {
		//1
		// This method can only be invoked when there are orders to process
		if(orders.isEmpty())//2
			throw new MissingOrdersException();//3
		
		// The handling amount is 2% of the orders' total amount
		double handlingPercentage = SystemConfiguration.getInstance().getHandlingPercentage();//4
		
		double totalAmount = 0;//4
		for(Order order : orders) {//4
			totalAmount += order.getAmount();	//5
		}
		
		// However, it increases depending on the time of the day
		// We need to know the hour of the day. Minutes and seconds are not relevant
		int hour = getHour();//6

		// and it also depends on the number of orders
		int numberOrders = orders.size();//6
		
		// When it is late and the number of orders is large
		// the handling costs more
		if(hour >= 22 || numberOrders > 10) {//6 y 7
			handlingPercentage += 0.01;//8
		}

		// The final handling amount
		return totalAmount * handlingPercentage;//9
		//10
	}

	
}
