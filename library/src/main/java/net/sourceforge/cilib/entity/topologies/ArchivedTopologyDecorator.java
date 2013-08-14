/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import java.util.Iterator;
import java.util.NoSuchElementException;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.AbstractTopology;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.IndexedIterator;

/**
 * Store particle information on the hard drive in tmp files: this decorator is necessary
 * for memory-consuming tasks such as deep NN learning.
 *
 * @param <E> The {@linkplain Entity} type.
 */
public class ArchivedTopologyDecorator<E extends Entity> extends AbstractTopology<E> {
    private static final long serialVersionUID = 93039445052676571L;
    private AbstractTopology<E> delegate;

    /**
     * Default constructor. The default {@link #neighbourhoodSize} is 3.
     */
    public ArchivedTopologyDecorator() {
        super();
    }

    @Override
    public E get(int index) {
        return delegate.get(index);
    }
    /**
     * Copy constructor..
     */
    public ArchivedTopologyDecorator(ArchivedTopologyDecorator<E> copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArchivedTopologyDecorator<E> getClone() {
        return new ArchivedTopologyDecorator<E>(this);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	@Override
    public Iterator<E> neighbourhood(Iterator<? extends Entity> iterator) {
        return new LBestNeighbourhoodIterator<E>(this, (IndexedIterator<E>) iterator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNeighbourhoodSize(ControlParameter neighbourhoodSize) {
        this.neighbourhoodSize = neighbourhoodSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNeighbourhoodSize() {
        int rounded = Long.valueOf(Math.round(neighbourhoodSize.getParameter())).intValue();

        if (size() == 0) // to show a sensible default value in CiClops
            return rounded;

        return Math.min(rounded, size());
    }
    
    /**
     * Iterator to traverse the lBest topology.
     */
    protected class LBestNeighbourhoodIterator<T extends Entity> extends NeighbourhoodIterator<T> {
        protected int count;

        public LBestNeighbourhoodIterator(AbstractTopology<T> topology, IndexedIterator<T> iterator) {
            super(topology, iterator);
            
            this.count = 0;
            this.index = iterator.getIndex() - (topology.getNeighbourhoodSize() / 2) - 1;
            
            if (index < 0) {
                index += topology.size();
            }
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public boolean hasNext() {
            return (count != topology.getNeighbourhoodSize());
        }

        @Override
        public T next() {
            if (count == topology.getNeighbourhoodSize()) {
                throw new NoSuchElementException();
            }
            ++index;
            ++count;
            if (index == topology.size()) {
               index = 0;
            }
            return topology.get(index);
        }

        @Override
        public void remove() {
            topology.remove(index);
            --index;
            if (index < 0) {
                index += topology.size();
            }
        }
    }
}
