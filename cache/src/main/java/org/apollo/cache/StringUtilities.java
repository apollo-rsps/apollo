package org.apollo.cache;

public class StringUtilities {

    private static char[] UNICODE_TABLE = {'\u20ac', '\0', '\u201a', '\u0192', '\u201e', '\u2026', '\u2020', '\u2021', '\u02c6', '\u2030',
			'\u0160', '\u2039', '\u0152', '\0', '\u017d', '\0', '\0',
            '\u2018', '\u2019', '\u201c', '\u201d', '\u2022', '\u2013', '\u2014', '\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', '\0', '\u017e',
			'\u0178'};

    public static final String decodeString(byte[] buffer, int offset, int length) {
        char[] strBuffer = new char[length];
        int write = 0;
        for (int dc = 0; dc < length; dc++) {
            int data = buffer[dc + offset] & 0xff;
            if (data == 0)
                continue;
            if (data >= 128 && data < 160) {
                char uni = UNICODE_TABLE[data - 128];
                if (uni == 0)
                    uni = '?';
                strBuffer[write++] = (char) uni;
                continue;
            }
            strBuffer[write++] = (char) data;
        }
        return new String(strBuffer, 0, write);
    }

    public static char fromCP1252(byte value) {
        int i = 0xff & value;
        if (i == 0) {
            throw new IllegalArgumentException("Non cp1252 character 0x" + Integer.toString(i, 16) + " provided");
        }
        if (i >= 128 && i < 160) {
            int i_1_ = UNICODE_TABLE[i - 128];
            if (i_1_ == 0) {
                i_1_ = 63;
            }
            i = i_1_;
        }
        return (char) i;
    }

    public static final int encodeString(byte[] buffer, int boffset, String str, int soffset, int slength) {
        int charsToEncode = slength - soffset;
        for (int cc = 0; cc < charsToEncode; cc++) {
            char c = str.charAt(cc + soffset);
            if ((c > 0 && c < 128) || (c >= 160 && c <= 255)) {
                buffer[boffset + cc] = (byte) c;
                continue;
            }

            switch (c) {
                case '\u20ac':
                    buffer[boffset + cc] = -128;
                    break;
                case '\u201a':
                    buffer[boffset + cc] = -126;
                    break;
                case '\u0192':
                    buffer[boffset + cc] = -125;
                    break;
                case '\u201e':
                    buffer[boffset + cc] = -124;
                    break;
                case '\u2026':
                    buffer[boffset + cc] = -123;
                    break;
                case '\u2020':
                    buffer[boffset + cc] = -122;
                    break;
                case '\u2021':
                    buffer[boffset + cc] = -121;
                    break;
                case '\u02c6':
                    buffer[boffset + cc] = -120;
                    break;
                case '\u2030':
                    buffer[boffset + cc] = -119;
                    break;
                case '\u0160':
                    buffer[boffset + cc] = -118;
                    break;
                case '\u2039':
                    buffer[boffset + cc] = -117;
                    break;
                case '\u0152':
                    buffer[boffset + cc] = -116;
                    break;
                case '\u017d':
                    buffer[boffset + cc] = -114;
                    break;
                case '\u2018':
                    buffer[boffset + cc] = -111;
                    break;
                case '\u2019':
                    buffer[boffset + cc] = -110;
                    break;
                case '\u201c':
                    buffer[boffset + cc] = -109;
                    break;
                case '\u201d':
                    buffer[boffset + cc] = -108;
                    break;
                case '\u2022':
                    buffer[boffset + cc] = -107;
                    break;
                case '\u2013':
                    buffer[boffset + cc] = -106;
                    break;
                case '\u2014':
                    buffer[boffset + cc] = -105;
                    break;
                case '\u02dc':
                    buffer[boffset + cc] = -104;
                    break;
                case '\u2122':
                    buffer[boffset + cc] = -103;
                    break;
                case '\u0161':
                    buffer[boffset + cc] = -102;
                    break;
                case '\u203a':
                    buffer[boffset + cc] = -101;
                    break;
                case '\u0153':
                    buffer[boffset + cc] = -100;
                    break;
                case '\u017e':
                    buffer[boffset + cc] = -98;
                    break;
                case '\u0178':
                    buffer[boffset + cc] = -97;
                    break;
                default:
                    buffer[boffset + cc] = (byte) '?';
                    break;
            }
        }
        return charsToEncode;
    }
}