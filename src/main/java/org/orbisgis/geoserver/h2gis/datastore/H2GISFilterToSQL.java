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
import java.io.IOException;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.filter.FilterCapabilities;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BinarySpatialOperator;

/**
 * Class to use the H2GIS function with FES
 * 
 * @author Erwan Bocher
 */
public class H2GISFilterToSQL extends FilterToSQL {

    H2GISFilterToSQLHelper h2GISFilterToSQLHelper;
    private boolean functionEncodingEnabled;

    public H2GISFilterToSQL(H2GISDialect dialect) {
        h2GISFilterToSQLHelper = new H2GISFilterToSQLHelper(this);
    }   

    @Override
    protected void visitLiteralGeometry(Literal expression) throws IOException {
        // evaluate the literal and store it for later
        Geometry geom = (Geometry) evaluateLiteral(expression, Geometry.class);
        out.write("ST_GeomFromText('");
        out.write(geom.toText());
        if (currentSRID == null && currentGeometry != null) {
        // if we don't know at all, use the srid of the geometry we're comparing against
        // (much slower since that has to be extracted record by record as opposed to
        // being a constant)
            out.write("', ST_SRID(\"" + currentGeometry.getLocalName() + "\"))");
        } else {
            out.write("', " + currentSRID + ")");
        }
    }

    @Override
    protected FilterCapabilities createFilterCapabilities() {
        return H2GISFilterToSQLHelper.createFilterCapabilities(functionEncodingEnabled);
    }

    @Override
    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter,
            PropertyName property, Literal geometry, boolean swapped,
            Object extraData) {
        h2GISFilterToSQLHelper.out = out;
        return h2GISFilterToSQLHelper.visitBinarySpatialOperator(filter, property, geometry,
                swapped, extraData);
    }

    @Override
    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter, Expression e1,
            Expression e2, Object extraData) {
        h2GISFilterToSQLHelper.out = out;
        return h2GISFilterToSQLHelper.visitBinarySpatialOperator(filter, e1, e2, extraData);
    }   

    @Override
    public Object visit(Function function, Object extraData) throws RuntimeException {
        h2GISFilterToSQLHelper.out = out;
        try {
            encodingFunction = true;
            boolean encoded = h2GISFilterToSQLHelper.visitFunction(function, extraData);
            encodingFunction = false;
            if (encoded) {
                return extraData;
            } else {
                return super.visit(function, extraData);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getFunctionName(Function function) {
        return H2GISFilterToSQLHelper.getFunctionName(function);
    }

    @Override
    protected String cast(String encodedProperty, Class target) throws IOException {
        return h2GISFilterToSQLHelper.cast(encodedProperty, target);
    }

    /**
     * 
     * @param functionEncodingEnabled 
     */
    public void setFunctionEncodingEnabled(boolean functionEncodingEnabled) {
        this.functionEncodingEnabled = functionEncodingEnabled;
    }
}
