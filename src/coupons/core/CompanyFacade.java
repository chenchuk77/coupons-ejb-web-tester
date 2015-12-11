package coupons.core;

public class CompanyFacade implements CouponClientFacade {
	private long companyId;

	public CompanyFacade() {
		super();
	}

	@Override
	public CouponClientFacade login(String name, String password, ClientType clientType) throws CouponException {
		if (!ClientType.COMPANY.equals(clientType) || !CouponsUtil.successfulLogin(name, password)) {
			throw new CouponException("Bad Login. Try again");
		}

		setId(CouponsUtil.getIdFromString(name, 1));
		return this;
	}
	
	public long getId() {
		return companyId;
	}
	
	public void setId(long companyId) {
		this.companyId = companyId;
	}
}
