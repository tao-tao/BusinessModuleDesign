package avicit.platform6.tools.codegeneration.core.util.id;

import java.io.Serializable;

class EthernetAddress
    implements Serializable, Cloneable, Comparable {
    
    private static final String kHexChars = "0123456789abcdefABCDEF";
    private final byte mAddress[];
    
    public EthernetAddress(String addrStr)
    throws NumberFormatException {
        mAddress = new byte[6];
        byte addr[] = mAddress;
        int len = addrStr.length();
        int i = 0;
        for(int j = 0; j < 6; j++) {
            if(i >= len) {
                if(j == 5) {
                    addr[5] = 0;
                } else {
                    throw new NumberFormatException("Incomplete ethernet address (missing one or more digits");
                }
                break;
            }
            char c = addrStr.charAt(i);
            i++;
            int value;
            if(c == ':') {
                value = 0;
            } else {
                if(c >= '0' && c <= '9') {
                    value = c - 48;
                } else
                    if(c >= 'a' && c <= 'f') {
                    value = (c - 97) + 10;
                    } else
                        if(c >= 'A' && c <= 'F') {
                    value = (c - 65) + 10;
                        } else {
                    throw new NumberFormatException("Non-hex character '" + c + "'");
                        }
                if(i < len) {
                    c = addrStr.charAt(i);
                    i++;
                    if(c != ':') {
                        value <<= 4;
                        if(c >= '0' && c <= '9') {
                            value |= c - 48;
                        } else
                            if(c >= 'a' && c <= 'f') {
                            value |= (c - 97) + 10;
                            } else
                                if(c >= 'A' && c <= 'F') {
                            value |= (c - 65) + 10;
                                } else {
                            throw new NumberFormatException("Non-hex character '" + c + "'");
                                }
                    }
                }
            }
            addr[j] = (byte)value;
            if(c == ':') {
                continue;
            }
            if(i < len) {
                if(addrStr.charAt(i) != ':') {
                    throw new NumberFormatException("Expected ':', got ('" + addrStr.charAt(i) + "')");
                }
                i++;
                continue;
            }
            if(j < 5) {
                throw new NumberFormatException("Incomplete ethernet address (missing one or more digits");
            }
        }
        
    }
    
    public EthernetAddress(byte addr[])
    throws NumberFormatException {
        mAddress = new byte[6];
        if(addr.length != 6) {
            throw new NumberFormatException("Ethernet address has to consist of 6 bytes");
        }
        for(int i = 0; i < 6; i++) {
            mAddress[i] = addr[i];
        }
        
    }
    
    public EthernetAddress(long addr) {
        mAddress = new byte[6];
        for(int i = 0; i < 6; i++) {
            mAddress[5 - i] = (byte)(int)addr;
            addr >>>= 8;
        }
        
    }
    
    EthernetAddress() {
        mAddress = new byte[6];
        byte z = 0;
        for(int i = 0; i < 6; i++) {
            mAddress[i] = z;
        }
        
    }
    
    public Object clone() {
        try {
            return super.clone();
        } catch(CloneNotSupportedException e) {
            return null;
        }
    }
    
    public boolean equals(Object o) {
        if(!(o instanceof EthernetAddress)) {
            return false;
        }
        byte otherAddress[] = ((EthernetAddress)o).mAddress;
        byte thisAddress[] = mAddress;
        for(int i = 0; i < 6; i++) {
            if(otherAddress[i] != thisAddress[i]) {
                return false;
            }
        }
        
        return true;
    }
    
    public int compareTo(Object o) {
        byte thatA[] = ((EthernetAddress)o).mAddress;
        byte thisA[] = mAddress;
        for(int i = 0; i < 6; i++) {
            int cmp = (thisA[i] & 0xff) - (thatA[i] & 0xff);
            if(cmp != 0) {
                return cmp;
            }
        }
        
        return 0;
    }
    
    public String toString() {
        StringBuffer b = new StringBuffer(17);
        byte addr[] = mAddress;
        for(int i = 0; i < 6; i++) {
            if(i > 0) {
                b.append(":");
            }
            int hex = addr[i] & 0xff;
            b.append("0123456789abcdefABCDEF".charAt(hex >> 4));
            b.append("0123456789abcdefABCDEF".charAt(hex & 0xf));
        }
        
        return b.toString();
    }
    
    public byte[] asByteArray() {
        byte result[] = new byte[6];
        toByteArray(result);
        return result;
    }
    
    public byte[] toByteArray() {
        return asByteArray();
    }
    
    public void toByteArray(byte array[]) {
        toByteArray(array, 0);
    }
    
    public void toByteArray(byte array[], int pos) {
        for(int i = 0; i < 6; i++) {
            array[pos + i] = mAddress[i];
        }
        
    }
    
    public long toLong() {
        byte addr[] = mAddress;
        int hi = (addr[0] & 0xff) << 8 | addr[1] & 0xff;
        int lo = addr[2] & 0xff;
        for(int i = 3; i < 6; i++) {
            lo = lo << 8 | addr[i] & 0xff;
        }
        
        return (long)hi << 32 | (long)lo & 0xffffffffL;
    }
    
    public static EthernetAddress valueOf(byte addr[])
    throws NumberFormatException {
        return new EthernetAddress(addr);
    }
    
    public static EthernetAddress valueOf(int addr[])
    throws NumberFormatException {
        byte bAddr[] = new byte[addr.length];
        for(int i = 0; i < addr.length; i++) {
            bAddr[i] = (byte)addr[i];
        }
        
        return new EthernetAddress(bAddr);
    }
    
    public static EthernetAddress valueOf(String addrStr)
    throws NumberFormatException {
        return new EthernetAddress(addrStr);
    }
    
    public static EthernetAddress valueOf(long addr) {
        return new EthernetAddress(addr);
    }
}
