package com.neak.NEAK_Configurator;

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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class OTA_Downloader_Activity extends Activity {

	/** Called when the activity is first created. */
	
	// Initiate buttons
	private Button btn_downloadOTA;
	
	// Initiate values for OTA Downloader 
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    
    // Initiate TextView for showing OTA App status
	private TextView ota_status;
	

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.ota_downloader_layout);
	    
		// Set up UI elements
	    initiateUI();
	
	    // TODO Auto-generated method stub
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
                   checkEnabled();
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
    
	private void initiateUI() {

		ota_status = (TextView)findViewById(R.id.textView_otaStatus);
    
	    btn_downloadOTA = (Button)findViewById(R.id.button_downloadOTA);
	    btn_downloadOTA.setOnClickListener(new OnClickListener(){
	        public void onClick(View v) {
	        	startDownload();
	        	checkEnabled();
	        }
	    });
	    
	    checkEnabled();
	
	}
	
	private void checkEnabled() {
		// Test if OTA app is installed
		try {
			File f = new File("/data/data/gingermodupdaterapp.ui");
			if (f.isDirectory()) {
				ota_status.setText("OTA App status: Installed");
			} else {
				ota_status.setText("OTA App status: Not Installed");
			}
		}
		catch (Exception e) {  
            e.printStackTrace();  
        }
				
	}
		
    

}
