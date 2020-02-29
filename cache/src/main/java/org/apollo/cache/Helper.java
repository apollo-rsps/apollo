package org.apollo.cache;

import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.bzip2.CBZip2OutputStream;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.LZMAInputStream;
import org.tukaani.xz.XZOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.CompletionException;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Helper {

    private static CRC32 crc32 = new CRC32();

    public static final int COMPRESSION_NONE = 0;
    public static final int COMPRESSION_BZIP2 = 1;
    public static final int COMPRESSION_GZIP = 2;
    public static final int COMPRESSION_LZMA = 3;


    public static void decryptContainer(byte[] container, int[] keys) {
        decryptContainer(new CacheBuffer(container), keys);
    }

    public static void decryptContainer(CacheBuffer container, int[] keys) {
        container.xteaDecrypt(keys, 5, container.getBuffer().length - 2);
    }


    public static void encryptContainer(byte[] container, int[] keys) {
        encryptContainer(new CacheBuffer(container), keys);
    }

    public static void encryptContainer(CacheBuffer container, int[] keys) {
        container.xteaEncrypt(keys, 5, container.getBuffer().length - 2);
    }

    public static byte[] decodeFilesContainer(byte[] data) {
        return decodeFilesContainer(new CacheBuffer(data)).getBuffer();
    }

    public static CacheBuffer decodeFilesContainer(CacheBuffer container) {
        return decodeContainer(container, false);
    }

    public static byte[] decodeFITContainer(byte[] data) {
        return decodeFITContainer(new CacheBuffer(data)).getBuffer();
    }

    public static CacheBuffer decodeFITContainer(CacheBuffer container) {
        return decodeContainer(container, true);
    }

    private static CacheBuffer decodeContainer(CacheBuffer container, boolean isFITContainer) {
        container.setPosition(0);
        int compressionType = container.readUByte();
        if (compressionType > COMPRESSION_LZMA)
            throw new RuntimeException("Unknown compression type:" + compressionType);
        int length = container.readInt();
        CacheBuffer data;
        if (compressionType != COMPRESSION_NONE) {
            int decompressedLength = container.readInt();
            data = new CacheBuffer(decompressedLength);
            try {
                if (compressionType == COMPRESSION_GZIP) {
                    DataInputStream stream = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(container.getBuffer(),
							container.getPosition(), length)));
                    stream.readFully(data.getBuffer());
                    stream.close();
                } else if (compressionType == COMPRESSION_BZIP2) {
                    // we need to add header
                    byte[] reloc = new byte[length + 2];
                    reloc[0] = 'h';reloc[1] = '1';
                    System.arraycopy(container.getBuffer(), container.getPosition(), reloc, 2, length);
                    DataInputStream stream = new DataInputStream(new CBZip2InputStream(new ByteArrayInputStream(reloc)));
                    stream.readFully(data.getBuffer());
                    stream.close();
                } else {
                    // we need to add length
                    byte[] reloc = new byte[length + 8];
                    reloc[5] = (byte) (decompressedLength >>> 0);
                    reloc[6] = (byte) (decompressedLength >>> 8);
                    reloc[7] = (byte) (decompressedLength >>> 16);
                    reloc[8] = (byte) (decompressedLength >>> 24);
                    reloc[9] = reloc[10] = reloc[11] = reloc[12] = 0;
                    System.arraycopy(container.getBuffer(), container.getPosition() + 0, reloc, 0, 5);
                    System.arraycopy(container.getBuffer(), container.getPosition() + 5, reloc, 13, length - 5);
                    LZMAInputStream stream = new LZMAInputStream(new ByteArrayInputStream(reloc));
                    while (data.getPosition() < data.getBuffer().length) {
                        int amt = stream.read(data.getBuffer(), data.getPosition(), data.getBuffer().length - data.getPosition());
                        if (amt == -1) {
                            stream.close();
                            throw new IOException();
                        }
                        data.setPosition(data.getPosition() + amt);
                    }

                    stream.close();
                }
            } catch (IOException ioex) {
                throw new CompletionException(ioex);
            }
        } else {
            data = new CacheBuffer(length);
            container.readBytes(data.getBuffer(), data.getPosition(), length);
        }
        if (!isFITContainer) {
            @SuppressWarnings("unused")
            int version = container.readUShort();
        }
        return data;
    }

    /**
     * Does basic verification and decides if container is valid.
     */
    public static boolean validFilesContainer(CacheBuffer container) {
        if (container.getBuffer().length < 7)
            return false;
        container.setPosition(0);
        int compressionType = container.readUByte();
        if (compressionType > COMPRESSION_LZMA)
            return false;
        int length = container.readInt();
        if (compressionType != COMPRESSION_NONE) {
            if ((container.getBuffer().length - container.getPosition()) < 4)
                return false;
            container.readInt();
        }
        if (length < 0 || (container.getBuffer().length - container.getPosition() - length - 2) != 0)
            return false;
        return true;

    }

    public static int decodeVersion(CacheBuffer container) {
        container.setPosition(0);
        int compressionType = container.readUByte();
        if (compressionType > COMPRESSION_LZMA)
            throw new RuntimeException("Unknown compression type:" + compressionType);
        int length = container.readInt();
        if (compressionType != COMPRESSION_NONE)
            container.readInt(); // decompressed length
        container.setPosition(container.getPosition() + length);
        return container.readUShort();
    }


    public static byte[] encodeFilesContainer(byte[] file, int fileVersion) {
        return encodeFilesContainer(new CacheBuffer(file), fileVersion).getBuffer();
    }

    public static byte[] encodeFilesContainer(byte[] file, int fileVersion, int compressionType) {
        return encodeFilesContainer(new CacheBuffer(file), fileVersion, compressionType).getBuffer();
    }

    public static CacheBuffer encodeFilesContainer(CacheBuffer file, int fileVersion) {
        return encodeFilesContainer(file, fileVersion, decideCompression(file));
    }

    public static CacheBuffer encodeFilesContainer(CacheBuffer file, int fileVersion, int compressionType) {
        return encodeContainer(file, fileVersion, compressionType, false);
    }

    public static byte[] encodeFITContainer(byte[] file, int fileVersion) {
        return encodeFITContainer(new CacheBuffer(file), fileVersion).getBuffer();
    }

    public static byte[] encodeFITContainer(byte[] file, int fileVersion, int compressionType) {
        return encodeFITContainer(new CacheBuffer(file), fileVersion, compressionType).getBuffer();
    }

    public static CacheBuffer encodeFITContainer(CacheBuffer file, int fileVersion) {
        return encodeFITContainer(file, fileVersion, decideCompression(file));
    }

    public static CacheBuffer encodeFITContainer(CacheBuffer file, int fileVersion, int compressionType) {
        return encodeContainer(file, fileVersion, compressionType, true);
    }

    private static CacheBuffer encodeContainer(CacheBuffer file, int fileVersion, int compressionType, boolean isFITContainer) {
        if (compressionType > COMPRESSION_GZIP) // This is wrong
            throw new RuntimeException("Unsupported compression type:" + compressionType);

        int allocLength = 1 + 4 + (!isFITContainer ? 2 : 0);
        int fileLength = file.getBuffer().length;
        if (compressionType != COMPRESSION_NONE) {
            allocLength += 4;
            try {
                if (compressionType == COMPRESSION_GZIP) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    GZIPOutputStream out = new GZIPOutputStream(baos);
                    out.write(file.getBuffer());
                    out.finish();
                    out.close();
                    file = new CacheBuffer(baos.toByteArray());
                    baos.close();
                } else if (compressionType == COMPRESSION_BZIP2) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    CBZip2OutputStream out = new CBZip2OutputStream(baos,1);
                    out.write(file.getBuffer());
                    out.close();
                    byte[] data = baos.toByteArray(); // we need to remove h1
                    byte[] reloc = new byte[data.length - 2];
                    System.arraycopy(data, 2, reloc, 0, reloc.length);
                    file = new CacheBuffer(reloc);
                    baos.close();
                } else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    LZMA2Options options = new LZMA2Options();

                    options.setPreset(7); // play with this number: 6 is default but 7 works better for mid sized archives ( > 8mb)

                    XZOutputStream out = new XZOutputStream(baos, options);
                    out.write(file.getBuffer());
                    out.flush();
                    out.close();

                    byte[] data = baos.toByteArray(); // we need to remove h1
                    byte[] reloc = new byte[data.length - 8];
                    System.arraycopy(data, 8, reloc, 0, reloc.length);
                    file = new CacheBuffer(reloc);
                    baos.close();
                }
            } catch (IOException ioex) {
                throw new RuntimeException(ioex);
            }
        }
        allocLength += file.getBuffer().length;

        CacheBuffer container = new CacheBuffer(allocLength);
        container.writeByte(compressionType);
        container.writeInt(file.getBuffer().length);
        if (compressionType != COMPRESSION_NONE)
            container.writeInt(fileLength);
        container.writeBytes(file.getBuffer(), 0, file.getBuffer().length);
        if (!isFITContainer) {
            container.writeShort(fileVersion);
        }
        return container;
    }

    public static int crc32(byte[] data, int offset, int length) {
        return crc32(new CacheBuffer(data), offset, length);
    }

    public static int crc32(CacheBuffer data, int offset, int length) {
        crc32.update(data.getBuffer(), offset, length);
        int v = (int) crc32.getValue();
        crc32.reset();
        return v;
    }

    public static int strToI(String str) {
        if (str == null)
            return -1;

        // same as return str.hashcode();
        int i = 0;
        for (int a = 0; a < str.length(); a++)
            i = (31 * i) + str.charAt(a);
        return i;
    }

    private static int decideCompression(CacheBuffer data) {
        if (data.getBuffer().length < 100)
            return COMPRESSION_NONE;
        return COMPRESSION_GZIP; // bzip2 is unreliable
    }
}
