package com.neak.neakconfigurator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.neak.neakconfigurator.Modules;


public class NEAKConfiguratorActivity extends Activity {
    /** Called when the activity is first created. */
	
	// Initiate CheckBoxs 
	private CheckBox chk_cons;
	private CheckBox chk_lion;
	private CheckBox chk_lazy;
	private CheckBox chk_schd;
	private CheckBox chk_aftr;
	
	// Initiate global boolean values for module state
	public static boolean boo_cons = false;
	public static boolean boo_lion = false;
	public static boolean boo_lazy = false;
	public static boolean boo_schd = false;
	public static boolean boo_aftr = false;
	
	// Initiate buttons
	private Button btn_applyNoBoot;
	private Button btn_applyBoot;
	private Button btn_downloadOTA;
		
	// Initiate values for OTA Downloader 
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    
    // Initiate TextView for showing OTA App status
    private TextView ota_status;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Set up UI elements
        initiateUI();
    }
	
	/** The following method sets up all UI elements and validates status of NEAK options modules */
	private void initiateUI() {
		chk_cons = (CheckBox) findViewById(R.id.checkBox_conservative);
        chk_lion = (CheckBox) findViewById(R.id.checkBox_lionheart);
        chk_lazy = (CheckBox) findViewById(R.id.checkBox_lazy);
        chk_schd = (CheckBox) findViewById(R.id.checkBox_sched_mc);
        chk_aftr = (CheckBox) findViewById(R.id.checkBox_aftr);
        
        // Set Lionheart tweaks option to disabled by default, will be enabled when Conservative is
        chk_lion.setEnabled(false);
        
        ota_status = (TextView)findViewById(R.id.textView_otaStatus);
        
        // Check which modules are enabled, and adjust UI to represent this
        checkEnabled();
        
        chk_cons.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	if (chk_cons.isChecked()) {
            		Toast.makeText(NEAKConfiguratorActivity.this, "Conservative Governor will be enabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_cons = true;
            		chk_lion.setEnabled(true);
               	}
            	else {
            		Toast.makeText(NEAKConfiguratorActivity.this, "Conservative Governor will be disabled on reboot", Toast.LENGTH_SHORT).show();
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
            			Toast.makeText(NEAKConfiguratorActivity.this, "Lionheart Tweaks will be enabled on reboot", Toast.LENGTH_SHORT).show();
            			boo_lion = true;
            		}
            		else {
            			chk_lion.setChecked(false);
            			Toast.makeText(NEAKConfiguratorActivity.this, "Conservative Governor must be enabled first", Toast.LENGTH_SHORT).show();
            		}
            	}
            	else {
            		Toast.makeText(NEAKConfiguratorActivity.this, "Lionheart Tweaks will be disabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_lion = false;
            	}
            }
        });
        
        chk_lazy.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	if (chk_lazy.isChecked()) {
            		Toast.makeText(NEAKConfiguratorActivity.this, "Lazy Governor will be enabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_lazy = true;
            	}
            	else {
            		Toast.makeText(NEAKConfiguratorActivity.this, "Lazy Governor will be disabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_lazy = false;
            	}
            }
        });
        
        chk_schd.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	if (chk_schd.isChecked()) {
            		Toast.makeText(NEAKConfiguratorActivity.this, "SCHED_MC will be enabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_schd = true;
            	}
            	else {
            		Toast.makeText(NEAKConfiguratorActivity.this, "SCHED_MC will be disabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_schd = false;
            	}
            }
        });
        
        chk_aftr.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	if (chk_aftr.isChecked()) {
            		Toast.makeText(NEAKConfiguratorActivity.this, "Aftr Idle Mode will be enabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_aftr = true;
            	}
            	else {
            		Toast.makeText(NEAKConfiguratorActivity.this, "Aftr Idle Mode will be disabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_aftr = false;
            	}
            }
        });
        
        btn_applyNoBoot = (Button)findViewById(R.id.button_applyNoBoot);
        btn_applyNoBoot.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                createBootString();
                Toast.makeText(NEAKConfiguratorActivity.this, "Settings Applied, Please reboot the device to complete module install.", Toast.LENGTH_SHORT).show();
            }
        });
        
        btn_applyBoot = (Button)findViewById(R.id.button_applyBoot);
        btn_applyBoot.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                createBootString();
                Toast.makeText(NEAKConfiguratorActivity.this, "Settings Applied, Rebooting...", Toast.LENGTH_SHORT).show();
                try {
					Runtime.getRuntime().exec("su -c reboot");
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
        
        btn_downloadOTA = (Button)findViewById(R.id.button_downloadOTA);
        btn_downloadOTA.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	startDownload();
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
		// Test if OTA app is installed
		try {
			File f = new File("/data/data/gingermodupdaterapp.ui/databases/cmupdater.db");
			if (f.exists()) {
				ota_status.setText("OTA App status: Installed");
			} else {
				ota_status.setText("OTA App status: Not Installed");
			}
		}
		catch (Exception e) {  
            e.printStackTrace();  
        }
		
	}
	/** Set up files to enable or disable modules on reboot */
	private void createBootString() {
    	
		Toast.makeText(NEAKConfiguratorActivity.this, "Requesting root access", Toast.LENGTH_SHORT).show();
        try {
			Runtime.getRuntime().exec("su");
			Modules.EnablePackage();
			Modules.DisablePackage();
			Toast.makeText(NEAKConfiguratorActivity.this, "Root access granted", Toast.LENGTH_SHORT).show();
			
        } catch (Exception e) {
			Toast.makeText(NEAKConfiguratorActivity.this, "No root access", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} 
		        	
    }
	
	/** The following is the code for the downloader part of the app. Download OTA App and Installs
	 *  Code created by Simone201
	 */
	private void startDownload() {
        String url = getResources().getString(R.string.app_url);
        new DownloadFileAsync().execute(url);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Downloading file..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }
    class DownloadFileAsync extends AsyncTask<String, String, String> {
       
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
                
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(getResources().getString(R.string.app_path));
                   
                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                try {
                   Process p = Runtime.getRuntime().exec("su");
                   OutputStream os = p.getOutputStream();
                   String cmd = "cp" + " " + getResources().getString(R.string.app_path) + " " + getResources().getString(R.string.app_final) + "\n";
                   os.write(cmd.getBytes());
                   String cmd2 = "rm" + " " + getResources().getString(R.string.app_path) + "\n";
                   os.write(cmd2.getBytes());
                   os.flush();
                   ota_status.setText("OTA App status: Installed");
                } catch (IOException e) {
                 Log.d("Failed to copy","Due to no root");
                 ota_status.setText("OTA App status: Not Installed");
                }
            } catch (Exception e) {}
            return null;

        }
        protected void onProgressUpdate(String... progress) {
             Log.d("ANDRO_ASYNC",progress[0]);
             mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
    }
		
}

