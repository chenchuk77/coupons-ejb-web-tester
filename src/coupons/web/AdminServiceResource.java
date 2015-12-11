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

import coupons.core.AdminFacade;
import coupons.core.ClientType;
import coupons.core.CompanyFacade;
import coupons.core.CouponException;
import coupons.core.CouponSystem;
import coupons.ejb.beans.IncomeService;
import coupons.ejb.db.Income;
import coupons.web.beans.Message;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
public class AdminServiceResource {
	private static final String ADMIN_SESSION_ATTR = "admin";

	@EJB
	private IncomeService incomeService;


	@GET
	@Path("/login/{username}/{password}")
	public Message login (
			@PathParam("username") String name,
			@PathParam("password") String password,
			@Context HttpServletRequest request) throws CouponException{
		AdminFacade adminFacade = (AdminFacade) CouponSystem.getInstance().login(name, password, ClientType.ADMIN);
		request.getSession(true).setAttribute(ADMIN_SESSION_ATTR, adminFacade);
		System.out.println("AdminServiceResource:\tAdmin logged in");
		return new Message("Successfully logged in as administrator");
	}

	@GET
	@Path("/logout")
	public Message logout (
			@Context HttpServletRequest request) throws CouponException{
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute(ADMIN_SESSION_ATTR) == null){
			throw new CouponException("You must be logged in first");
		}
		if (session != null){
			session.invalidate();
		}
		System.out.println("AdminServiceResource:\tAdmin logged out");
		return new Message("Successfully logged out administrator");
	}

	@GET
	@Path("/viewAllIncomes")
	public Income[] getAllIncomes(
			@Context HttpServletRequest request) throws CouponException{
		AdminFacade adminFacade = (AdminFacade) request.getSession(false).getAttribute(ADMIN_SESSION_ATTR);
		System.out.println("Admin: list of all incomes");
		System.out.println("AdminServiceResource:\tAdmin request all incomes");
		return incomeService.viewAllIncomes().toArray(new Income[0]);
	}

	@GET
	@Path("/viewIncomeByCustomer/{invokerId}")
	public Income[] getCustomerIncome(
			@PathParam("invokerId") long invokerId,
			@Context HttpServletRequest request) throws CouponException{
		AdminFacade adminFacade = (AdminFacade) request.getSession(false).getAttribute(ADMIN_SESSION_ATTR);
		System.out.println("AdminServiceResource:\tAdmin request all incomes for customer " + invokerId);
		return incomeService.viewIncomeByCustomer(invokerId).toArray(new Income[0]);
	}

	@GET
	@Path("/viewIncomeByCompany/{invokerId}")
	public Income[] getCompanyIncome(
			@PathParam("invokerId") long invokerId,
			@Context HttpServletRequest request) throws CouponException{
		AdminFacade adminFacade = (AdminFacade) request.getSession(false).getAttribute(ADMIN_SESSION_ATTR);
		System.out.println("AdminServiceResource:\tAdmin request all incomes for company " + invokerId);
		return incomeService.viewIncomeByCompany(invokerId).toArray(new Income[0]);
	}
}