/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
 *
 *
 *
 */
package org.jooq;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * [#20504] A {@link ByteArrayInputStream} over the internal <code>byte[]</code>
 * of an immutable {@link Data} value, e.g. {@link JSON#inputStream()} or
 * {@link JSONB#inputStream()}.
 * <p>
 * On some JDK versions (e.g. JDK 21),
 * {@link ByteArrayInputStream#transferTo(OutputStream)} passes its internal
 * buffer to arbitrary {@link OutputStream} implementations, which could then
 * modify it. This implementation copies the bytes in chunks instead.
 *
 * @author Lukas Eder
 */
final class ImmutableBytesInputStream extends ByteArrayInputStream {

    ImmutableBytesInputStream(byte[] buf) {
        super(buf);
    }

    // No @Override, for Java 8 compatibility
    public final long transferTo(OutputStream out) throws IOException {
        byte[] buffer = new byte[Math.min(8192, Math.max(1, available()))];
        long transferred = 0;
        int read;

        while ((read = read(buffer, 0, buffer.length)) > 0) {
            out.write(buffer, 0, read);
            transferred += read;
        }

        return transferred;
    }
}
