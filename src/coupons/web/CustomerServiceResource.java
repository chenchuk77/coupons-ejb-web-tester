package coupons.web;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import coupons.core.ClientType;
import coupons.core.CouponException;
import coupons.core.CouponSystem;
import coupons.core.CustomerFacade;
import coupons.ejb.beans.IncomeService;
import coupons.ejb.beans.IncomeType;
import coupons.ejb.db.Income;
import coupons.web.beans.Message;

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerServiceResource {
	private static final String CUSTOMER_SESSION_ATTR = "customer";

	@EJB
	private IncomeService incomeService;
	
	@GET
	@Path("/login/{username}/{password}")
	public Message login (
			@PathParam("username") String name, 
			@PathParam("password") String password, 
			@Context HttpServletRequest request) throws CouponException{
		CustomerFacade customerFacade = (CustomerFacade) CouponSystem.getInstance().login(name, password, ClientType.CUSTOMER);
		request.getSession(true).setAttribute(CUSTOMER_SESSION_ATTR, customerFacade);
		System.out.println("CustomerServiceResource:\tCustomer logged in");
		return new Message("Successfully logged in as customer: " + name);
	}
	
	@GET
	@Path("/purchase/{couponName}/{couponPrice}")
	public Message purchaseCoupon(
			@PathParam("couponName") String couponName,
			@PathParam("couponPrice") double couponPrice,
			@Context HttpServletRequest request) throws CouponException{
		CustomerFacade customerFacade = (CustomerFacade) request.getSession(false).getAttribute(CUSTOMER_SESSION_ATTR);
		Income income = new Income(customerFacade.getId(), IncomeType.CUSTOMER_PURCHASE, couponPrice);
		incomeService.storeIncome(income);
		System.out.println("CustomerServiceResource:\tCustomer purchased coupon.");
		return new Message("Customer #" + customerFacade.getId() + " successfully purcahsed coupon: " + couponName);
	}
	
	@GET
	@Path("/logout")
	public Message logout (
			@Context HttpServletRequest request) throws CouponException{
		HttpSession session = request.getSession(false);
		CustomerFacade customerFacade = (CustomerFacade) request.getSession(false).getAttribute(CUSTOMER_SESSION_ATTR);
		long id = customerFacade.getId();
		if (session != null){
			session.invalidate();
		}
		System.out.println("CustomerServiceResource:\tCustomer logged out");
		return new Message("Successfully logged out customer #" + id);
	}
}
