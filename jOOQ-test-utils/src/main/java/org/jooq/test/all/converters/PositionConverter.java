/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.test.all.converters;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.jooq.Converter;
import org.jooq.test.all.types.Position;

import org.postgis.Geometry;
import org.postgis.PGgeometry;
import org.postgis.Point;


/**
 * @author Lukas Eder
 * @autho Andreas Ahlenstorf
 * @see <a href=
 *      "https://groups.google.com/d/msg/jooq-user/TBQZCPTCvnk/HJYND9AQs6EJ">
 *      https://groups.google.com/d/msg/jooq-user/TBQZCPTCvnk/HJYND9AQs6EJ</a>
 */
public class PositionConverter implements Converter<Object, Position> {

    @Override
    public Position from(Object object) {
        if (object == null) {
            return null;
        }

        Geometry geometry = null;

        try {
            geometry = PGgeometry.geomFromString(object.toString());
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }

        if (!(geometry instanceof Point)) {
            throw new IllegalArgumentException("Geometry is not a org.postgis.Point.");
        }

        Point point = (Point) geometry;
        return new Position(BigDecimal.valueOf(point.getX()), BigDecimal.valueOf(point.getY()));
    }

    @Override
    public Object to(Position position) {
        if (position == null) {
            return null;
        }

        Point p = new Point(position.latitude.doubleValue(), position.longitude.doubleValue());
        p.setSrid(Position.SPATIAL_REF_SYS);
        return new PGgeometry(p);
    }

    @Override
    public Class<Object> fromType() {
        return Object.class;
    }

    @Override
    public Class<Position> toType() {
        return Position.class;
    }
}

