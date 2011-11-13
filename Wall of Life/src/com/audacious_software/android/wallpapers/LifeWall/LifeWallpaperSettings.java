package com.audacious_software.android.wallpapers.LifeWall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public final class LifeWallpaperSettings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
	private SharedPreferences prefs = null;
	
	public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()); 

        this.addPreferencesFromResource(R.layout.settings);

        prefs.registerOnSharedPreferenceChangeListener(this);
    }

	protected void onResume() 
	{
		super.onResume();
		prefs.registerOnSharedPreferenceChangeListener(this);
	}

	protected void onPause() 
	{		
		super.onPause();
		prefs.unregisterOnSharedPreferenceChangeListener(this);
	}
	
    protected void onDestroy() 
    {
        prefs.unregisterOnSharedPreferenceChangeListener(this);
 
        prefs = null;
        
        super.onDestroy();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) 
    {

    }
    
    public boolean onPreferenceTreeClick (PreferenceScreen preferenceScreen, Preference preference)
    {
    	if (preference.getKey().equals("aboutlife"))
    	{
    		Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://en.wikipedia.org/wiki/Conway's_Game_of_Life")); //you can make this URL dynamic 
    		startActivity(myIntent); 
    	}
    	else if (preference.getKey().equals("aboutaudacious"))
    	{
    		Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.audacious-software.com")); //you can make this URL dynamic 
    		startActivity(myIntent); 
    	}
    	else if (preference.getKey().equals("sendfeedback"))
    	{
    		Intent emailIntent = new Intent(Intent.ACTION_SEND);
    		emailIntent.setType("plain/text");  

    		String aEmailList[] = { "walloflife@audacious-software.com" };  
    		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);  
    		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Wall of Life Feedback");  
    		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Let us know what you think.");  
    		  
    		startActivity(emailIntent);  
    	}
    	
    	return true;
    }
}
