package coupons.core;

public class CustomerFacade implements CouponClientFacade {
	private long customerId;

	public CustomerFacade() {
	}

	@Override
	public CouponClientFacade login(String name, String password, ClientType clientType) throws CouponException {
		if (!ClientType.CUSTOMER.equals(clientType) || !CouponsUtil.successfulLogin(name, password)) {
			throw new CouponException("Bad Login. Try again");
		}
		setId(CouponsUtil.getIdFromString(name, 1));
		return this;
	}
	
	public long getId() {
		return customerId;
	}
	
	public void setId(long customerId) {
		this.customerId = customerId;
	}
}
