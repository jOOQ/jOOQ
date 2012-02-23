/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 *                          Christopher Deckers, chrriis@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.debugger.console.misc;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Christopher Deckers & others
 */
public class Utils {

	private Utils() {}

    public static enum DurationFormatPrecision {
        DAY,
        HOUR,
        MINUTE,
        SECOND,
        MILLISECOND,
    }

    public static String formatDuration(long duration) {
        return formatDuration(duration, DurationFormatPrecision.MILLISECOND);
    }

    public static String formatDuration(long duration, DurationFormatPrecision precision) {
        StringBuilder sb = new StringBuilder();
        if (duration < 0) {
            duration = -duration;
            sb.append("-");
        }
        if (duration / (24 * 1000 * 60 * 60) != 0 || precision == DurationFormatPrecision.DAY) {
            sb.append(duration / (24 * 1000 * 60 * 60)).append("d ");
            if (precision == DurationFormatPrecision.DAY) {
                return sb.toString().trim();
            }
        }
        if (duration / (1000 * 60 * 60) != 0 || precision == DurationFormatPrecision.HOUR) {
            sb.append((duration / (1000 * 60 * 60)) % 24).append("h ");
            if (precision == DurationFormatPrecision.HOUR) {
                return sb.toString().trim();
            }
        }
        if (duration / (1000 * 60) != 0 || precision == DurationFormatPrecision.MINUTE) {
            sb.append((duration / (1000 * 60)) % 60).append("min ");
            if (precision == DurationFormatPrecision.MINUTE) {
                return sb.toString().trim();
            }
        }
        if (duration / 1000 != 0 || precision == DurationFormatPrecision.SECOND) {
            sb.append((duration / 1000) % 60).append("s ");
            if (precision == DurationFormatPrecision.SECOND) {
                return sb.toString().trim();
            }
        }
        sb.append(duration % 1000).append("ms");
        return sb.toString();
    }

    public static String formatDateTimeTZ(Date date) {
    	// TODO: GMT vs TZ
    	return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(date);
    }

    public static String formatDateTimeGMT(Date date) {
    	// TODO: GMT vs TZ
    	return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(date);
    }

    public static String formatDateGMT(Date date) {
    	// TODO: GMT vs TZ
    	return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    public static boolean equals(Object o1, Object o2) {
        return (o1 == o2) || (o1 != null && o1.equals(o2));
    }

}
