/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.util.mysql;

import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 * The {@link SQLDialect#MYSQL} specific DSL.
 *
 * @author Lukas Eder
 */
public class MySQLDSL extends DSL {

    /**
     * No instances
     */
    private MySQLDSL() {
    }

    // -------------------------------------------------------------------------
    // MySQL-specific compression and encryption functions
    // -------------------------------------------------------------------------

    /**
     * Get the MySQL-specific <code>DECODE()</code> function
     * <p>
     * Don't mix this up with the various {@link DSL#decode()} methods!
     */
    public static Field<String> decode(String cryptString, String keyString) {
        return decode(val(cryptString), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>DECODE()</code> function
     * <p>
     * Don't mix this up with the various {@link DSL#decode()} methods!
     */
    public static Field<String> decode(Field<String> cryptString, Field<String> keyString) {
        return function("decode", String.class, cryptString, keyString);
    }

    /**
     * Get the MySQL-specific <code>ENCODE()</code> function
     */
    public static Field<String> encode(String string, String keyString) {
        return encode(val(string), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>ENCODE()</code> function
     */
    public static Field<String> encode(Field<String> string, Field<String> keyString) {
        return function("encode", String.class, string, keyString);
    }

    /**
     * Get the MySQL-specific <code>AES_DECRYPT()</code> function
     */
    public static Field<String> aesDecrypt(String cryptString, String keyString) {
        return aesDecrypt(val(cryptString), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>AES_DECRYPT()</code> function
     */
    public static Field<String> aesDecrypt(Field<String> cryptString, Field<String> keyString) {
        return function("aes_decrypt", String.class, cryptString, keyString);
    }

    /**
     * Get the MySQL-specific <code>AES_ENCRYPT()</code> function
     */
    public static Field<String> aesEncrypt(String string, String keyString) {
        return aesEncrypt(val(string), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>AES_ENCRYPT()</code> function
     */
    public static Field<String> aesEncrypt(Field<String> string, Field<String> keyString) {
        return function("aes_encrypt", String.class, string, keyString);
    }

    /**
     * Get the MySQL-specific <code>DES_DECRYPT()</code> function
     */
    public static Field<String> desDecrypt(String cryptString) {
        return desDecrypt(val(cryptString));
    }

    /**
     * Get the MySQL-specific <code>DES_DECRYPT()</code> function
     */
    public static Field<String> desDecrypt(Field<String> cryptString) {
        return function("des_decrypt", String.class, cryptString);
    }

    /**
     * Get the MySQL-specific <code>DES_DECRYPT()</code> function
     */
    public static Field<String> desDecrypt(String cryptString, String keyString) {
        return desDecrypt(val(cryptString), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>DES_DECRYPT()</code> function
     */
    public static Field<String> desDecrypt(Field<String> cryptString, Field<String> keyString) {
        return function("des_decrypt", String.class, cryptString, keyString);
    }

    /**
     * Get the MySQL-specific <code>DES_ENCRYPT()</code> function
     */
    public static Field<String> desEncrypt(String string) {
        return desEncrypt(val(string));
    }

    /**
     * Get the MySQL-specific <code>DES_ENCRYPT()</code> function
     */
    public static Field<String> desEncrypt(Field<String> string) {
        return function("des_encrypt", String.class, string);
    }

    /**
     * Get the MySQL-specific <code>DES_ENCRYPT()</code> function
     */
    public static Field<String> desEncrypt(String string, String keyString) {
        return desEncrypt(val(string), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>DES_ENCRYPT()</code> function
     */
    public static Field<String> desEncrypt(Field<String> string, Field<String> keyString) {
        return function("des_encrypt", String.class, string, keyString);
    }

    /**
     * Get the MySQL-specific <code>COMPRESS()</code> function
     */
    public static Field<String> compress(String string) {
        return compress(val(string));
    }

    /**
     * Get the MySQL-specific <code>COMPRESS()</code> function
     */
    public static Field<String> compress(Field<String> string) {
        return function("compress", String.class, string);
    }

    /**
     * Get the MySQL-specific <code>UNCOMPRESS()</code> function
     */
    public static Field<String> uncompress(String string) {
        return uncompress(val(string));
    }

    /**
     * Get the MySQL-specific <code>UNCOMPRESS()</code> function
     */
    public static Field<String> uncompress(Field<String> string) {
        return function("uncompress", String.class, string);
    }

    /**
     * Get the MySQL-specific <code>UNCOMPRESSED_LENGTH()</code> function
     */
    public static Field<Integer> uncompressedLength(String string) {
        return uncompressedLength(val(string));
    }

    /**
     * Get the MySQL-specific <code>UNCOMPRESSED_LENGTH()</code> function
     */
    public static Field<Integer> uncompressedLength(Field<String> string) {
        return function("uncompressed_length", Integer.class, string);
    }

    /**
     * Get the MySQL-specific <code>SHA1()</code> function
     */
    public static Field<String> sha1(String string) {
        return sha1(val(string));
    }

    /**
     * Get the MySQL-specific <code>SHA1()</code> function
     */
    public static Field<String> sha1(Field<String> string) {
        return function("sha1", String.class, string);
    }

    /**
     * Get the MySQL-specific <code>SHA2()</code> function
     */
    public static Field<String> sha2(String string, int hashLength) {
        return sha2(val(string), val(hashLength));
    }

    /**
     * Get the MySQL-specific <code>SHA2()</code> function
     */
    public static Field<String> sha2(Field<String> string, Field<Integer> hashLength) {
        return function("sha2", String.class, string, hashLength);
    }

    /**
     * Get the MySQL-specific <code>PASSWORD()</code> function
     */
    public static Field<String> password(String string) {
        return password(val(string));
    }

    /**
     * Get the MySQL-specific <code>PASSWORD()</code> function
     */
    public static Field<String> password(Field<String> string) {
        return function("password", String.class, string);
    }

    // -------------------------------------------------------------------------
    // Other utilities
    // -------------------------------------------------------------------------

    /**
     * Get a field based {@link EnumType} by its MySQL-specific index
     * <p>
     * If your MySQL enum type contains these three values: <code>A, B, C</code>
     * , then this will be the mapping of indexes to values:
     * <table border="1">
     * <tr>
     * <th>Enum literal as in {@link Enum#name()}</th>
     * <th>Enum ordinal as in {@link Enum#ordinal()}</th>
     * <th>MySQL index</th>
     * </tr>
     * <tr>
     * <td><code>null</code></td>
     * <td><code>-</code></td>
     * <td><code>0</code></td>
     * </tr>
     * <tr>
     * <td><code>A</code></td>
     * <td><code>0</code></td>
     * <td><code>1</code></td>
     * </tr>
     * <tr>
     * <td><code>B</code></td>
     * <td><code>1</code></td>
     * <td><code>2</code></td>
     * </tr>
     * <tr>
     * <td><code>C</code></td>
     * <td><code>2</code></td>
     * <td><code>3</code></td>
     * </tr>
     * </table>
     * <p>
     * See <a
     * href="dev.mysql.com/doc/refman/5.5/en/enum.html">dev.mysql.com/doc/
     * refman/5.5/en/enum.html</a> for more details about MySQL enum types
     */
    public static <E extends java.lang.Enum<E> & org.jooq.EnumType> E enumType(Class<E> type, int index) {
        if (index <= 0) {
            return null;
        }

        E[] values = type.getEnumConstants();
        if (index > values.length) {
            return null;
        }

        return values[index - 1];
    }
}
