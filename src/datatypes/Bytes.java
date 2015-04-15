package datatypes;

import java.util.Arrays;

/**
 * Created by cotix on 4/15/15.
 */
public class Bytes {
    private byte[] data;

    public Bytes(byte[] d) {
        data = d;
    }

    public byte[] getBytes() {
        return data;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Bytes)) {
            return false;
        }
        return Arrays.equals(data, ((Bytes)o).getBytes());
    }

    public int hashCode() {
        int res = 5381;
        for (int i = 0; i != data.length; ++i) {
            res = ((res << 5)  + res) + data[i];
        }
        return res;
    }
}
