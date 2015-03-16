///* (c) 2014 Open Source Geospatial Foundation - all rights reserved
// * (c) 2001 - 2013 OpenPlans
// * This code is licensed under the GPL 2.0 license, available at the root
// * application directory.
// */
//package org.orbisgis.geoserver.h2gis;
//
//import java.util.HashMap;
//import org.orbisgis.geoserver.h2gis.datastore.H2GISDataStoreFactory;
//import org.geoserver.test.GeoServerSystemTestSupport;
//import org.geotools.data.DataAccessFactory;
//import static org.junit.Assert.*;
//
//import org.junit.Test;
//import org.vfny.geoserver.util.DataStoreUtils;
//
//public class H2DataStoreInitializerTest extends GeoServerSystemTestSupport {
//
//    @Test
//    public void testDataStoreFactoryInitialized() {
//        HashMap params = new HashMap();
//        params.put( H2GISDataStoreFactory.DBTYPE.key, "h2");
//        params.put( H2GISDataStoreFactory.DATABASE.key, "test" );
//        
//        DataAccessFactory f = DataStoreUtils.aquireFactory( params );
//        assertNotNull( f );
//        assertTrue( f instanceof H2GISDataStoreFactory );
//        
//        assertEquals( testData.getDataDirectoryRoot(), ((H2GISDataStoreFactory)f).getBaseDirectory() );
//        
//    }
//}
