package coupons.core;

public class CouponSystem {
	private static CouponSystem instance = new CouponSystem();
	
	private CouponSystem() {
	}
	
	public CouponClientFacade login (String name, String password, ClientType clientType) throws CouponException{
		switch (clientType) {
			case ADMIN:
				return new AdminFacade().login(name, password, clientType);
				//break;

			case COMPANY:
				return new CompanyFacade().login(name, password, clientType);
				//break;
				
			case CUSTOMER:
				return new CustomerFacade().login(name, password, clientType);
				//break;

			default:
				throw new CouponException("Illegal login type. Try again.");
		}
	}
	
	public static CouponSystem getInstance() {
		return instance;
	}
}
