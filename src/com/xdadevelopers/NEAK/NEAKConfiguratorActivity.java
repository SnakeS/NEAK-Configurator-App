package com.xdadevelopers.NEAK;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class NEAKConfiguratorActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
		}

    }
}