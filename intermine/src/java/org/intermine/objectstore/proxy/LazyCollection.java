package org.intermine.objectstore.proxy;

/*
 * Copyright (C) 2002-2005 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.List;

import org.intermine.objectstore.ObjectStoreException;
import org.intermine.objectstore.query.Query;
import org.intermine.objectstore.query.ResultsInfo;

/**
 * Class which uses an ObjectStore to perform lazy fetching of data
 *
 * @author Matthew Wakeling
 */
public interface LazyCollection extends Lazy, List
{
    /**
     * Sets this LazyCollection to bypass the optimiser
     */
    public void setNoOptimise();

    /**
     * Sets this LazyCollection to bypass the explain check in ObjectStore.execute().
     */
    public void setNoExplain();

    /**
     * Returns the Query used by this LazyCollection
     * 
     * @return a Query
     */
    public Query getQuery();

    /**
     * Returns Returns the current best estimate of the characteristics of the LazyCollection
     *
     * @return a ResultsInfo object
     * @throws ObjectStoreException if an error occurs in the underlying ObjectStore
     */
    public ResultsInfo getInfo() throws ObjectStoreException;

    /**
     * Sets the number of rows requested from the ObjectStore whenever an execute call is made
     *
     * @param size the number of rows
     */
    public void setBatchSize(int size);
}

