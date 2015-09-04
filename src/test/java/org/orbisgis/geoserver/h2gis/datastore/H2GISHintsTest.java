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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.Hints;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import static org.junit.Assert.*;

/**
 *
 * @author Erwan Bocher
 */
public class H2GISHintsTest {

    private Statement st;

    private static final String DB_NAME = "H2GISDBHINTSTest";

    private H2GISDataStoreFactory factory;
    private HashMap params;
    public JDBCDataStore ds;
    public WKTReader wKTReader;

    @Before
    public void setDatabase() throws Exception {
        factory = new H2GISDataStoreFactory();
        factory.setBaseDirectory(new File(getDataBasePath(DB_NAME)));
        params = new HashMap();
        params.put(JDBCDataStoreFactory.NAMESPACE.key, "http://www.geotools.org/h2gishints");
        params.put(JDBCDataStoreFactory.DATABASE.key, "h2gishints");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "h2gishints");
        params.put(JDBCDataStoreFactory.USER, "h2gishints");
        params.put(JDBCDataStoreFactory.PASSWD, "h2gishints");
        params.put(H2GISDataStoreFactory.SIMPLIFY.key, true);
        ds = factory.createDataStore(params);
        wKTReader = new WKTReader();
        ds.getDataSource();
        st = ds.getDataSource().getConnection().createStatement();
    }

    /**
     * Generate a path for the database
     *
     * @param dbName
     * @return
     */
    private static String getDataBasePath(String dbName) {
        if (dbName.startsWith("file://")) {
            return new File(URI.create(dbName)).getAbsolutePath();
        } else {
            return new File("target/test-resources/" + dbName).getAbsolutePath();
        }
    }

    @After
    public void tearDown() throws Exception {
        ds.getDataSource().getConnection().close();
        st.close();
    }
    
    
    @Test
    public void getFeatureSimplified() throws SQLException, IOException, ParseException {
        st.execute("drop table if exists LANDCOVER");
        st.execute("CREATE TABLE LANDCOVER ( FID INTEGER, NAME CHARACTER VARYING(64),"
                + " THE_GEOM POLYGON);"
                + "INSERT INTO LANDCOVER VALUES(1, 'Green Forest', 'POLYGON ((200 340, 90 250, 150 160, 266 165, 310 290, 200 340))');");

        Query query = new Query("LANDCOVER");
        query.getHints().add(new RenderingHints(Hints.GEOMETRY_SIMPLIFICATION, 100.0));

        SimpleFeatureSource fs = (SimpleFeatureSource) ds.getFeatureSource("LANDCOVER");
        SimpleFeatureCollection features = fs.getFeatures(query);

        SimpleFeatureIterator iterator = features.features();

        try {
            SimpleFeature feature = iterator.next();
            Geometry geom = (Geometry) feature.getDefaultGeometry();
            assertTrue(geom.equals(wKTReader.read("POLYGON ((200 340, 266 165, 90 250, 200 340))")));
        } finally {
            iterator.close();
        }
        st.execute("drop table LANDCOVER");
    }

}
