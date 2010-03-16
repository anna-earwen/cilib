/**
 * Copyright (C) 2003 - 2009
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
 */
package net.sourceforge.cilib.type.parser;

import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;

/**
 *
 * @author Gary Pampara
 *
 */
final class T implements TypeCreator {
    private static final long serialVersionUID = 1198714503772193216L;

    /**
     * {@inheritDoc}
     */
    public Type create() {
        return new StringType("");
    }

    /**
     * {@inheritDoc}
     */
    public Type create(double value) {
        throw new UnsupportedOperationException("StringTypes with single values do not exist");
    }

    /**
     * {@inheritDoc}
     */
    public Type create(final Bounds bounds) {
        throw new UnsupportedOperationException("StringTypes with bounds do not exist");
    }

}