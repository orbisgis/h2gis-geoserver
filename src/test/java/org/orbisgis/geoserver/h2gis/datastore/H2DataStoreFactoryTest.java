/*
 * h2gis-gs is an extension to geoserver to connect H2GIS a spatial library 
 * that brings spatial support to the H2 database engine.
 *
 * h2gis-gs  is distributed under GPL 3 license. It is produced by the DECIDE
 * team of the Lab-STICC laboratory <http://www.labsticc.fr/> CNRS UMR 6285.
 *
 * Copyright (C) 2015-2016 Lab-STICC (CNRS UMR 6285)
 *
 * h2gis-gs  is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * h2gis-gs  is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * h2gis-gs. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.geoserver.h2gis.datastore;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.factory.Hints;
import org.geotools.factory.Hints.Key;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import static org.geotools.jdbc.JDBCDataStoreFactory.DATABASE;
import static org.geotools.jdbc.JDBCDataStoreFactory.HOST;
import static org.geotools.jdbc.JDBCDataStoreFactory.PASSWD;
import static org.geotools.jdbc.JDBCDataStoreFactory.PORT;
import static org.geotools.jdbc.JDBCDataStoreFactory.USER;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class H2DataStoreFactoryTest  {
    H2GISDataStoreFactory factory;
    HashMap params;
    
    @Before
    public void setUp() throws Exception {
        factory = new H2GISDataStoreFactory();
        factory.setBaseDirectory(new File("./testH2"));
        params = new HashMap();
        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/test");
        params.put(JDBCDataStoreFactory.DATABASE.key, "h2gis");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "h2gis");
        
    }

    @Test
    public void testCanProcess() throws Exception {
        assertFalse(factory.canProcess(Collections.EMPTY_MAP));
        assertTrue(factory.canProcess(params));
    }
    
    @Test
    public void testCreateDataStore() throws Exception {
        JDBCDataStore ds = factory.createDataStore( params );
        assertNotNull( ds );
        assertTrue(ds.getDataSource() instanceof ManageableDataSource);
    }

    @Test
    public void testCreateDataStoreMVCC() throws Exception {
        Map clonedParams = new HashMap(params);
        clonedParams.put(H2GISDataStoreFactory.MVCC.key, true);
        JDBCDataStore ds = factory.createDataStore(clonedParams);
        assertNotNull(ds);
        final DataSource source = ds.getDataSource();
        assertNotNull(source);
        final DataSource wrapped = source.unwrap(DataSource.class);
        assertNotNull(wrapped);
        if (wrapped instanceof BasicDataSource) {
            final BasicDataSource basicSource = (BasicDataSource) wrapped;
            final String url = basicSource.getUrl();
            assertTrue(url.contains("MVCC=true"));
        }
    }
    
    
//    public void testSimplifyParameterDisabled() throws Exception {
//        H2GISDataStoreFactory factory = new H2GISDataStoreFactory();
//        Properties db = ;
//        
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put(HOST.key, db.getProperty(HOST.key));
//        params.put(DATABASE.key, db.getProperty(DATABASE.key));
//        params.put(PORT.key, db.getProperty(PORT.key));
//        params.put(USER.key, db.getProperty(USER.key));
//        params.put(PASSWD.key, db.getProperty(PASSWD.key));
//        
//        // force simplify off
//        params.put(H2GISDataStoreFactory.SIMPLIFY.key, false);
//        JDBCDataStore store = factory.createDataStore(params);
//        assertNotNull(store);
//        try {
//            // check dialect
//            H2GISDialect dialect = (H2GISDialect) store.getSQLDialect();
//            assertFalse(dialect.isSimplifyEnabled());
//            Set<Hints.Key> baseHints = new HashSet<Key>();
//            dialect.addSupportedHints(baseHints);
//            assertTrue(baseHints.isEmpty());
//        } finally {
//            store.dispose();
//        }
//    }
//    
//    public void testSimplifyParameter() throws Exception {
//        H2GISDataStoreFactory factory = new H2GISDataStoreFactory();
//        Properties db = fixture;
//        
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put(HOST.key, db.getProperty(HOST.key));
//        params.put(DATABASE.key, db.getProperty(DATABASE.key));
//        params.put(PORT.key, db.getProperty(PORT.key));
//        params.put(USER.key, db.getProperty(USER.key));
//        params.put(PASSWD.key, db.getProperty(PASSWD.key));
//        
//        // do not specify simplify, on by default
//        JDBCDataStore store = factory.createDataStore(params);
//        assertNotNull(store);
//        try {
//            // check dialect
//            H2GISDialect dialect = (H2GISDialect) store.getSQLDialect();
//            assertTrue(dialect.isSimplifyEnabled());
//            Set<Hints.Key> baseHints = new HashSet<Key>();
//            dialect.addSupportedHints(baseHints);
//            assertFalse(baseHints.isEmpty());
//            assertTrue(baseHints.contains(Hints.GEOMETRY_SIMPLIFICATION));
//        } finally {
//            store.dispose();
//        }
//    }
}
