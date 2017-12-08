package avicit.platform6.tools.codegeneration.core.util.id;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

class LockedFile {
    
    static final int DEFAULT_LENGTH = 22;
    static final long READ_ERROR = 0L;
    final File mFile;
    RandomAccessFile mRAFile;
    FileChannel mChannel;
    FileLock mLock;
    ByteBuffer mWriteBuffer;
    boolean mWeirdSize;
    long mLastTimestamp;
    static final String HEX_DIGITS = "0123456789abcdef";
    
    LockedFile(File f) throws IOException {
        RandomAccessFile raf;
        FileChannel channel;
        FileLock lock;
        boolean ok;
        mWriteBuffer = null;
        mLastTimestamp = 0L;
        mFile = f;
        raf = null;
        channel = null;
        lock = null;
        ok = false;
        raf = new RandomAccessFile(f, "rwd");
        channel = raf.getChannel();
        if(channel == null) {
            throw new IOException("Failed to access channel for '" + f + "'");
        }
        lock = channel.tryLock();
        if(lock == null) {
            throw new IOException("Failed to lock '" + f + "' (another JVM running UUIDGenerator?)");
        }
        ok = true;
        
        try{
            if(!ok) {
                doDeactivate(f, raf, lock);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
        
        
        
//        break MISSING_BLOCK_LABEL_159;
//        Exception exception;
//        exception;
//        if(!ok) {
//            doDeactivate(f, raf, lock);
//        }
//        throw exception;
        
        
        mRAFile = raf;
        mChannel = channel;
        mLock = lock;
        return;
    }
    
    public void deactivate() {
        RandomAccessFile raf = mRAFile;
        mRAFile = null;
        FileLock lock = mLock;
        mLock = null;
        doDeactivate(mFile, raf, lock);
    }
    
    public long readStamp() {
        int size;
        try {
            size = (int)mChannel.size();
        } catch(IOException ioe) {
            System.err.println("Failed to read file size: " + ioe);
            return 0L;
        }
        mWeirdSize = size != 22;
        if(size == 0) {
            System.err.println("Missing or empty file, can not read timestamp value");
            return 0L;
        }
        if(size > 100) {
            size = 100;
        }
        byte data[] = new byte[size];
        try {
            mRAFile.readFully(data);
        } catch(IOException ie) {
            System.err.println("Failed to read " + size + " bytes: " + ie);
            return 0L;
        }
        char cdata[] = new char[size];
        for(int i = 0; i < size; i++) {
            cdata[i] = (char)(data[i] & 0xff);
        }
        
        String dataStr = new String(cdata);
        dataStr = dataStr.trim();
        long result = -1L;
        String err = null;
        if(!dataStr.startsWith("[0") || dataStr.length() < 3 || Character.toLowerCase(dataStr.charAt(2)) != 'x') {
            err = "does not start with '[0x' prefix";
        } else {
            int ix = dataStr.indexOf(']', 3);
            if(ix <= 0) {
                err = "does not end with ']' marker";
            } else {
                String hex = dataStr.substring(3, ix);
                if(hex.length() > 16) {
                    err = "length of the (hex) timestamp too long; expected 16, had " + hex.length() + " ('" + hex + "')";
                } else {
                    try {
                        result = Long.parseLong(hex, 16);
                    } catch(NumberFormatException nex) {
                        err = "does not contain a valid hex timestamp; got '" + hex + "' (parse error: " + nex + ")";
                    }
                }
            }
        }
        if(result < 0L) {
            System.err.println("Malformed timestamp file contents: " + err);
            return 0L;
        } else {
            mLastTimestamp = result;
            return result;
        }
    }
    
    public void writeStamp(long stamp)
    throws IOException {
        if(stamp <= mLastTimestamp) {
            if(stamp == mLastTimestamp) {
                System.err.println("Trying to re-write existing timestamp (" + stamp + ")");
                return;
            } else {
                throw new IOException("" + getFileDesc() + " trying to overwrite existing value (" + mLastTimestamp + ") with an earlier timestamp (" + stamp + ")");
            }
        }
        if(mWriteBuffer == null) {
            mWriteBuffer = ByteBuffer.allocate(22);
            mWriteBuffer.put(0, (byte)91);
            mWriteBuffer.put(1, (byte)48);
            mWriteBuffer.put(2, (byte)120);
            mWriteBuffer.put(19, (byte)93);
            mWriteBuffer.put(20, (byte)13);
            mWriteBuffer.put(21, (byte)10);
        }
        for(int i = 18; i >= 3; i--) {
            int val = (int)stamp & 0xf;
            mWriteBuffer.put(i, (byte)"0123456789abcdef".charAt(val));
            stamp >>= 4;
        }
        
        mWriteBuffer.position(0);
        mChannel.write(mWriteBuffer, 0L);
        if(mWeirdSize) {
            mRAFile.setLength(22L);
            mWeirdSize = false;
        }
        mChannel.force(false);
    }
    
    protected String getFileDesc() {
        return mFile.toString();
    }
    
    protected static void doDeactivate(File f, RandomAccessFile raf, FileLock lock) {
        if(lock != null) {
            try {
                lock.release();
            } catch(Throwable t) {
                System.err.println("Failed to release lock (for file '" + f + "'): " + t);
            }
        }
        if(raf != null) {
            try {
                raf.close();
            } catch(Throwable t) {
                System.err.println("Failed to close file '" + f + "':" + t);
            }
        }
    }

}
