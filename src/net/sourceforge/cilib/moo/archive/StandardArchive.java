/*
 * StandardArchive.java
 * 
 * Created on Apr 1, 2006
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
package net.sourceforge.cilib.moo.archive;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * 
 * @author Andries Engelbrecht
 *
 */

public class StandardArchive extends ArrayList<OptimisationSolution> implements Archive {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2395164492771478604L;
	
	private LocalGuideStrategy localGuideStrategy;
	private GlobalGuideStrategy globalGuideStrategy;

	public StandardArchive() {
		localGuideStrategy = new DominatesStrategy();
		globalGuideStrategy = null;
	}
	
	public void accept(OptimisationSolution candidateNonDominatedSolution) {
		
	}
	
	public void accept(Collection<OptimisationSolution> paretoFront) {
		
	}

	public GlobalGuideStrategy getGlobalGuideStrategy() {
		return globalGuideStrategy;
	}

	public void setGlobalGuideStrategy(GlobalGuideStrategy globalGuideStrategy) {
		this.globalGuideStrategy = globalGuideStrategy;
	}

	public LocalGuideStrategy getLocalGuideStrategy() {
		return localGuideStrategy;
	}

	public void setLocalGuideStrategy(LocalGuideStrategy localGuideStrategy) {
		this.localGuideStrategy = localGuideStrategy;
	}

	

}
