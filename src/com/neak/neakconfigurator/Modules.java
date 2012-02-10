package com.neak.neakconfigurator;

import java.io.File;


public class Modules extends NEAKConfiguratorActivity {
/**
 * This class is used purely for the enabling and disabling the NEAK options modules
 */

	public static void EnablePackage() {
		
		if (boo_cons) {
			try {
				File f_cons = new File("/data/neak/conservative");
				if (f_cons.exists()) {
					// Do Nothing
				}else {
					Runtime.getRuntime().exec("touch /data/neak/conservative");
				}
				
			}
			catch (Exception e) {  
	            e.printStackTrace();  
	        }
		}
		if (boo_lion) {
			try {
				File f_lion = new File("/data/neak/lionheart");
				if (f_lion.exists()) {
					// Do Nothing
				}else {
					Runtime.getRuntime().exec("touch /data/neak/lionheart");
				}
				
			}
			catch (Exception e) {  
	            e.printStackTrace();  
	        }
		}
		if (boo_lazy) {
			try {
				File f_lazy = new File("/data/neak/lazy");
				if (f_lazy.exists()) {
					// Do Nothing
				}else {
					Runtime.getRuntime().exec("touch /data/neak/lazy");
				}
				
			}
			catch (Exception e) {  
	            e.printStackTrace(); 
	        }
		}
		if (boo_schd) {
			try {
				File f_schd = new File("/data/neak/schedmc");
				if (f_schd.exists()) {
					// Do Nothing
				}else {
					Runtime.getRuntime().exec("touch /data/neak/schedmc");
				}
				
			}
			catch (Exception e) {  
	            e.printStackTrace();  
	        }
		}
		if (boo_aftr) {
			try {
				File f_aftr = new File("/data/neak/aftridle");
				if (f_aftr.exists()) {
					// Do Nothing
				}else {
					Runtime.getRuntime().exec("touch /data/neak/aftridle");
				}
				
			}
			catch (Exception e) {  
	            e.printStackTrace();  
	        }
		}
	
	}
	
	public static void DisablePackage() {
		
		if (!boo_cons) {
			try {
				File f_cons = new File("/data/neak/conservative");
				if (f_cons.exists()) {
					Runtime.getRuntime().exec("rm /data/neak/conservative");
				}else {
					// Do Nothing
				}
				
			}
			catch (Exception e) {  
	            e.printStackTrace();  
	        }
		}
		if (!boo_lion) {
			try {
				File f_lion = new File("/data/neak/lionheart");
				if (f_lion.exists()) {
					Runtime.getRuntime().exec("rm /data/neak/lionheart");
				}else {
					// Do Nothing
				}
				
			}
			catch (Exception e) {  
	            e.printStackTrace();  
	        }
		}
		if (!boo_lazy) {
			try {
				File f_lazy = new File("/data/neak/lazy");
				if (f_lazy.exists()) {
					Runtime.getRuntime().exec("rm /data/neak/lazy");
				}else {
					// Do Nothing
				}
				
			}
			catch (Exception e) {  
	            e.printStackTrace(); 
	        }
		}
		if (!boo_schd) {
			try {
				File f_schd = new File("/data/neak/schedmc");
				if (f_schd.exists()) {
					Runtime.getRuntime().exec("rm /data/neak/schedmc");
				}else {
					// Do Nothing
				}
				
			}
			catch (Exception e) {  
	            e.printStackTrace();  
	        }
		}
		if (!boo_aftr) {
			try {
				File f_aftr = new File("/data/neak/aftridle");
				if (f_aftr.exists()) {
					Runtime.getRuntime().exec("rm /data/neak/aftridle");
				}else {
					// Do Nothing
				}
				
			}
			catch (Exception e) {  
	            e.printStackTrace();  
	        }
		}
	
	}
	
	
		
}
