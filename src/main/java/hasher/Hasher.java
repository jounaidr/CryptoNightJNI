package hasher;

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
        String binary = "";
        //Get the system OS
        final String system = System.getProperty("os.name").toLowerCase();

        try {
            //Load the binary corresponding with the system architecture
            if (system.contains("win")) {
                loadBinary("/win/cryptonight.dll");
            } else if (system.contains("nix")
                    || system.contains("nux")
                    || system.contains("aix")) {
                loadBinary("/unix/libcryptonight.so");
            } else {
                //If the operating system isn't supported, throw exception
                throw new hasher.UnsupportedOperatingSystemException(String.format("Operating system: '%s' is not supported", system));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadBinary(String binary) throws IOException {
        final InputStream inputStream = Hasher.class.getResourceAsStream(binary);
        final byte[] buffer = new byte[1024];
        int read = -1;
        final File temp = File.createTempFile(binary, "");
        final FileOutputStream outputStream = new FileOutputStream(temp);
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.close();
        inputStream.close();
        System.load(temp.getAbsolutePath());
    }
}
