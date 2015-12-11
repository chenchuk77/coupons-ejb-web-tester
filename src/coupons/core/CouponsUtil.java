package coupons.core;

public class CouponsUtil {

	public static long getIdFromString (String str, long defaultValue){
		long id = defaultValue;
		String [] ids = str.split("[\\D\\s]+");
		if (ids.length > 0){
			for (String s : ids){
				if (!s.isEmpty()){
					try {
						id = Integer.parseInt(s);
						break;
					} catch (Exception e) {	}
				}
			}
		}
		
		return id;
	}
	
	public static boolean successfulLogin (String name, String password){
		return name != null && !name.trim().isEmpty() && name.equals(password);
	}

}
