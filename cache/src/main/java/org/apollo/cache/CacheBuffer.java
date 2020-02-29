package org.apollo.cache;

public class CacheBuffer {

    private static final int xteaDelta = 0x9E3779B9;
    private static final int xteaRounds = 32;

    private int position;
    public byte[] buffer;
    private boolean dontExpand;

    public CacheBuffer() {
        this(16);
    }

    public CacheBuffer(byte[] data) {
        this(data, 0);
    }

    public CacheBuffer(int capacity) {
		this.position = 0;
		this.buffer = new byte[capacity];
    }

    public CacheBuffer(byte[] data, int position) {
		this.position = position;
		this.buffer = new byte[data.length];
        System.arraycopy(data, 0, buffer, 0, buffer.length);
    }

    public void setDontExpand(boolean dontExpand) {
        this.dontExpand = dontExpand;
    }

    public final void xteaEncrypt(int[] keys, int offset, int length) {
        int originalPosition = this.position;
        this.position = offset;
        int numCycles = (length - offset) / 8;
        for (int cycle = 0; cycle < numCycles; cycle++) {
            int v0 = readInt();
            int v1 = readInt();
            int sum = 0;
            int numRounds = xteaRounds;
            while (numRounds-- > 0) {
                v0 += (sum + keys[sum & 0x3] ^ (v1 << 4 ^ v1 >>> 5) + v1);
                sum += xteaDelta;
                v1 += (sum + keys[(sum & 0x1d2f) >>> 11] ^ v0 + (v0 << 4 ^ v0 >>> 5));
            }
            this.position -= 8;
            writeInt(v0);
            writeInt(v1);
        }
        this.position = originalPosition;
    }

    public final void xteaDecrypt(int[] keys, int start, int length) {
        int l = position;
        position = start;
        int i1 = (length - start) / 8;
        for (int j1 = 0; j1 < i1; j1++) {
            position += 4;
            int k1 = ((0xff & buffer[-3 + position]) << 16) + ((((0xff & buffer[-4 + position]) << 24) + ((buffer[-2 + position] & 0xff) << 8)) + (buffer[-1 + position] & 0xff));
            position += 4;
            int l1 = ((0xff & buffer[-3 + position]) << 16) + ((((0xff & buffer[-4 + position]) << 24) + ((buffer[-2 + position] & 0xff) << 8)) + (buffer[-1 + position] & 0xff));
            int sum = 0xc6ef3720;
            for (int k2 = 32; k2-- > 0; ) {
                l1 -= keys[(sum & 0x1c84) >>> 11] + sum ^ (k1 >>> 5 ^ k1 << 4) + k1;
                sum -= 0x9e3779b9;
                k1 -= (l1 >>> 5 ^ l1 << 4) + l1 ^ keys[sum & 3] + sum;
            }

            position -= 8;
            buffer[position++] = (byte) (k1 >> 24);
            buffer[position++] = (byte) (k1 >> 16);
            buffer[position++] = (byte) (k1 >> 8);
            buffer[position++] = (byte) k1;
            buffer[position++] = (byte) (l1 >> 24);
            buffer[position++] = (byte) (l1 >> 16);
            buffer[position++] = (byte) (l1 >> 8);
            buffer[position++] = (byte) l1;
        }
        position = l;
    }

    public byte[] toArray(int offset, int length) {
        byte[] bf = new byte[length - offset];
        for (int i = 0; i < length; i++) {
            bf[i] = this.buffer[offset + i];
        }
        return bf;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getRemaining() {
        int length = buffer.length;
        return position < length ? length - position : 0;
    }

    public void skip(int length) {
        position += length;
    }

    public int readByte() {
        return getRemaining() > 0 ? buffer[position++] : 0;
    }

    public byte readSignedByte() {
        return (byte) readByte();
    }

    public int peekByte() {
        return getRemaining() > 0 ? buffer[position] : 0;
    }

    public void readBytes(byte buffer[]) {
        readBytes(buffer, 0, buffer.length);
    }

    public void readBytes(byte buffer[], int off, int len) {
        for (int k = off; k < len + off; k++) {
            buffer[k] = (byte) readByte();
        }
    }

    public String readString() {
        int startpos = position;
        while (position < buffer.length && buffer[position++] != 0) {
        }
        int strlen = position - startpos - 1;
        if (strlen <= 0)
            return "";
        return StringUtilities.decodeString(buffer, startpos, strlen);
    }

    public String readHttpHeaderString() {
        int length = 0;
        while ((position + length + 2) <= buffer.length && buffer[position + length + 0] != '\r' && buffer[position + length + 1] != '\n')
            length++;

        if ((position + length + 2) > buffer.length)
            return null;

        String str = new String(buffer, position, length);
        position += length + 2;
        return str;
    }

    public String readVersionedString() { // aka JAG string getService you called it
        return readVersionedString((byte) 0);
    }

    public String readVersionedString(byte versionNumber) {
        if (readByte() != versionNumber)
            throw new IllegalStateException("Bad string version number!");
        return readString();
    }

    public String readNullString() {
        if (peekByte() == 0) {
            skip(1);
            return null;
        }
        return readString();
    }

    public int readUMedInt() {
        return (readUByte() << 16) + (readUByte() << 8) + (readUByte());
    }

    public int readUByte() {
        return readByte() & 0xff;
    }

    public int readByte128() {
        return (byte) (readByte() - 128);
    }

    public int readByteC() {
        return (byte) -readByte();
    }

    public int read128Byte() {
        return (byte) (128 - readByte());
    }

    public int readUByteA() {
        return readUByte() - 128 & 0xff;
    }

    public int readUByteC() {
        return -readUByte() & 0xff;
    }

    public int readUByteS() {
        return 128 - readUByte() & 0xff;
    }

    public int readSignedLEShort() {
        int i = readUByte() + (readUByte() << 8);
        if (i > 32767) {
            i -= 0x10000;
        }
        return i;
    }

    public int readSignedShortA() {
        int i = (readUByte() << 8) + (readByte() - 128 & 0xff);
        if (i > 32767) {
            i -= 0x10000;
        }
        return i;
    }

    public int readSignedLEShortA() {
        int i = (readByte() - 128 & 0xff) + (readUByte() << 8);
        if (i > 32767) {
            i -= 0x10000;
        }
        return i;
    }

    public int readSignedLEShortS() {
        int i = (128 - readByte() & 0xff) + (readUByte() << 8);
        if (i > 32767) {
            i -= 0x10000;
        }
        return i;
    }

    public int readSignedShort() {
        int i = (readUByte() << 8) + readUByte();
        if (i > 32767) {
            i -= 0x10000;
        }
        return i;
    }

    public int readULEShort() {
        return readUByte() + (readUByte() << 8);
    }

    public int readUShort() {
        return (readUByte() << 8) + readUByte();
    }

    public int readSignedSmart() {
        int var2 = peekByte() & 0xff;
        if (var2 < 128) {
            return readUByte() - 64;
        }
        return readUShort() - 49152;
    }

    public int readSmart32() {
        if ((peekByte() ^ 0xffffffff) <= -1) {
            int value = readUShort();
            if (value == 32767) {
                return -1;
            }
            return value;
        }
        return readInt() & 0x7fffffff;
    }

    public int readDecoratedSmart() {
        int var2 = peekByte() & 0xff;
        if (var2 < 128) {
            return readUByte() - 1;
        }
        return readUShort() - 32769;
    }

    public int readUnsignedShortA() {
        return (readUByte() << 8) + (readByte() - 128 & 0xff);
    }

    public int readUnsignedLEShortA() {
        return (readByte() - 128 & 0xff) + (readUByte() << 8);
    }

    public int readInt() {
        return (readUByte() << 24) + (readUByte() << 16) + (readUByte() << 8) + readUByte();
    }

    public int readIntV1() {
        return (readUByte() << 8) + readUByte() + (readUByte() << 24) + (readUByte() << 16);
    }

    public int readIntV2() {
        return (readUByte() << 16) + (readUByte() << 24) + readUByte() + (readUByte() << 8);
    }

    public int readLEInt() {
        return readUByte() + (readUByte() << 8) + (readUByte() << 16) + (readUByte() << 24);
    }

    public long readLong() {
        long l = readInt() & 0xffffffffL;
        long l1 = readInt() & 0xffffffffL;
        return (l << 32) + l1;
    }

    public int readUnsignedSmart() {
        int peak = peekByte() & 0xff;
        if (peak < 128)
            return readUByte();
        return readUShort() - 32768;
    }

    public int readSmartLE() {
        int peak = peekByte();
        if (peak <= Byte.MAX_VALUE && peak >= Byte.MIN_VALUE)
            return readSignedByte();
        return readSignedLEShort() - 32768;
    }

    public int readCustomInt() {
        return (readUByte() << 24) + (readUByte() << 16) + (readUByte() << 8) + readUByte() + 128;
    }

    public long readDynamic(int byteCount) {
        if (--byteCount < 0 || byteCount > 7) {
            throw new IllegalArgumentException();
        }
        int bitposition = byteCount * 8;
        long l = 0L;
        for (/**/; bitposition >= 0; bitposition -= 8) {

            l |= (0xffL & readByte()) << bitposition;
        }
        return l;
    }

    public int readHugeSmart() {
        int value = 0;
        int read;
        for (read = readUnsignedSmart(); read == 32767; read = readUnsignedSmart()) {
            value += 32767;
        }
        value += read;
        return value;
    }

    public void writeHugeSmart(int value) {
        for (int i = 0; i < value / 0x7fff; i++) {
            writeShort((short) 0xffff);
        }
        writeUnsignedSmart(value & 0x7fff);
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public void expand(int position) {
        if (dontExpand) {
            return;
        }

        int length = this.buffer.length;
        if (position >= length) {
            byte[] newBuffer = new byte[position + 1000];
            System.arraycopy(buffer, 0, newBuffer, 0, length);
            this.buffer = newBuffer;
        }
    }

    public void writeByte(int value, int position) {
        expand(position);
        buffer[position] = (byte) value;
    }

    public void writeBytes(byte[] b, int offset, int length) {
        expand(this.position + length - 1);
        System.arraycopy(b, offset, buffer, this.position, length);

        this.position += length;
    }

    public void writeBytes(byte[] b) {
        expand(this.position + b.length - 1);
        System.arraycopy(b, 0, buffer, this.position, b.length);

        this.position += b.length;
    }

    public void writeBytes128Reverse(byte[] b) {
        for (int i = b.length - 1; i >= 0; i--)
            writeByteA(b[i]);
    }

    public void writeBytesReverse(byte[] b) {
        for (int i = b.length - 1; i >= 0; i--)
            writeByte(b[i]);
    }

    public void writeBytesA(byte[] buffer, int offset, int length) {
        for (int index = offset; index < length; index++) {
            writeByteA(buffer[index]);
        }
    }

    public void writeBytesA(byte[] buffer) {
        writeBytesA(buffer, 0, buffer.length);
    }

    public void writeVersionedString(String s) {
        writeVersionedString(s, (byte) 0);
    }

    public void writeVersionedString(String s, byte version) {
        writeByte(version);
        writeString(s);
    }

    public void writeString(String s) {
        int n = s.indexOf('\0');
        if (n >= 0)
            throw new IllegalArgumentException("NUL character at " + n + "!");
        expand(position + s.length());
        position += StringUtilities.encodeString(buffer, position, s, 0, s.length());
        writeByte(0);
    }

    public void writeByte(int value) {
        writeByte(value, position++);
    }

    public void writeByteA(int i) {
        writeByte(i + 128);
    }

    public void writeByteC(int i) {
        writeByte(-i);
    }

    public void writeByteS(int i) {
        writeByte(128 - i);
    }

    public void writeLEShortA(int i) {
        writeByte(i + 128);
        writeByte(i >> 8);
    }

    public void writeShortA(int i) {
        writeByte(i >> 8);
        writeByte(i + 128);
    }

    public void writeUnsignedSmart(int value) {
        if (value < 128)
            writeByte(value);
        else if (value < 32768)
            writeShort(value + 32768);
        else
            throw new IllegalArgumentException();
    }

    public void writeSmart32(int value) {
        if ((value & 0xFFFF) < 32768)
            writeShort(value);
        else
            writeInt(0x80000000 | value);
    }

    public void writeShort(int value) {
        writeByte(value >> 8);
        writeByte(value);
    }

    public void writeLEShort(int i) {
        writeByte(i);
        writeByte(i >> 8);
    }

    public void writeMedInt(int i) {
        writeByte(i >> 16);
        writeByte(i >> 8);
        writeByte(i);
    }

    public void write24BitIntegerV2(int i) {
        writeByte(i >> 16);
        writeByte(i);
        writeByte(i >> 8);
    }

    public void write24BitIntegerV3(int i) {
        writeByte(i);
        writeByte(i >> 8);
        writeByte(i >> 16);
    }


    public void writeInt(int i) {
        writeByte(i >> 24);
        writeByte(i >> 16);
        writeByte(i >> 8);
        writeByte(i);
    }

    public void writeIntV1(int i) {
        writeByte(i >> 8);
        writeByte(i);
        writeByte(i >> 24);
        writeByte(i >> 16);
    }

    public void writeIntV2(int i) {
        writeByte(i >> 16);
        writeByte(i >> 24);
        writeByte(i);
        writeByte(i >> 8);
    }

    public void writeIntLE(int i) {
        writeByte(i);
        writeByte(i >> 8);
        writeByte(i >> 16);
        writeByte(i >> 24);
    }

    public void writeLong(long l) {
        writeByte((int) (l >> 56));
        writeByte((int) (l >> 48));
        writeByte((int) (l >> 40));
        writeByte((int) (l >> 32));
        writeByte((int) (l >> 24));
        writeByte((int) (l >> 16));
        writeByte((int) (l >> 8));
        writeByte((int) l);
    }

    public void write5ByteInteger(long l) {
        writeByte((int) (l >> 32));
        writeByte((int) (l >> 24));
        writeByte((int) (l >> 16));
        writeByte((int) (l >> 8));
        writeByte((int) l);
    }

    public void writeDynamic(int bytes, long l) {
        bytes--;
        if (bytes < 0 || bytes > 7) {
            throw new IllegalArgumentException();
        }
        for (int shift = 8 * bytes; shift >= 0; shift -= 8) {
            writeByte((int) (l >> shift));
        }
    }

    public void writeFloat(float value) {
        writeInt(Float.floatToIntBits(value));
    }

    public void writeBoolean(boolean condition) {
        writeByte(condition ? 1 : 0);
    }

    public void writeSignedSmart(int value) {
        if (value < 64 && value >= -64) {
            writeByte(value + 64);
            return;
        }
        if (value < 16384 && value >= -16384) {
            writeShort(value + 49152);
            return;
        } else {
            System.out.println("Error psmart out of range: " + value);
            return;
        }
    }

    public final int readVarSeized() {
        int f = this.buffer[this.position++];
        int sum = 0;
        for (; f < 0; f = this.buffer[this.position++])
            sum = (sum | f & 0x7f) << 7;
        return sum | f;
    }

    public final void writeVarSeized(int val) {
        if ((val & ~0x7f) != 0) {
            if ((val & ~0x3fff) != 0) {
                if ((val & ~0x1fffff) != 0) {
                    if ((val & ~0xfffffff) != 0)
                        this.writeByte(val >>> 28 | 0x80);
                    this.writeByte((val | 0x100a2c1c) >>> 21);
                }
                this.writeByte(val >>> 14 | 0x80);
            }
            this.writeByte((val | 0x4021) >>> 7);
        }
        this.writeByte(val & 0x7f);
    }

    public void writeMidiInt(int value) {
        this.buffer[this.position - value - 4] = (byte) (value >> 24);
        this.buffer[this.position - value - 3] = (byte) (value >> 16);
        this.buffer[this.position - value - 2] = (byte) (value >> 8);
        this.buffer[this.position - value - 1] = (byte) value;
    }

    public byte getByte(int position) {
        return buffer[position];
    }

    public void setByte(int value, byte position) {
        buffer[position] = (byte) value;
    }

    public CacheBuffer trim() {
        return new CacheBuffer(toArray(0, position));
    }

    public boolean isEmpty() {
        return buffer.length == 0;
    }
}
