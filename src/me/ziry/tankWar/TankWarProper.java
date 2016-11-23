package me.ziry.tankWar;

import java.io.IOException;
import java.util.Properties;

public class TankWarProper {
	
	public static Properties ppt = new Properties();
	
	static {
		try {			
			ppt.load( TankWarProper.class.getClassLoader().getResourceAsStream("conpig/tankWar.properties") );
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private TankWarProper() {};
	
	public static String getProper(String key) {
		return ppt.getProperty(key);
	}
	
}
