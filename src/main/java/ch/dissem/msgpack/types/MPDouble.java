package ch.dissem.msgpack.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

import static ch.dissem.msgpack.types.Utils.bytes;

/**
 * Representation of a msgpack encoded float64 number.
 */
public class MPDouble implements MPType<Double> {
    private double value;

    public MPDouble(double value) {
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    public void pack(OutputStream out) throws IOException {
        out.write(0xCB);
        out.write(ByteBuffer.allocate(8).putDouble(value).array());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MPDouble mpDouble = (MPDouble) o;
        return Double.compare(mpDouble.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public String toJson() {
        return String.valueOf(value);
    }

    public static class Unpacker implements MPType.Unpacker<MPDouble> {
        public boolean is(int firstByte) {
            return firstByte == 0xCB;
        }

        public MPDouble unpack(int firstByte, InputStream in) throws IOException {
            if (firstByte == 0xCB) {
                return new MPDouble(bytes(in, 8).getDouble());
            } else {
                throw new IllegalArgumentException(String.format("Unexpected first byte 0x%02x", firstByte));
            }
        }
    }
}