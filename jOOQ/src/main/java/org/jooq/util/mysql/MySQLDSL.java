/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.util.mysql;

import static org.jooq.SQLDialect.MYSQL;

import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.Support;
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
    protected MySQLDSL() {
    }

    // -------------------------------------------------------------------------
    // MySQL-specific compression and encryption functions
    // -------------------------------------------------------------------------

    /**
     * Get the MySQL-specific <code>DECODE()</code> function.
     * <p>
     * Don't mix this up with the various {@link DSL#decode()} methods!
     */
    @Support({ MYSQL })
    public static Field<String> decode(String cryptString, String keyString) {
        return decode(val(cryptString), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>DECODE()</code> function.
     * <p>
     * Don't mix this up with the various {@link DSL#decode()} methods!
     */
    @Support({ MYSQL })
    public static Field<byte[]> decode(byte[] cryptString, byte[] keyString) {
        return decode(val(cryptString), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>DECODE()</code> function.
     * <p>
     * Don't mix this up with the various {@link DSL#decode()} methods!
     */
    @Support({ MYSQL })
    public static <T> Field<T> decode(Field<T> cryptString, Field<T> keyString) {
        return function("decode", cryptString.getType(), cryptString, keyString);
    }

    /**
     * Get the MySQL-specific <code>ENCODE()</code> function.
     */
    @Support({ MYSQL })
    public static Field<String> encode(String string, String keyString) {
        return encode(val(string), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>ENCODE()</code> function.
     */
    @Support({ MYSQL })
    public static Field<byte[]> encode(byte[] string, byte[] keyString) {
        return encode(val(string), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>ENCODE()</code> function.
     */
    @Support({ MYSQL })
    public static <T> Field<T> encode(Field<T> string, Field<T> keyString) {
        return function("encode", string.getType(), string, keyString);
    }

    /**
     * Get the MySQL-specific <code>AES_DECRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static Field<String> aesDecrypt(String cryptString, String keyString) {
        return aesDecrypt(val(cryptString), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>AES_DECRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static Field<byte[]> aesDecrypt(byte[] cryptString, byte[] keyString) {
        return aesDecrypt(val(cryptString), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>AES_DECRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static <T> Field<T> aesDecrypt(Field<T> cryptString, Field<T> keyString) {
        return function("aes_decrypt", cryptString.getType(), cryptString, keyString);
    }

    /**
     * Get the MySQL-specific <code>AES_ENCRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static Field<String> aesEncrypt(String string, String keyString) {
        return aesEncrypt(val(string), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>AES_ENCRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static Field<byte[]> aesEncrypt(byte[] string, byte[] keyString) {
        return aesEncrypt(val(string), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>AES_ENCRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static <T> Field<T> aesEncrypt(Field<T> string, Field<T> keyString) {
        return function("aes_encrypt", string.getType(), string, keyString);
    }

    /**
     * Get the MySQL-specific <code>DES_DECRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static Field<String> desDecrypt(String cryptString) {
        return desDecrypt(val(cryptString));
    }

    /**
     * Get the MySQL-specific <code>DES_DECRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static Field<byte[]> desDecrypt(byte[] cryptString) {
        return desDecrypt(val(cryptString));
    }

    /**
     * Get the MySQL-specific <code>DES_DECRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static <T> Field<T> desDecrypt(Field<T> cryptString) {
        return function("des_decrypt", cryptString.getType(), cryptString);
    }

    /**
     * Get the MySQL-specific <code>DES_DECRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static Field<String> desDecrypt(String cryptString, String keyString) {
        return desDecrypt(val(cryptString), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>DES_DECRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static Field<byte[]> desDecrypt(byte[] cryptString, byte[] keyString) {
        return desDecrypt(val(cryptString), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>DES_DECRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static <T> Field<T> desDecrypt(Field<T> cryptString, Field<T> keyString) {
        return function("des_decrypt", cryptString.getType(), cryptString, keyString);
    }

    /**
     * Get the MySQL-specific <code>DES_ENCRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static Field<String> desEncrypt(String string) {
        return desEncrypt(val(string));
    }

    /**
     * Get the MySQL-specific <code>DES_ENCRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static Field<byte[]> desEncrypt(byte[] string) {
        return desEncrypt(val(string));
    }

    /**
     * Get the MySQL-specific <code>DES_ENCRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static <T> Field<T> desEncrypt(Field<T> string) {
        return function("des_encrypt", string.getType(), string);
    }

    /**
     * Get the MySQL-specific <code>DES_ENCRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static Field<String> desEncrypt(String string, String keyString) {
        return desEncrypt(val(string), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>DES_ENCRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static Field<byte[]> desEncrypt(byte[] string, byte[] keyString) {
        return desEncrypt(val(string), val(keyString));
    }

    /**
     * Get the MySQL-specific <code>DES_ENCRYPT()</code> function.
     */
    @Support({ MYSQL })
    public static <T> Field<T> desEncrypt(Field<T> string, Field<T> keyString) {
        return function("des_encrypt", string.getType(), string, keyString);
    }

    /**
     * Get the MySQL-specific <code>COMPRESS()</code> function.
     */
    @Support({ MYSQL })
    public static Field<String> compress(String string) {
        return compress(val(string));
    }

    /**
     * Get the MySQL-specific <code>COMPRESS()</code> function.
     */
    @Support({ MYSQL })
    public static Field<byte[]> compress(byte[] string) {
        return compress(val(string));
    }

    /**
     * Get the MySQL-specific <code>COMPRESS()</code> function.
     */
    @Support({ MYSQL })
    public static <T> Field<T> compress(Field<T> string) {
        return function("compress", string.getType(), string);
    }

    /**
     * Get the MySQL-specific <code>UNCOMPRESS()</code> function.
     */
    @Support({ MYSQL })
    public static Field<String> uncompress(String string) {
        return uncompress(val(string));
    }

    /**
     * Get the MySQL-specific <code>UNCOMPRESS()</code> function.
     */
    @Support({ MYSQL })
    public static Field<byte[]> uncompress(byte[] string) {
        return uncompress(val(string));
    }

    /**
     * Get the MySQL-specific <code>UNCOMPRESS()</code> function.
     */
    @Support({ MYSQL })
    public static <T> Field<T> uncompress(Field<T> string) {
        return function("uncompress", string.getType(), string);
    }

    /**
     * Get the MySQL-specific <code>UNCOMPRESSED_LENGTH()</code> function.
     */
    @Support({ MYSQL })
    public static Field<Integer> uncompressedLength(String string) {
        return uncompressedLength(val(string));
    }

    /**
     * Get the MySQL-specific <code>UNCOMPRESSED_LENGTH()</code> function.
     */
    @Support({ MYSQL })
    public static Field<Integer> uncompressedLength(byte[] string) {
        return uncompressedLength(val(string));
    }

    /**
     * Get the MySQL-specific <code>UNCOMPRESSED_LENGTH()</code> function.
     */
    @Support({ MYSQL })
    public static <T> Field<Integer> uncompressedLength(Field<T> string) {
        return function("uncompressed_length", Integer.class, string);
    }

    /**
     * Get the MySQL-specific <code>SHA1()</code> function.
     */
    @Support({ MYSQL })
    public static Field<String> sha1(String string) {
        return sha1(val(string));
    }

    /**
     * Get the MySQL-specific <code>SHA1()</code> function.
     */
    @Support({ MYSQL })
    public static Field<byte[]> sha1(byte[] string) {
        return sha1(val(string));
    }

    /**
     * Get the MySQL-specific <code>SHA1()</code> function.
     */
    @Support({ MYSQL })
    public static <T> Field<T> sha1(Field<T> string) {
        return function("sha1", string.getType(), string);
    }

    /**
     * Get the MySQL-specific <code>SHA2()</code> function.
     */
    @Support({ MYSQL })
    public static Field<String> sha2(String string, int hashLength) {
        return sha2(val(string), val(hashLength));
    }

    /**
     * Get the MySQL-specific <code>SHA2()</code> function.
     */
    @Support({ MYSQL })
    public static Field<byte[]> sha2(byte[] string, int hashLength) {
        return sha2(val(string), val(hashLength));
    }

    /**
     * Get the MySQL-specific <code>SHA2()</code> function.
     */
    @Support({ MYSQL })
    public static <T> Field<T> sha2(Field<T> string, Field<Integer> hashLength) {
        return function("sha2", string.getType(), string, hashLength);
    }

    /**
     * Get the MySQL-specific <code>PASSWORD()</code> function.
     */
    @Support({ MYSQL })
    public static Field<String> password(String string) {
        return password(val(string));
    }

    /**
     * Get the MySQL-specific <code>PASSWORD()</code> function.
     */
    @Support({ MYSQL })
    public static Field<byte[]> password(byte[] string) {
        return password(val(string));
    }

    /**
     * Get the MySQL-specific <code>PASSWORD()</code> function.
     */
    @Support({ MYSQL })
    public static <T> Field<T> password(Field<T> string) {
        return function("password", string.getType(), string);
    }

    // -------------------------------------------------------------------------
    // Other functions
    // -------------------------------------------------------------------------

    /**
     * Get the MySQL-specific <code>VALUES()</code> function for use with
     * <code>INSERT .. ON DUPLICATE KEY UPDATE</code> statements.
     *
     * @see <a href=
     *      "http://dev.mysql.com/doc/refman/5.7/en/miscellaneous-functions.html#function_values">
     *      http://dev.mysql.com/doc/refman/5.7/en/miscellaneous-functions.html#
     *      function_values</a>
     */
    @Support({ MYSQL })
    public static <T> Field<T> values(Field<T> values) {
        return function("values", values.getDataType(), values);
    }

    // -------------------------------------------------------------------------
    // Other utilities
    // -------------------------------------------------------------------------

    /**
     * Get a field based {@link EnumType} by its MySQL-specific index.
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
