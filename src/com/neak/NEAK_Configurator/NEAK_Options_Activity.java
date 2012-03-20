package com.neak.NEAK_Configurator;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class NEAK_Options_Activity extends Activity {

	/** Called when the activity is first created. */

	// Initiate CheckBoxs 
	private CheckBox chk_cons;
	private CheckBox chk_lion;
	private CheckBox chk_lazy;
	private CheckBox chk_schd;
	private CheckBox chk_aftr;
	private CheckBox chk_ext4;
	
	// Initiate global boolean values for module state
	public static boolean boo_cons = false;
	public static boolean boo_lion = false;
	public static boolean boo_lazy = false;
	public static boolean boo_schd = false;
	public static boolean boo_aftr = false;
	public static boolean boo_ext4 = false;
		
	// Initiate buttons
	private Button btn_applyNoBoot;
	private Button btn_applyBoot;
    

    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.neak_options_layout);
	    
		// Set up UI elements
	    initiateUI();
	    
	    try {
 			File f = new File("/sbin/near");
 			if (f.isDirectory()) {
 			}else {
 			Toast.makeText(NEAK_Options_Activity.this, "This app requires NEAK Kernel to be installed. Please install and try again. Now closing app...", Toast.LENGTH_LONG).show();
 			chk_cons.setEnabled(false);
 			chk_lion.setEnabled(false);
 			chk_lazy.setEnabled(false);
 			chk_schd.setEnabled(false);
 			chk_aftr.setEnabled(false);
 			chk_ext4.setEnabled(false);
 			btn_applyNoBoot.setEnabled(false);
 			btn_applyBoot.setEnabled(false);
 			//NEAK_Configurator_Activity.this.finish();
 			}
 		}
 		catch (Exception e) {  
             e.printStackTrace();  
 		}
	
	    // TODO Auto-generated method stub
	}
	
	private void initiateUI() {
		chk_cons = (CheckBox) findViewById(R.id.checkBox_conservative);
        chk_lion = (CheckBox) findViewById(R.id.checkBox_lionheart);
        chk_lazy = (CheckBox) findViewById(R.id.checkBox_lazy);
        chk_schd = (CheckBox) findViewById(R.id.checkBox_sched_mc);
        chk_aftr = (CheckBox) findViewById(R.id.checkBox_aftr);
        chk_ext4 = (CheckBox) findViewById(R.id.checkBox_ext4);
        
        // Set Lionheart tweaks option to disabled by default, will be enabled when Conservative is
        chk_lion.setEnabled(false);
        
        // Check which modules are enabled, and adjust UI to represent this
        checkEnabled();
        
        chk_cons.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	if (chk_cons.isChecked()) {
            		Toast.makeText(NEAK_Options_Activity.this, "Conservative Governor will be enabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_cons = true;
            		chk_lion.setEnabled(true);
               	}
            	else {
            		Toast.makeText(NEAK_Options_Activity.this, "Conservative Governor will be disabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_cons = false;
            		chk_lion.setEnabled(false);
            		boo_lion = false;
            	}
            }
        });
        
        chk_lion.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	if (chk_lion.isChecked()) {
            		if (chk_cons.isChecked()) {
            			Toast.makeText(NEAK_Options_Activity.this, "Lionheart Tweaks will be enabled on reboot", Toast.LENGTH_SHORT).show();
            			boo_lion = true;
            		}
            		else {
            			chk_lion.setChecked(false);
            			Toast.makeText(NEAK_Options_Activity.this, "Conservative Governor must be enabled first", Toast.LENGTH_SHORT).show();
            		}
            	}
            	else {
            		Toast.makeText(NEAK_Options_Activity.this, "Lionheart Tweaks will be disabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_lion = false;
            	}
            }
        });
        
        chk_lazy.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	if (chk_lazy.isChecked()) {
            		Toast.makeText(NEAK_Options_Activity.this, "Lazy Governor will be enabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_lazy = true;
            	}
            	else {
            		Toast.makeText(NEAK_Options_Activity.this, "Lazy Governor will be disabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_lazy = false;
            	}
            }
        });
                
        chk_schd.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	if (chk_schd.isChecked()) {
            		Toast.makeText(NEAK_Options_Activity.this, "SCHED_MC will be enabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_schd = true;
            	}
            	else {
            		Toast.makeText(NEAK_Options_Activity.this, "SCHED_MC will be disabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_schd = false;
            	}
            }
        });
        
        chk_aftr.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	if (chk_aftr.isChecked()) {
            		Toast.makeText(NEAK_Options_Activity.this, "Aftr Idle Mode will be enabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_aftr = true;
            	}
            	else {
            		Toast.makeText(NEAK_Options_Activity.this, "Aftr Idle Mode will be disabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_aftr = false;
            	}
            }
        });
        
        chk_ext4.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	if (chk_ext4.isChecked()) {
            		Toast.makeText(NEAK_Options_Activity.this, "Ext4 Boost Optimisations will be enabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_ext4 = true;
            	}
            	else {
            		Toast.makeText(NEAK_Options_Activity.this, "Ext4 Boost Optimisations will be disabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_ext4 = false;
            	}
            }
        });
        
        btn_applyNoBoot = (Button)findViewById(R.id.button_applyNoBoot);
        btn_applyNoBoot.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                createBootString();
                Toast.makeText(NEAK_Options_Activity.this, "Settings Applied, Please reboot the device to complete module install.", Toast.LENGTH_SHORT).show();
            }
        });
        
        btn_applyBoot = (Button)findViewById(R.id.button_applyBoot);
        btn_applyBoot.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                createBootString();
                Toast.makeText(NEAK_Options_Activity.this, "Settings Applied, Rebooting...", Toast.LENGTH_SHORT).show();
                try {
					Runtime.getRuntime().exec("su -c reboot");
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });

	}
	
	/**
     * Returns a SystemProperty
     *
     * @param propName The Property to retrieve
     * @return The Property, or NULL if not found
     */
    public String getSystemProperty(String propName) {
    	String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        finally {
        	if (input != null) {
                try {
                    input.close();
                }
                catch (Exception e) {
                	e.printStackTrace();
                }
            }
        }

        if (input != null && line != null) {
        	return line;
        }
        else {
        	return "Unable to Determine";
        }
    }
    
    private void checkEnabled() {
		// Test if Conservative is already enabled, and check it in the App
		try {
			File f = new File("/data/neak/conservative");
			if (f.exists()) {
				chk_cons.setChecked(true);
				boo_cons = true;
				chk_lion.setEnabled(true);
			}else {
				chk_cons.setChecked(false);
				boo_cons = false;
				chk_lion.setEnabled(false);
        		boo_lion = false;
			}
			
		}
		catch (Exception e) {  
            e.printStackTrace();  
        }
		// Test if Lionheart is already enabled, and check it in the App
		try {
			File f = new File("/data/neak/lionheart");
			if (f.exists()) {
				chk_lion.setChecked(true);
				boo_lion = true;
			}else {
				chk_lion.setChecked(false);
				boo_lion = false;
			}
			
		}
		catch (Exception e) {  
            e.printStackTrace();  
        }
		// Test if Lazy is already enabled, and check it in the App
		try {
			File f = new File("/data/neak/lazy");
			if (f.exists()) {
				chk_lazy.setChecked(true);
				boo_lazy = true;
			}else {
				chk_lazy.setChecked(false);
				boo_lazy = false;
			}
			
		}
		catch (Exception e) {  
            e.printStackTrace();  
        }
		
		// Test if SCHED_MC is already enabled, and check it in the App
		try {
			File f = new File("/data/neak/schedmc");
			if (f.exists()) {
				chk_schd.setChecked(true);
				boo_schd = true;
			}else {
				chk_schd.setChecked(false);
				boo_schd = false;
			}
			
		}
		catch (Exception e) {  
            e.printStackTrace();  
        }
		// Test if Aftr Idle Mode is already enabled, and check it in the App
		try {
			File f = new File("/data/neak/aftridle");
			if (f.exists()) {
				chk_aftr.setChecked(true);
				boo_aftr = true;
			}else {
				chk_aftr.setChecked(false);
				boo_aftr = false;
			}
			
		}
		catch (Exception e) {  
            e.printStackTrace();  
        }
		// Test if Ext4 Boost Optimizations is already enabled, and check it in the App
		try {
			File f = new File("/data/neak/ext4boost");
			if (f.exists()) {
				chk_ext4.setChecked(true);
				boo_ext4 = true;
			}else {
				chk_ext4.setChecked(false);
				boo_ext4 = false;
			}
			
		}
		catch (Exception e) {  
            e.printStackTrace();  
        }
		
    }
    
    /** Set up files to enable or disable modules on reboot */
	private void createBootString() {
         try {
			Runtime.getRuntime().exec("su");
			EnablePackage();
			DisablePackage();
         } catch (Exception e) {
			e.printStackTrace();
         } 
		        	
    }
	
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
		
		if (boo_ext4) {
			try {
				File f_ext4 = new File("/data/neak/ext4boost");
				if (f_ext4.exists()) {
					// Do Nothing
				}else {
					Runtime.getRuntime().exec("touch /data/neak/ext4boost");
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
		if (!boo_ext4) {
			try {
				File f_ext4 = new File("/data/neak/ext4boost");
				if (f_ext4.exists()) {
					Runtime.getRuntime().exec("rm /data/neak/ext4boost");
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
