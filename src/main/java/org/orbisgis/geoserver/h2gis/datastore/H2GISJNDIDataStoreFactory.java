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


import java.io.File;
import java.util.Map;
import static org.orbisgis.geoserver.h2gis.datastore.H2GISDataStoreFactory.ASSOCIATIONS;
import org.geotools.jdbc.JDBCJNDIDataStoreFactory;

/**
 * JNDI DataStoreFactory for H2GIS. 
 * 
 * 
 */
public class H2GISJNDIDataStoreFactory extends JDBCJNDIDataStoreFactory {

    public H2GISJNDIDataStoreFactory() {
        super(new H2GISDataStoreFactory());
    }
 
    /**
     * @param baseDirectory
     * @see H2DataStoreFactory#setBaseDirectory(File)
     */
    public void setBaseDirectory(File baseDirectory) {
        ((H2GISDataStoreFactory)delegate).setBaseDirectory(baseDirectory);
    }

    /**
     * @return 
     * @see H2DataStoreFactory#getBaseDirectory()
     */
    public File getBaseDirectory() {
        return ((H2GISDataStoreFactory)delegate).getBaseDirectory();
    }
    
    @Override
    protected void setupParameters(Map parameters) {
        super.setupParameters(parameters);
        parameters.put(ASSOCIATIONS.key, ASSOCIATIONS);
    }
}
