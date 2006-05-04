/*
 * LoggingSingleton.java
 *
 * Created on May 2, 2006
 *
 * 
 * Copyright (C) 2003 - 2006 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA 
 *   
 */
package net.sourceforge.cilib.algorithm;

import org.apache.log4j.PropertyConfigurator;

/**
 * Initialisation singleton for logging
 * 
 * @author Gary Pampara
 */
public class LoggingSingleton {
	
	private static LoggingSingleton instance;
	
	/**
	 * Constructor that initialises the logging properties.
	 *
	 */
	private LoggingSingleton() {
		PropertyConfigurator.configure("log4j.properties");
	}
	
	public static LoggingSingleton initialise() {
		if (instance == null)
			instance = new LoggingSingleton();
		
		return instance;
	}

}
