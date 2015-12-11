package coupons.core;

public interface CouponClientFacade {
	public CouponClientFacade login (String name, String password, ClientType clientType) throws CouponException;
}
