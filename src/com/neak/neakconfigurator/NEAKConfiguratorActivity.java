package com.neak.neakconfigurator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.neak.neakconfigurator.Modules;


public class NEAKConfiguratorActivity extends Activity {
    /** Called when the activity is first created. */
	
	// Initiate CheckBoxs 
	private CheckBox chk_cons;
	private CheckBox chk_lion;
	private CheckBox chk_lazy;
	private CheckBox chk_lagf;
	private CheckBox chk_schd;
	private CheckBox chk_aftr;
	private CheckBox chk_ext4;
	
	// Initiate global boolean values for module state
	public static boolean boo_cons = false;
	public static boolean boo_lion = false;
	public static boolean boo_lazy = false;
	public static boolean boo_lagf = false;
	public static boolean boo_schd = false;
	public static boolean boo_aftr = false;
	public static boolean boo_ext4 = false;
	
	// Initiate buttons
	private Button btn_applyNoBoot;
	private Button btn_applyBoot;
	private Button btn_downloadOTA;
		
	// Initiate values for OTA Downloader 
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    
    // Initiate TextView for showing OTA App status
    private TextView ota_status;
    
    private TextView aboutKernelVersion;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Check if configurator files exists, if not create it.
        try {
			File f = new File("/data/neak/configurator");
			if (f.exists()) {
			}else {
			Runtime.getRuntime().exec("touch /data/neak/configurator");
			}
        }
        catch (Exception e) {  
            e.printStackTrace();  
        }
		// Check if neak kernel is installed, if not, close app and present message to user
		try {
			File f = new File("/sbin/near");
			if (f.isDirectory()) {
			}else {
			Toast.makeText(NEAKConfiguratorActivity.this, "This app requires NEAK Kernel to be installed. Please install and try again. Now closing app...", Toast.LENGTH_LONG).show();
			NEAKConfiguratorActivity.this.finish();
			}
		}
		catch (Exception e) {  
            e.printStackTrace();  
        }

        // Set up UI elements
        initiateUI();
    }
	
	// Initiating Menu display
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    /**
     * Menu option click handler
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
 
        switch (item.getItemId())
        {
        case R.id.menu_about:
            // Display about dialog
            final Dialog aboutDialog = new Dialog(NEAKConfiguratorActivity.this);
            aboutDialog.setContentView(R.layout.about_dialog);
            aboutDialog.setTitle("NEAK Configurator App");
            aboutDialog.setCancelable(true);
            
            try {
            	aboutKernelVersion = (TextView)aboutDialog.findViewById(R.id.textview_aboutKernelVersion);
            	aboutKernelVersion.setText("Kernel Version: " + getSystemProperty("ro.neak.type"));
            }
            catch (Exception e){
            	e.printStackTrace();
            }
            
            Button about_button = (Button) aboutDialog.findViewById(R.id.button_aboutClose);
            about_button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                    aboutDialog.cancel();
                }
            });
            aboutDialog.show();
            return true;
 
        case R.id.menu_exit:
            // Close the application
            NEAKConfiguratorActivity.this.finish();
            return true;
 
        case R.id.menu_pro:
            // Placeholder for link to future paid version
            return true;
 
        default:
            return super.onOptionsItemSelected(item);
        }
    } 
	
	/** The following method sets up all UI elements and validates status of NEAK options modules */
	private void initiateUI() {
		chk_cons = (CheckBox) findViewById(R.id.checkBox_conservative);
        chk_lion = (CheckBox) findViewById(R.id.checkBox_lionheart);
        chk_lazy = (CheckBox) findViewById(R.id.checkBox_lazy);
        chk_lagf = (CheckBox) findViewById(R.id.checkBox_lagf);
        chk_schd = (CheckBox) findViewById(R.id.checkBox_sched_mc);
        chk_aftr = (CheckBox) findViewById(R.id.checkBox_aftr);
        chk_ext4 = (CheckBox) findViewById(R.id.checkBox_ext4);
        
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
        
        chk_lagf.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	if (chk_lagf.isChecked()) {
            		Toast.makeText(NEAKConfiguratorActivity.this, "Lagfree Governor will be enabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_lagf = true;
            	}
            	else {
            		Toast.makeText(NEAKConfiguratorActivity.this, "Lagfree Governor will be disabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_lagf = false;
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
        
        chk_aftr.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	if (chk_ext4.isChecked()) {
            		Toast.makeText(NEAKConfiguratorActivity.this, "Ext4 Boost Optimisations will be enabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_ext4 = true;
            	}
            	else {
            		Toast.makeText(NEAKConfiguratorActivity.this, "Ext4 Boost Optimisations will be disabled on reboot", Toast.LENGTH_SHORT).show();
            		boo_ext4 = false;
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
		// Test if Lagfree is already enabled, and check it in the App
		try {
			File f = new File("/data/neak/lagfree");
			if (f.exists()) {
				chk_lagf.setChecked(true);
				boo_lagf = true;
			}else {
				chk_lagf.setChecked(false);
				boo_lagf = false;
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
         try {
			Runtime.getRuntime().exec("su");
			Modules.EnablePackage();
			Modules.DisablePackage();
         } catch (Exception e) {
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
		
}

