package com.audacious_software.android.life_wallpaper.lite;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;

public class ConfigurationManager implements SharedPreferences.OnSharedPreferenceChangeListener
{
	private SharedPreferences prefs;
	
	private Paint activePaint = null;
	private Paint inactivePaint = null;
	private Paint bgPaint = null;
	
	private int padding = -1; 
	private int radius = -1; 
	
	public ConfigurationManager(SharedPreferences mPrefs)
	{
	    prefs = mPrefs;

        prefs.registerOnSharedPreferenceChangeListener(this);

	    this.onSharedPreferenceChanged(prefs, "none");
	}
	
	public int getWidth() 
	{
		return 20;
	}

	public int getHeight()
	{
		return 20;
	}
	
	public int getPadding()
	{
		return padding;
	}

	public Paint getActivePaint() 
	{
		if (activePaint == null)
		{
			String color = prefs.getString("cellColor", "#ff808080");

			int hex = 0xff808080;
			
			try
			{
				hex = Color.parseColor(color);
			}
			catch (IllegalArgumentException e)
			{

			}
			
			activePaint = new Paint();
		
			activePaint.setAntiAlias(true);
			activePaint.setColor(hex);
		
			activePaint.setStyle(Paint.Style.FILL);
		}
		
        return activePaint;
	}

	public Paint getInactivePaint() 
	{
		if (inactivePaint == null)
		{
			int hex = 0;

			inactivePaint = new Paint();
		
			inactivePaint.setAntiAlias(true);
			inactivePaint.setColor(hex);
		
			inactivePaint.setStyle(Paint.Style.FILL);
		}
		
        return inactivePaint;
	}

	public float getRadius() 
	{
		return radius;
	}

	public Paint getBackgroundPaint() 
	{
		if (bgPaint == null)
		{
			int hex = 0xff000000;

			bgPaint = new Paint();
		
			bgPaint.setAntiAlias(true);
			bgPaint.setColor(hex);
		
			bgPaint.setStyle(Paint.Style.FILL);
		}
		
        return bgPaint;
	}

	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
	{
		padding = 20 / 5; // TODO 

		String type = prefs.getString("cellType", "rounded");

		if (type.equals("square"))
			radius = 0;
		else if (type.equals("minimal"))
			radius = 20 / 8;
		else if (type.equals("rounded"))
			radius = 20 / 4;
		else if (type.equals("maximal"))
			radius = 20 / 3;
		else if (type.equals("circle"))
			radius = 20 / 2;

		bgPaint = null;
		activePaint = null;
		inactivePaint = null;
	}
	
	public int getRefreshInterval()
	{
		return 500;
	}

	public boolean floatAbove() 
	{
		return true;
	}
}
