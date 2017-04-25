/*
 * h2gis-geoserver is an extension to geoserver to connect H2GIS a spatial library 
 * that brings spatial support to the H2 database engine.
 *
 * h2gis-geoserver  is distributed under GPL 3 license. It is produced by the DECIDE
 * team of the Lab-STICC laboratory <http://www.labsticc.fr/> CNRS UMR 6285.
 *
 * Copyright (C) 2015-2017 Lab-STICC (CNRS UMR 6285)
 *
 * h2gis-geoserver  is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * h2gis-geoserver  is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * h2gis-geoserver. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.h2gis.org/>
 * or contact directly:
 * info_at_ h2gis.org
 */
package org.h2gis.geoserver;

import org.geoserver.data.DataStoreFactoryInitializer;
import org.geoserver.platform.GeoServerResourceLoader;
import org.h2gis.geotools.H2GISDataStoreFactory;

/**
 * Initializes an H2GIS data store factory setting its location to the geoserver
 *  data directory.
 *
 * @author Erwan Bocher
 *
 */
public class H2GISDataStoreFactoryInitializer extends 
    DataStoreFactoryInitializer<H2GISDataStoreFactory> {

    GeoServerResourceLoader resourceLoader;
    
    /**
     * Load the H2GISDataStore
     * 
     */
    public H2GISDataStoreFactoryInitializer() {
        super( H2GISDataStoreFactory.class );
    }
    
    /**
     * Init the GeoServer resource loader
     * @param resourceLoader 
     */
    public void setResourceLoader(GeoServerResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    @Override
    public void initialize(H2GISDataStoreFactory factory) {
        factory.setBaseDirectory(resourceLoader.getBaseDirectory() );
    }
}
