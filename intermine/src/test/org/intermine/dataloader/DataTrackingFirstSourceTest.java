package org.intermine.dataloader;

/*
 * Copyright (C) 2002-2005 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import org.intermine.model.datatracking.Source;
import org.intermine.model.datatracking.Field;
import org.intermine.objectstore.ObjectStoreWriter;
import org.intermine.objectstore.ObjectStoreWriterFactory;
import org.intermine.sql.DatabaseFactory;

import org.intermine.model.testmodel.Department;

import junit.framework.TestCase;

public class DataTrackingFirstSourceTest extends TestCase {
    protected DataTrackerFirstSource dt;
    protected Source source1, source2;

    public void setUp() throws Exception {
        dt = new DataTrackerFirstSource(DatabaseFactory.getDatabase("db.unittest"), 30, 10);
        source1 = dt.stringToSource("Source1");
    }

    public void tearDown() throws Exception {
        dt.close();
    }


    public void testGetSource() throws Exception {
        try {
            dt.getSource(new Integer(13), "name").getName();
            fail("expected IllegalStateException - no skelSource set");
        } catch (IllegalStateException e) {
        }

        // correct
        Source skelSource1 = dt.stringToSource("skel_" + source1.getName());
        dt.setSkelSource(skelSource1);
        assertEquals(skelSource1, dt.getSource(new Integer(13), "name"));

        // source must be a skeleton
        try {
            dt.setSkelSource(source1);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }
}
