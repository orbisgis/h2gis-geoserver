/*
 * h2gis-gs is an extension to geoserver to connect H2GIS a spatial library 
 * that brings spatial support to the H2 Java database.
 *
 * h2gis-gs  is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2014-2015 IRSTV (FR CNRS 2488)
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

import com.vividsolutions.jts.io.ParseException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 *
 * @author Erwan Bocher
 */
public class H2GISTest extends H2GISDBTestSetUp{
    private Statement st;
    
    @Before
    public void setUpStatement() throws Exception {
        st = ds.getDataSource().getConnection().createStatement();
    }

    @After
    public void tearDownStatement() throws Exception {
        st.close();
    }
    
    @Test
    public void createSpatialTables() throws SQLException{
        st.execute("DROP SCHEMA IF EXISTS h2gis; COMMIT;");
        st.execute("CREATE SCHEMA h2gis;");
        st.execute("DROP TABLE IF EXISTS h2gis.geomtable; COMMIT;");      
        
        String sql = "CREATE TABLE h2gis.geomtable (id int AUTO_INCREMENT(1) PRIMARY KEY, "
            + "the_geom POINT)";
        st.execute(sql);
                
        sql = "INSERT INTO h2gis.geomtable VALUES ("
            + "0,ST_GeomFromText('POINT(12 0)',4326));";
        st.execute(sql);
        
        ResultSet rs = st.executeQuery("select count(id) from h2gis.geomtable");

        assertTrue(rs.next());
        assertTrue(rs.getInt(1) == 1);
        rs.close();

        rs = st.executeQuery("select * from h2gis.geomtable;");
        rs.next();
        assertTrue(rs.getInt(1)==0);
        assertEquals("POINT (12 0)",rs.getString(2) );
        rs.close();        
        st.execute("DROP TABLE h2gis.geomtable");
    }
    
    @Test
    public void findTables() throws SQLException, IOException {
        List<Name> names = ds.getNames();
        for (Name name : names) {

        }
    }    
    
    @Test
    public void getFeatureSchema() throws SQLException, IOException {
        st.execute("drop table if exists FORESTS");
        st.execute("CREATE TABLE FORESTS ( FID INTEGER, NAME CHARACTER VARYING(64),"
                + " THE_GEOM MULTIPOLYGON);"
                + "INSERT INTO FORESTS VALUES(109, 'Green Forest', ST_MPolyFromText( 'MULTIPOLYGON(((28 26,28 0,84 0,"
                + "84 42,28 26), (52 18,66 23,73 9,48 6,52 18)),((59 18,67 18,67 13,59 13,59 18)))', 101));");

        SimpleFeatureSource fs = (SimpleFeatureSource) ds.getFeatureSource("FORESTS");
        SimpleFeatureType schema = fs.getSchema();
        Query query = new Query(schema.getTypeName(), Filter.INCLUDE);
        assertEquals(1, fs.getCount(query));        
        assertEquals(3, schema.getAttributeCount());
        assertEquals("THE_GEOM", schema.getGeometryDescriptor().getLocalName());
        assertEquals("FID", schema.getDescriptor(0).getLocalName());
        assertEquals("NAME", schema.getDescriptor(1).getLocalName());
        assertEquals("THE_GEOM", schema.getDescriptor(2).getLocalName());
        st.execute("drop table FORESTS");
    }
    
    @Test
    public void getBoundingBox() throws SQLException, IOException, ParseException {
        st.execute("drop table if exists FORESTS");
        st.execute("CREATE TABLE FORESTS ( FID INTEGER, NAME CHARACTER VARYING(64),"
                + " THE_GEOM POLYGON);"
                + "INSERT INTO FORESTS VALUES(109, 'Green Forest', 'POLYGON((0 0,10 0,10 10, 0 10, 0 0))');");

        SimpleFeatureSource fs = (SimpleFeatureSource) ds.getFeatureSource("FORESTS");
        SimpleFeatureType schema = fs.getSchema();
        Query query = new Query(schema.getTypeName(), Filter.INCLUDE);
        ReferencedEnvelope bounds = fs.getBounds(query);
        if (bounds == null) {
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection = fs.getFeatures(query);
            bounds = collection.getBounds();
        }        
        assertTrue(JTS.toEnvelope(wKTReader.read("POLYGON((0 0,10 0,10 10, 0 10, 0 0))")).boundsEquals2D(bounds, 0.01));
                
        st.execute("drop table FORESTS");
    }
    
}
