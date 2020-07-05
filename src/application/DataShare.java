package application;

import javaBean.Reposite;
import util.CheckoutHelper;

public interface DataShare
{
	void setReposite(Reposite reposite);
	void setCheckoutHelper(CheckoutHelper helper);
	void setName(String name);
}
