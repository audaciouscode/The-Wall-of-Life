package com.audacious_software.android.life_wallpaper.lite;

import java.util.Random;

public class LifeModel 
{
	private boolean[][] cells;
	private Random random;
	
	public LifeModel()
	{
		random = new Random();
		cells = new boolean[0][];
	}
	
	public boolean isActive(int column, int row, boolean addMissing)
	{
		if (column < 0 || row < 0)
			return random.nextBoolean();
		
		boolean value = false;

		try
		{
			value = cells[column][row];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			value = random.nextBoolean();
			
			if (addMissing)
			{
				try
				{
					boolean[] oldColumn = cells[column];
					
					boolean[] newColumn = new boolean[row + 1];
					
					for (int i = 0; i < newColumn.length; i++)
					{
						if (i < oldColumn.length)
							newColumn[i] = oldColumn[i];
						else
							newColumn[i] = random.nextBoolean();
					}
					
					cells[column] = newColumn;
				}
				catch (ArrayIndexOutOfBoundsException f)
				{
					boolean[][] newCells = new boolean[column + 1][];
					
					for (int i = 0; i < newCells.length; i++)
					{
						if (i < cells.length)
							newCells[i] = cells[i];
						else
							newCells[i] = new boolean[0];
					}
					
					cells = newCells;
					
					return this.isActive(column, row, addMissing);
				}

				cells[column][row] = value;
			}
		}
		
		return value;
	}

	public void iterate() 
	{
		boolean nextCells[][] = new boolean[cells.length][];
		
		for (int i = 0; i < cells.length; i++)
		{
			nextCells[i] = new boolean[cells[i].length];
			
			for (int j = 0; j < cells[i].length; j++)
			{
				nextCells[i][j] = this.nextState(i, j);
			}
		}
		
		cells = nextCells;
	}
	
	public int neighborCount(int column, int row)
	{
		int count = 0;
		
		if (isActive(column - 1, row - 1, false))
			count += 1;
		
		if (isActive(column, row - 1, false))
			count += 1;

		if (isActive(column + 1, row - 1, false))
			count += 1;

		if (isActive(column + 1, row, false))
			count += 1;

		if (isActive(column + 1, row + 1, false))
			count += 1;

		if (isActive(column, row + 1, false))
			count += 1;

		if (isActive(column - 1, row + 1, false))
			count += 1;

		if (isActive(column - 1, row, false))
			count += 1;
		
		return count;
	}
	
	public Boolean nextState(int column, int row)
	{
		int neighbors = this.neighborCount(column, row);
		
		boolean now = cells[column][row];
		
		if (neighbors < 2 && now)
			return false;
		else if (neighbors > 3 && now)
			return false;
		else if ((neighbors == 2 || neighbors == 3) && now)
			return true;
		else if (now == false && neighbors == 3)
			return true;
		else
			return false;
	}

	public void clear() 
	{
		cells = new boolean[0][];
	}
}
