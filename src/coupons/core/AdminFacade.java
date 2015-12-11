package coupons.core;

public class AdminFacade implements CouponClientFacade {

	@Override
	public CouponClientFacade login (String name, String password, ClientType clientType) throws CouponException {
		if (!ClientType.ADMIN.equals(clientType) || !CouponsUtil.successfulLogin(name, password)){
			throw new CouponException("Bad Login. Try again");
		}
		
		return this;
	}

}
