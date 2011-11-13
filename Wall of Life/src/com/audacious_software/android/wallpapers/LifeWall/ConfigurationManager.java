package com.audacious_software.android.wallpapers.LifeWall;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;

public class ConfigurationManager implements SharedPreferences.OnSharedPreferenceChangeListener
{
	private SharedPreferences prefs;
	
	private Paint bgPaint = null;
	private Paint activePaint = null;
	private Paint inactivePaint = null;
	
	private int width = -1; 
	private int height = -1; 
	private int padding = -1; 
	private int radius = -1; 
	
	private boolean floatAbove = true;
	
	public ConfigurationManager(SharedPreferences mPrefs)
	{
	    prefs = mPrefs;

        prefs.registerOnSharedPreferenceChangeListener(this);

	    this.onSharedPreferenceChanged(prefs, "none");
	}
	
	public int getWidth() 
	{
		return width;
	}

	public int getHeight()
	{
		return height;
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
			String color = prefs.getString("cellColor", "#ff000000");

			int hex = 0xff000000;
			
			try
			{
				hex = Color.parseColor(color);
			}
			catch (IllegalArgumentException e)
			{

			}

			if (prefs.getBoolean("showInactive", true))
				hex = Color.argb(Color.alpha(hex) / 3, Color.red(hex), Color.green(hex), Color.blue(hex));
			else 
				hex = 0;

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
			String color = prefs.getString("backgroundColor", "#ff000000");
			int hex = 0xff000000;
			
			try
			{
				hex = Color.parseColor(color);
			}
			catch (IllegalArgumentException e)
			{

			}

			bgPaint = new Paint();
		
			bgPaint.setAntiAlias(true);
			bgPaint.setColor(hex);
		
			bgPaint.setStyle(Paint.Style.FILL);
		}
		
        return bgPaint;
	}

	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
	{
		width = Integer.decode(prefs.getString("cellSize", "10")); 
		height = width; 

		padding = width / 5; // TODO 

		String type = prefs.getString("cellType", "rounded");

		if (type.equals("square"))
			radius = 0;
		else if (type.equals("minimal"))
			radius = width / 8;
		else if (type.equals("rounded"))
			radius = width / 4;
		else if (type.equals("maximal"))
			radius = width / 3;
		else if (type.equals("circle"))
			radius = width / 2;

		bgPaint = null;
		activePaint = null;
		inactivePaint = null;
		
		floatAbove = prefs.getBoolean("floatAbove", true);
	}
	
	public int getRefreshInterval()
	{
		int interval = Integer.decode(prefs.getString("lifespan", "125")); 
		
		return interval;
	}

	public boolean floatAbove() 
	{
		return floatAbove;
	}
}
