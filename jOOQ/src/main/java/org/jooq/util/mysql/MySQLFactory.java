/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import java.sql.Connection;

import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.SchemaMapping;
import org.jooq.impl.Factory;

/**
 * A {@link SQLDialect#MYSQL} specific factory
 *
 * @author Lukas Eder
 */
public class MySQLFactory extends Factory {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -8433470252008110069L;

    /**
     * Create a factory with connection and a schema mapping configured
     *
     * @param connection The connection to use with objects created from this
     *            factory
     * @param mapping The schema mapping to use with objects created from this
     *            factory
     */
    public MySQLFactory(Connection connection, SchemaMapping mapping) {
        super(connection, SQLDialect.MYSQL, mapping);
    }

    /**
     * Create a factory with connection
     *
     * @param connection The connection to use with objects created from this
     *            factory
     */
    public MySQLFactory(Connection connection) {
        super(connection, SQLDialect.MYSQL);
    }

    // -------------------------------------------------------------------------
    // MySQL-specific compression and encryption functions
    // -------------------------------------------------------------------------

    /**
     * Get the MySQL-specific <code>DECODE()</code> function
     * <p>
     * Don't mix this up with the various {@link Factory#decode()} methods!
     */
    public final Field<String> decode(String cryptString, String keyString) {
        return decode(val(cryptString), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>DECODE()</code> function
     * <p>
     * Don't mix this up with the various {@link Factory#decode()} methods!
     */
    public final Field<String> decode(Field<String> cryptString, Field<String> keyString) {
        return function("decode", String.class, cryptString, keyString);
    }

    /**
     * Get the MySQL-specific <code>ENCODE()</code> function
     */
    public final Field<String> encode(String string, String keyString) {
        return encode(val(string), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>ENCODE()</code> function
     */
    public final Field<String> encode(Field<String> string, Field<String> keyString) {
        return function("encode", String.class, string, keyString);
    }

    /**
     * Get the MySQL-specific <code>AES_DECRYPT()</code> function
     */
    public final Field<String> aesDecrypt(String cryptString, String keyString) {
        return aesDecrypt(val(cryptString), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>AES_DECRYPT()</code> function
     */
    public final Field<String> aesDecrypt(Field<String> cryptString, Field<String> keyString) {
        return function("aes_decrypt", String.class, cryptString, keyString);
    }

    /**
     * Get the MySQL-specific <code>AES_ENCRYPT()</code> function
     */
    public final Field<String> aesEncrypt(String string, String keyString) {
        return aesEncrypt(val(string), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>AES_ENCRYPT()</code> function
     */
    public final Field<String> aesEncrypt(Field<String> string, Field<String> keyString) {
        return function("aes_encrypt", String.class, string, keyString);
    }

    /**
     * Get the MySQL-specific <code>DES_DECRYPT()</code> function
     */
    public final Field<String> desDecrypt(String cryptString) {
        return desDecrypt(val(cryptString));
    }

    /**
     * Get the MySQL-specific <code>DES_DECRYPT()</code> function
     */
    public final Field<String> desDecrypt(Field<String> cryptString) {
        return function("des_decrypt", String.class, cryptString);
    }

    /**
     * Get the MySQL-specific <code>DES_DECRYPT()</code> function
     */
    public final Field<String> desDecrypt(String cryptString, String keyString) {
        return desDecrypt(val(cryptString), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>DES_DECRYPT()</code> function
     */
    public final Field<String> desDecrypt(Field<String> cryptString, Field<String> keyString) {
        return function("des_decrypt", String.class, cryptString, keyString);
    }

    /**
     * Get the MySQL-specific <code>DES_ENCRYPT()</code> function
     */
    public final Field<String> desEncrypt(String string) {
        return desEncrypt(val(string));
    }

    /**
     * Get the MySQL-specific <code>DES_ENCRYPT()</code> function
     */
    public final Field<String> desEncrypt(Field<String> string) {
        return function("des_encrypt", String.class, string);
    }

    /**
     * Get the MySQL-specific <code>DES_ENCRYPT()</code> function
     */
    public final Field<String> desEncrypt(String string, String keyString) {
        return desEncrypt(val(string), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>DES_ENCRYPT()</code> function
     */
    public final Field<String> desEncrypt(Field<String> string, Field<String> keyString) {
        return function("des_encrypt", String.class, string, keyString);
    }

    /**
     * Get the MySQL-specific <code>COMPRESS()</code> function
     */
    public final Field<String> compress(String string) {
        return compress(val(string));
    }

    /**
     * Get the MySQL-specific <code>COMPRESS()</code> function
     */
    public final Field<String> compress(Field<String> string) {
        return function("compress", String.class, string);
    }

    /**
     * Get the MySQL-specific <code>UNCOMPRESS()</code> function
     */
    public final Field<String> uncompress(String string) {
        return uncompress(val(string));
    }

    /**
     * Get the MySQL-specific <code>UNCOMPRESS()</code> function
     */
    public final Field<String> uncompress(Field<String> string) {
        return function("uncompress", String.class, string);
    }

    /**
     * Get the MySQL-specific <code>UNCOMPRESSED_LENGTH()</code> function
     */
    public final Field<Integer> uncompressedLength(String string) {
        return uncompressedLength(val(string));
    }

    /**
     * Get the MySQL-specific <code>UNCOMPRESSED_LENGTH()</code> function
     */
    public final Field<Integer> uncompressedLength(Field<String> string) {
        return function("uncompressed_length", Integer.class, string);
    }

    /**
     * Get the MySQL-specific <code>MD5()</code> function
     */
    public final Field<String> md5(String string) {
        return md5(val(string));
    }

    /**
     * Get the MySQL-specific <code>MD5()</code> function
     */
    public final Field<String> md5(Field<String> string) {
        return function("md5", String.class, string);
    }

    /**
     * Get the MySQL-specific <code>SHA1()</code> function
     */
    public final Field<String> sha1(String string) {
        return sha1(val(string));
    }

    /**
     * Get the MySQL-specific <code>SHA1()</code> function
     */
    public final Field<String> sha1(Field<String> string) {
        return function("sha1", String.class, string);
    }

    /**
     * Get the MySQL-specific <code>SHA2()</code> function
     */
    public final Field<String> sha2(String string, int hashLength) {
        return sha2(val(string), val(hashLength));
    }

    /**
     * Get the MySQL-specific <code>SHA2()</code> function
     */
    public final Field<String> sha2(Field<String> string, Field<Integer> hashLength) {
        return function("sha2", String.class, string, hashLength);
    }

    /**
     * Get the MySQL-specific <code>PASSWORD()</code> function
     */
    public final Field<String> password(String string) {
        return password(val(string));
    }

    /**
     * Get the MySQL-specific <code>PASSWORD()</code> function
     */
    public final Field<String> password(Field<String> string) {
        return function("password", String.class, string);
    }
}
