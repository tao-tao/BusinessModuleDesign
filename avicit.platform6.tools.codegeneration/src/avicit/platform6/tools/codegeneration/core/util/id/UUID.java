package avicit.platform6.tools.codegeneration.core.util.id;

import java.io.Serializable;

public class UUID
    implements Serializable, Cloneable, Comparable {
    
    private static final String kHexChars = "0123456789abcdefABCDEF";
    public static final byte INDEX_CLOCK_HI = 6;
    public static final byte INDEX_CLOCK_MID = 4;
    public static final byte INDEX_CLOCK_LO = 0;
    public static final byte INDEX_TYPE = 6;
    public static final byte INDEX_CLOCK_SEQUENCE = 8;
    public static final byte INDEX_VARIATION = 8;
    public static final byte TYPE_NULL = 0;
    public static final byte TYPE_TIME_BASED = 1;
    public static final byte TYPE_DCE = 2;
    public static final byte TYPE_NAME_BASED = 3;
    public static final byte TYPE_RANDOM_BASED = 4;
    public static final String NAMESPACE_DNS = "6ba7b810-9dad-11d1-80b4-00c04fd430c8";
    public static final String NAMESPACE_URL = "6ba7b811-9dad-11d1-80b4-00c04fd430c8";
    public static final String NAMESPACE_OID = "6ba7b812-9dad-11d1-80b4-00c04fd430c8";
    public static final String NAMESPACE_X500 = "6ba7b814-9dad-11d1-80b4-00c04fd430c8";
    private static boolean sDescCaching = true;
    private static final UUID sNullUUID = new UUID();
    private final byte mId[];
    private transient String mDesc;
    private transient int mHashCode;
    private static final int kShifts[] = {
        3, 7, 17, 21, 29, 4, 9
    };
    private static final int sTimeCompare[] = {
        6, 7, 4, 5, 0, 1, 2, 3
    };
    
    public UUID() {
        mId = new byte[16];
        mDesc = null;
        mHashCode = 0;
    }
    
    public UUID(byte data[]) {
        mId = new byte[16];
        mDesc = null;
        mHashCode = 0;
        for(int i = 0; i < 16; i++) {
            mId[i] = data[i];
        }
        
    }
    
    public UUID(byte data[], int start) {
        mId = new byte[16];
        mDesc = null;
        mHashCode = 0;
        for(int i = 0; i < 16; i++) {
            mId[i] = data[start + i];
        }
        
    }
    
    UUID(int type, byte data[]) {
        mId = new byte[16];
        mDesc = null;
        mHashCode = 0;
        for(int i = 0; i < 16; i++) {
            mId[i] = data[i];
        }
        
        mId[6] &= 0xf;
        mId[6] |= (byte)(type << 4);
        mId[8] &= 0x3f;
        mId[8] |= 0x80;
    }
    
    public UUID(String id)
    throws NumberFormatException {
        mId = new byte[16];
        mDesc = null;
        mHashCode = 0;
        if(id == null) {
            throw new NullPointerException();
        }
        if(id.length() != 36) {
            throw new NumberFormatException("UUID has to be represented by the standard 36-char representation");
        }
        int i = 0;
        for(int j = 0; i < 36; j++) {
            switch(i) {
                case 8: // '\b'
                case 13: // '\r'
                case 18: // '\022'
                case 23: // '\027'
                    if(id.charAt(i) != '-') {
                        throw new NumberFormatException("UUID has to be represented by the standard 36-char representation");
                    }
                    i++;
                    break;
            }
            char c = id.charAt(i);
            if(c >= '0' && c <= '9') {
                mId[j] = (byte)(c - 48 << 4);
            } else
                if(c >= 'a' && c <= 'f') {
                mId[j] = (byte)((c - 97) + 10 << 4);
                } else
                    if(c >= 'A' && c <= 'F') {
                mId[j] = (byte)((c - 65) + 10 << 4);
                    } else {
                throw new NumberFormatException("Non-hex character '" + c + "'");
                    }
            c = id.charAt(++i);
            if(c >= '0' && c <= '9') {
                mId[j] |= (byte)(c - 48);
            } else
                if(c >= 'a' && c <= 'f') {
                mId[j] |= (byte)((c - 97) + 10);
                } else
                    if(c >= 'A' && c <= 'F') {
                mId[j] |= (byte)((c - 65) + 10);
                    } else {
                throw new NumberFormatException("Non-hex character '" + c + "'");
                    }
            i++;
        }
        
    }
    
    public Object clone() {
        try {
            return super.clone();
        } catch(CloneNotSupportedException e) {
            return null;
        }
    }
    
    public static void setDescCaching(boolean state) {
        sDescCaching = state;
    }
    
    public static UUID getNullUUID() {
        return sNullUUID;
    }
    
    public boolean isNullUUID() {
        if(this == sNullUUID) {
            return true;
        }
        byte data[] = mId;
        int i = mId.length;
        byte zero = 0;
        while(--i >= 0) {
            if(data[i] != zero) {
                return false;
            }
        }
        return true;
    }
    
    public int getType() {
        return (mId[6] & 0xff) >> 4;
    }
    
    public byte[] asByteArray() {
        byte result[] = new byte[16];
        toByteArray(result);
        return result;
    }
    
    public void toByteArray(byte dst[], int pos) {
        byte src[] = mId;
        for(int i = 0; i < 16; i++) {
            dst[pos + i] = src[i];
        }
        
    }
    
    public void toByteArray(byte dst[]) {
        toByteArray(dst, 0);
    }
    
    public byte[] toByteArray() {
        return asByteArray();
    }
    
    public int hashCode() {
        if(mHashCode == 0) {
            int result = mId[0] & 0xff;
            result |= result << 16;
            result |= result << 8;
            for(int i = 1; i < 15; i += 2) {
                int curr = (mId[i] & 0xff) << 8 | mId[i + 1] & 0xff;
                int shift = kShifts[i >> 1];
                if(shift > 16) {
                    result ^= curr << shift | curr >>> 32 - shift;
                } else {
                    result ^= curr << shift;
                }
            }
            
            int last = mId[15] & 0xff;
            result ^= last << 3;
            result ^= last << 13;
            result ^= last << 27;
            if(result == 0) {
                mHashCode = -1;
            } else {
                mHashCode = result;
            }
        }
        return mHashCode;
    }
    
    public String toStringByGUID() {
        if(mDesc == null) {
            StringBuffer b = new StringBuffer(36);
            for(int i = 0; i < 16; i++) {
                int hex;
                switch(i) {
                    case 4: // '\004'
                    case 6: // '\006'
                    case 8: // '\b'
                    case 10: // '\n'
                        b.append('-');
                        // fall through
                        
                    case 5: // '\005'
                    case 7: // '\007'
                    case 9: // '\t'
                    default:
                        hex = mId[i] & 0xff;
                        break;
                }
                b.append("0123456789abcdefABCDEF".charAt(hex >> 4));
                b.append("0123456789abcdefABCDEF".charAt(hex & 0xf));
            }
            
            if(!sDescCaching) {
                return b.toString();
            }
            mDesc = b.toString();
        }
        return mDesc;
    }
    
    public String toString() {
        StringBuffer b = new StringBuffer(36);
        for(int i = 0; i < 16; i++) {
            int hex = mId[i] & 0xff;
            b.append("0123456789abcdefABCDEF".charAt(hex >> 4));
            b.append("0123456789abcdefABCDEF".charAt(hex & 0xf));
        }
        
        return b.toString();
    }
    
    public int compareTo(Object o) {
        UUID other = (UUID)o;
        int thisType = getType();
        int thatType = other.getType();
        if(thisType > thatType) {
            return 1;
        }
        if(thisType < thatType) {
            return -1;
        }
        byte thisId[] = mId;
        byte thatId[] = other.mId;
        int i = 0;
        if(thisType == 1) {
            for(; i < 8; i++) {
                int index = sTimeCompare[i];
                int cmp = (thisId[index] & 0xff) - (thatId[index] & 0xff);
                if(cmp != 0) {
                    return cmp;
                }
            }
            
        }
        for(; i < 16; i++) {
            int cmp = (thisId[i] & 0xff) - (thatId[i] & 0xff);
            if(cmp != 0) {
                return cmp;
            }
        }
        
        return 0;
    }
    
    public boolean equals(Object o) {
        if(!(o instanceof UUID)) {
            return false;
        }
        byte otherId[] = ((UUID)o).mId;
        byte thisId[] = mId;
        for(int i = 0; i < 16; i++) {
            if(otherId[i] != thisId[i]) {
                return false;
            }
        }
        
        return true;
    }
    
    public static UUID valueOf(String id)
    throws NumberFormatException {
        return new UUID(id);
    }
    
    public static UUID valueOf(byte src[], int start) {
        return new UUID(src, start);
    }
    
    public static UUID valueOf(byte src[]) {
        return new UUID(src);
    }
    
    private void copyFrom(UUID src) {
        byte srcB[] = src.mId;
        byte dstB[] = mId;
        for(int i = 0; i < 16; i++) {
            dstB[i] = srcB[i];
        }
        
        mDesc = sDescCaching ? src.mDesc : null;
    }
    
}
