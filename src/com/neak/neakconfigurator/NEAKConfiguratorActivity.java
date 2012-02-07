package com.neak.neakconfigurator;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import com.neak.neakconfigurator.Modules;

public class NEAKConfiguratorActivity extends Activity {
    /** Called when the activity is first created. */
   
	private CheckBox chk_cons;
	private CheckBox chk_lion;
	private CheckBox chk_lazy;
	private CheckBox chk_schd;
	private CheckBox chk_aftr;
	
	public static boolean boo_cons = false;
	public static boolean boo_lion = false;
	public static boolean boo_lazy = false;
	public static boolean boo_schd = false;
	public static boolean boo_aftr = false;
	
	private Button btn_applyNoBoot;
	private Button btn_applyBoot;
	
	public String boot_string;


		@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initiateUI();
        
        
        
        /*
        Process p;
        Toast.makeText(NEAKConfiguratorActivity.this, "Requesting root access", Toast.LENGTH_LONG).show();
        try {
			p = Runtime.getRuntime().exec("su");
			try {  
			      p.waitFor();  
			           if (p.exitValue() != 255) {  
			              // TODO Code to run on success  
			        	   Toast.makeText(NEAKConfiguratorActivity.this, "Root access granted", Toast.LENGTH_LONG).show();
			           }  
			           else {  
			               // TODO Code to run on unsuccessful  
			        	   Toast.makeText(NEAKConfiguratorActivity.this, "No root access", Toast.LENGTH_LONG).show();  
			           }  
			   } catch (InterruptedException e) {  
			      // TODO Code to run in interrupted exception  
				   Toast.makeText(NEAKConfiguratorActivity.this, "No root access", Toast.LENGTH_LONG).show();  
			   }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(NEAKConfiguratorActivity.this, "No root access", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} */

    }
		
		private void initiateUI() {
			chk_cons = (CheckBox) findViewById(R.id.checkBox_conservative);
	        chk_lion = (CheckBox) findViewById(R.id.checkBox_lionheart);
	        chk_lazy = (CheckBox) findViewById(R.id.checkBox_lazy);
	        chk_schd = (CheckBox) findViewById(R.id.checkBox_sched_mc);
	        chk_aftr = (CheckBox) findViewById(R.id.checkBox_aftr);
	        
	        checkEnabled();
	        
	        chk_cons.setOnClickListener(new OnClickListener(){
	            public void onClick(View v) {
	            	if (chk_cons.isChecked()) {
	            		Toast.makeText(NEAKConfiguratorActivity.this, "Conservative Governor will be enabled on reboot", Toast.LENGTH_LONG).show();
	            		boo_cons = true;
	               	}
	            	else {
	            		Toast.makeText(NEAKConfiguratorActivity.this, "Conservative Governor will be disabled on reboot", Toast.LENGTH_LONG).show();
	            	}
	            }
	        });
	        
	        chk_lion.setOnClickListener(new OnClickListener(){
	            public void onClick(View v) {
	            	if (chk_lion.isChecked()) {
	            		if (chk_cons.isChecked()) {
	            			Toast.makeText(NEAKConfiguratorActivity.this, "Lionheart Tweaks will be enabled on reboot", Toast.LENGTH_LONG).show();
	            		}
	            		else {
	            			chk_lion.setChecked(false);
	            			Toast.makeText(NEAKConfiguratorActivity.this, "Conservative Governor must be enabled first", Toast.LENGTH_LONG).show();
	            		}
	            	}
	            	else {
	            		Toast.makeText(NEAKConfiguratorActivity.this, "Lionheart Tweaks will be disabled on reboot", Toast.LENGTH_LONG).show();
	            	}
	            }
	        });
	        
	        chk_lazy.setOnClickListener(new OnClickListener(){
	            public void onClick(View v) {
	            	if (chk_lazy.isChecked()) {
	            		Toast.makeText(NEAKConfiguratorActivity.this, "Lazy Governor will be enabled on reboot", Toast.LENGTH_LONG).show();
	            	}
	            	else {
	            		Toast.makeText(NEAKConfiguratorActivity.this, "Lazy Governor will be disabled on reboot", Toast.LENGTH_LONG).show();
	            	}
	            }
	        });
	        
	        chk_schd.setOnClickListener(new OnClickListener(){
	            public void onClick(View v) {
	            	if (chk_schd.isChecked()) {
	            		Toast.makeText(NEAKConfiguratorActivity.this, "SCHED_MC will be enabled on reboot", Toast.LENGTH_LONG).show();
	            	}
	            	else {
	            		Toast.makeText(NEAKConfiguratorActivity.this, "SCHED_MC will be disabled on reboot", Toast.LENGTH_LONG).show();
	            	}
	            }
	        });
	        
	        chk_aftr.setOnClickListener(new OnClickListener(){
	            public void onClick(View v) {
	            	if (chk_aftr.isChecked()) {
	            		Toast.makeText(NEAKConfiguratorActivity.this, "Aftr Idle Mode will be enabled on reboot", Toast.LENGTH_LONG).show();
	            	}
	            	else {
	            		Toast.makeText(NEAKConfiguratorActivity.this, "Aftr Idle Mode will be disabled on reboot", Toast.LENGTH_LONG).show();
	            	}
	            }
	        });
	        
	        btn_applyNoBoot = (Button)findViewById(R.id.button_applyNoBoot);
	        btn_applyNoBoot.setOnClickListener(new OnClickListener(){
	            public void onClick(View v) {
	                createBootString();
	            }
	        });
	        
	        btn_applyBoot = (Button)findViewById(R.id.button_applyBoot);
	        btn_applyBoot.setOnClickListener(new OnClickListener(){
	            public void onClick(View v) {
	                createBootString();
	            }
	        });
		}
		
		private void checkEnabled() {
			// Test if Conservative is already enabled, and check it in the App
			try {
				File f = new File("/data/neak/conservative");
				if (f.exists()) {
					chk_cons.setChecked(true);
					boo_cons = true;
				}else {
					chk_cons.setChecked(false);
					boo_cons = false;
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
			
		}
		
		private void createBootString() {
        	boot_string = Modules.EnablePackage();
        	Toast.makeText(NEAKConfiguratorActivity.this, boot_string, Toast.LENGTH_LONG).show();
        }
		
		
}

