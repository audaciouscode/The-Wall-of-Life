package com.audacious_software.android.wallpapers.LifeWall;

import java.util.Timer;
import java.util.TimerTask;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class LifeWallpaperService extends WallpaperService 
{    
	public Engine onCreateEngine() 
	{
		return new LifeWallEngine();
	}
	
    class LifeWallEngine extends Engine implements SharedPreferences.OnSharedPreferenceChangeListener
    {
    	private final ConfigurationManager _config;
    	private final LifeModel _model = new LifeModel();
    	private SharedPreferences _prefs;

    	private Timer _timer = null;
        private boolean _visible;

        private boolean _float = true;
        private Paint _bg = null;
        private Paint _active = null;
		private Paint _inactive = null;
        private int _interval = -1;
		private int _canvasHeight = -1;
		private int _canvasWidth = -1;
        private int _configWidth = -1;
        private int _configHeight = -1;
        private int _padding = -1;
        private float _radius = -1;

        private boolean drawing = false;
        
        private SurfaceHolder _holder = null;
        
        private float _offset = 0.5f;

        private void play()
        {
        	if (_timer != null)
        		this.pause();
        	
        	_timer = new Timer();
        	
        	_timer.schedule(new TimerTask()
        	{
				public void run() 
				{
					if (_visible)
						drawFrame(true);
				}
        	}, 0, _interval);
        }
        
        private void pause()
        {
        	if (_timer != null)
        	{
        		_timer.cancel();
        		_timer = null;
        	}
        }

        LifeWallEngine() 
        {
            _prefs = PreferenceManager.getDefaultSharedPreferences(LifeWallpaperService.this.getApplicationContext()); 

            _config = new ConfigurationManager(_prefs);

            _prefs.registerOnSharedPreferenceChangeListener(this);

            onSharedPreferenceChanged(_prefs, null);
        }

        public void onDestroy() 
        {
        	_prefs.unregisterOnSharedPreferenceChangeListener(this);
         
        	_prefs = null;

        	this.pause();
                
        	super.onDestroy();
        }

        public void onVisibilityChanged(boolean visible) 
        {
        	boolean change = (visible != _visible);
        	
            _visible = visible;

            if (visible && change) 
            	this.play();
            else if (change)
            	this.pause();
        }

        public void onOffsetsChanged (float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset)
        {
        	if (_float)
        		_offset = xOffset;
        	
        	this.drawFrame(false);
        }

        public void onSurfaceDestroyed(SurfaceHolder holder) 
        {
            super.onSurfaceDestroyed(holder);
            _visible = false;

            this.pause();
        }

        void drawFrame(boolean iterate) 
        {
        	if (drawing)
        		return;
        	
        	drawing = true;
        	
            if (_holder == null)
            	_holder = getSurfaceHolder();

            Canvas c = null;

            try 
            {
                c = _holder.lockCanvas();
                
                if (c != null) 
                	drawCells(c);
            } 
            finally 
            {
                if (c != null) 
                	_holder.unlockCanvasAndPost(c);
            }

            if (iterate)
            	_model.iterate();
            
            drawing = false;
        }

        void drawCells(Canvas c) 
        {
            c.save();
            
			if (_canvasHeight == -1)
	            _canvasHeight = c.getHeight();

	        if (_canvasWidth == -1)
	        	_canvasWidth = c.getWidth();

            int wholeWidth = _canvasWidth * 3;
            
            RectF bgRect = new RectF(0, 0, _canvasWidth, _canvasHeight);

            int baseOffset = (int) ((wholeWidth - _canvasWidth) * (1 - _offset)) - _canvasWidth;

        	if (!_float)
        	{      		
        		wholeWidth = _canvasWidth;
        		baseOffset = 0;
        	}
        	
            c.drawRect(bgRect, _bg);
            	
            int fullCellWidth = _configWidth + _padding;
            int fullCellHeight = _configHeight + _padding;

            int rows =  _canvasHeight / fullCellHeight + 1;
            int columns = wholeWidth / fullCellWidth + 1;

    		RectF rect = new RectF(0, 0, 0, 0);

            int yOffset = (_canvasHeight - (fullCellHeight * rows) - _padding) / 2;
            int xOffset = (_canvasWidth - (fullCellWidth * columns) - _padding) / 2;

            int xCoefs = baseOffset + xOffset + _padding;
            int yCoefs = yOffset + _padding;
            int dblWidth = (_configWidth * 2);
            int tripleWidth = (_configWidth * 3);
            
            boolean drawInactive = (_inactive.getColor() != 0);

            for (int i = 0; i < rows; i++)
            {
            	for (int j = 0; j < columns; j++)
            	{
                    int x = (j * fullCellWidth) + xCoefs;

                    if (x + dblWidth < 0)
                    	j++;
                    else if (x - tripleWidth > _canvasWidth)
                    	j = columns;
                   	else
                   	{
                   		int y = (i * fullCellHeight) + yCoefs;

                   		rect.set(x, y, x + _configWidth, y + _configHeight);

                   		if (_model.isActive(j, i, true))
                   			c.drawRoundRect(rect, _radius, _radius, _active);
                   		else if (drawInactive)
                   			c.drawRoundRect(rect, _radius, _radius, _inactive);
                   	}
            	}
            }

            c.restore();
        }

		public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) 
		{
			_model.clear();

	        _interval = _config.getRefreshInterval();
        	_bg = _config.getBackgroundPaint();
            _active = _config.getActivePaint();
        	_inactive = _config.getInactivePaint();
			_canvasHeight = -1;
			_canvasWidth = -1;

        	_configWidth = _config.getWidth();
        	_configHeight = _config.getHeight();
        	_padding = _config.getPadding();
        	_radius = _config.getRadius();
        	_float = _config.floatAbove();
		}
    }
}
