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
import coupons.core.CompanyFacade;
import coupons.core.CouponException;
import coupons.core.CouponSystem;
import coupons.ejb.beans.IncomeService;
import coupons.ejb.beans.IncomeType;
import coupons.ejb.db.Income;
import coupons.web.beans.Message;

@Path("/company")
@Produces(MediaType.APPLICATION_JSON)
public class CompanyServiceResource {
	private static final String COMPANY_SESSION_ATTR = "company";

	@EJB
	private IncomeService incomeService;
	
	@GET
	@Path("/login/{username}/{password}")
	public Message login (
			@PathParam("username") String name, 
			@PathParam("password") String password, 
			@Context HttpServletRequest request) throws CouponException{
		CompanyFacade companyFacade = (CompanyFacade) CouponSystem.getInstance().login(name, password, ClientType.COMPANY);
		request.getSession(true).setAttribute(COMPANY_SESSION_ATTR, companyFacade);
		System.out.println("CompanyServiceResource:\tCompany logged in");
		return new Message("Successfully logged in as company: " + name);
	}
	
	@GET
	@Path("/coupon/create/{couponName}")
	public Message createCoupon(
			@PathParam("couponName") String couponName,
			@Context HttpServletRequest request) throws CouponException{
		CompanyFacade companyFacade = (CompanyFacade) request.getSession(false).getAttribute(COMPANY_SESSION_ATTR);
		Income income = new Income(companyFacade.getId(), IncomeType.COMPANY_NEW_COUPON, 100);
		incomeService.storeIncome(income);
		System.out.println("CompanyServiceResource:\tCompany created a coupon");
		return new Message("Company #" + companyFacade.getId() + " successfully created coupon: " + couponName);
	}
	
	@GET
	@Path("/coupon/update/{couponName}")
	public Message updateCoupon(
			@PathParam("couponName") String couponName,
			@Context HttpServletRequest request) throws CouponException{
		CompanyFacade companyFacade = (CompanyFacade) request.getSession(false).getAttribute(COMPANY_SESSION_ATTR);
		Income income = new Income(companyFacade.getId(), IncomeType.COMPANY_UPDATE_COUPON, 10);
		incomeService.storeIncome(income);
		System.out.println("CompanyServiceResource:\tCompany updated a coupon");
		return new Message("Company #" + companyFacade.getId() + " successfully updated coupon: " + couponName);
	}

	@GET
	@Path("/logout")
	public Message logout (
			@Context HttpServletRequest request) throws CouponException{
		HttpSession session = request.getSession(false);
		CompanyFacade compnayFacade = (CompanyFacade) request.getSession(false).getAttribute(COMPANY_SESSION_ATTR);
		long id = compnayFacade.getId();
		if (session != null){
			session.invalidate();
		}
		System.out.println("CompanyServiceResource:\tCompany logged out");
		return new Message("Successfully logged out company #" + id);
	}
	
	@GET
	@Path("/viewIncomeByCompany")
	public Income[] getCompanyIncome(
			@Context HttpServletRequest request) throws CouponException{
		CompanyFacade companyFacade = (CompanyFacade) request.getSession(false).getAttribute(COMPANY_SESSION_ATTR);
		System.out.println("CompanyServiceResource:\tCompany "+companyFacade.getId()+" request to see all incomes");
		return incomeService.viewIncomeByCompany(companyFacade.getId()).toArray(new Income[0]);
	}
}
