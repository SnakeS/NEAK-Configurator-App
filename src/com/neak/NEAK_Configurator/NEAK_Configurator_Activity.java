package com.neak.NEAK_Configurator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class NEAK_Configurator_Activity extends TabActivity {
    /** Called when the activity is first created. */
    
    private TextView aboutKernelVersion;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /* Check if configurator files exists, if not create it.
         * No Longer implemented
         
        try {
			File f = new File("/data/neak/configurator");
			if (f.exists()) {
			}else {
			Runtime.getRuntime().exec("touch /data/neak/configurator");
			}
        }
        catch (Exception e) {  
            e.printStackTrace();  
        }*/
        
        // Check if neak kernel is installed, if not, close app and present message to user
 		
        
        TabHost tabHost = getTabHost();
        
        // Tab for NEAK Options
        TabSpec neakspec = tabHost.newTabSpec("NEAK Options");
        // setting Title and Icon for the Tab
        neakspec.setIndicator("NEAK Options");
        Intent photosIntent = new Intent(this, NEAK_Options_Activity.class);
        neakspec.setContent(photosIntent);
 
        // Tab for OTA Downloader
        TabSpec otaspec = tabHost.newTabSpec("OTA Downloader");
        otaspec.setIndicator("OTA Downloader");
        Intent songsIntent = new Intent(this, OTA_Downloader_Activity.class);
        otaspec.setContent(songsIntent);
 
        // Adding all TabSpec to TabHost
        tabHost.addTab(neakspec); // Adding photos tab
        tabHost.addTab(otaspec); // Adding songs tab
 
    }
    
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
            final Dialog aboutDialog = new Dialog(NEAK_Configurator_Activity.this);
            aboutDialog.setContentView(R.layout.about_dialog);
            aboutDialog.setTitle("NEAK Configurator App");
            aboutDialog.setCancelable(true);
            
            try {
            	aboutKernelVersion = (TextView)aboutDialog.findViewById(R.id.textview_aboutKernelVersion);
            	aboutKernelVersion.setText("Kernel Version: " + getSystemProperty("ro.neak.version"));
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
        	NEAK_Configurator_Activity.this.finish();
            return true;
 
        case R.id.menu_pro:
        	String url = "market://details?id=com.neak.configurator_pro";
            Intent urlintent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
            startActivity(urlintent);
            return true;
 
        default:
            return super.onOptionsItemSelected(item);
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