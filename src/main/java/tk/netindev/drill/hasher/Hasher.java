package tk.netindev.drill.hasher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author netindev
 * Source: https://github.com/netindev/drill
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class Hasher {

    public static native void slowHash(byte[] input, byte[] output);

    static {
        String library = null;
        final String system = System.getProperty("os.name").toLowerCase();

        try {
            if (system.indexOf("win") >= 0) {
                library = "/win/x64/cryptonight.dll";
            } else if (system.indexOf("nix") >= 0 || system.indexOf("nux") >= 0
                    || system.indexOf("aix") >= 0) {
                library = "/unix/x64/libcryptonight.so";
            } else {
                throw new UnsupportedOperatingSystemException(String.format("Operating system: '%s' is not supported", system));
            }
        } catch (UnsupportedOperatingSystemException e) {
            e.printStackTrace();
        }

        try {
            loadLibrary(library);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadLibrary(String name) throws IOException {
        final InputStream inputStream = Hasher.class.getResourceAsStream(name);
        final byte[] buffer = new byte[1024];
        int read = -1;
        final File temp = File.createTempFile(name, "");
        final FileOutputStream outputStream = new FileOutputStream(temp);
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.close();
        inputStream.close();
        System.load(temp.getAbsolutePath());
    }
}
