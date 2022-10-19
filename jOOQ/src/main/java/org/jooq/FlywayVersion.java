/*
 * Copyright 2010-2019 Boxfuse GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jooq;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jooq.tools.StringUtils;

/**
 * A version of a migration.
 * <p>
 * [#8939] Minor adaptations have been made to use this class in jOOQ directly.
 *
 * @author Axel Fontaine
 */
final class FlywayVersion implements Comparable<FlywayVersion> {
    /**
     * Version for an empty schema.
     */
    public static final FlywayVersion EMPTY = new FlywayVersion(null);

    /**
     * Regex for matching proper version format
     */
    private static final Pattern SPLIT_REGEX = Pattern.compile("\\.(?=\\d)");

    /**
     * The individual parts this version string is composed of. Ex. 1.2.3.4.0 -> [1, 2, 3, 4, 0]
     */
    private final List<BigInteger> versionParts;

    /**
     * Create a FlywayVersion from a version String.
     *
     * @param version The version String. The value {@code current} will be interpreted as FlywayVersion.CURRENT,
     *                a marker for the latest version that has been applied to the database.
     * @return The FlywayVersion
     */
    public static FlywayVersion fromVersion(String version) {
        if (StringUtils.isEmpty(version)) return EMPTY;
        return new FlywayVersion(version);
    }

    /**
     * Creates a Version using this version string.
     *
     * @param version The version in one of the following formats: 6, 6.0, 005, 1.2.3.4, 201004200021. <br>{@code null}
     *                means that this version refers to an empty schema.
     */
    private FlywayVersion(String version) {
        if (!StringUtils.isEmpty(version)) {
            String normalizedVersion = version.replace('_', '.');
            this.versionParts = tokenize(normalizedVersion);
        }
        else {
            this.versionParts = new ArrayList<>();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlywayVersion version1 = (FlywayVersion) o;

        return compareTo(version1) == 0;
    }

    @Override
    public int hashCode() {
        return versionParts == null ? 0 : versionParts.hashCode();
    }

    @Override
    public int compareTo(FlywayVersion o) {
        if (o == null) {
            return 1;
        }

        if (this == EMPTY) {
            if (o == EMPTY) return 0;
            else return Integer.MIN_VALUE;
        }

        if (o == EMPTY) {
            return Integer.MAX_VALUE;
        }

        final List<BigInteger> parts1 = versionParts;
        final List<BigInteger> parts2 = o.versionParts;
        int largestNumberOfParts = Math.max(parts1.size(), parts2.size());
        for (int i = 0; i < largestNumberOfParts; i++) {
            final int compared = getOrZero(parts1, i).compareTo(getOrZero(parts2, i));
            if (compared != 0) {
                return compared;
            }
        }
        return 0;
    }

    private BigInteger getOrZero(List<BigInteger> elements, int i) {
        return i < elements.size() ? elements.get(i) : BigInteger.ZERO;
    }

    /**
     * Splits this string into list of Long
     *
     * @param versionStr The string to split.
     * @return The resulting array.
     */
    private List<BigInteger> tokenize(String versionStr) {
        List<BigInteger> parts = new ArrayList<>();
        for (String part : SPLIT_REGEX.split(versionStr)) {
            parts.add(new BigInteger(part));
        }

        for (int i = parts.size() - 1; i > 0; i--) {
            if (!parts.get(i).equals(BigInteger.ZERO)) {
                break;
            }
            parts.remove(i);
        }

        return parts;
    }
}
