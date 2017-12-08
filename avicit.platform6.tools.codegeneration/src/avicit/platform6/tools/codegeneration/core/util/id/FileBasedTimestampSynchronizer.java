package avicit.platform6.tools.codegeneration.core.util.id;

import java.io.File;
import java.io.IOException;



final class FileBasedTimestampSynchronizer extends TimestampSynchronizer {

    static final long DEFAULT_UPDATE_INTERVAL = 10000L;
    static final String FILENAME1 = "uuid1.lck";
    static final String FILENAME2 = "uuid2.lck";
    long mInterval;
    final LockedFile mLocked1;
    final LockedFile mLocked2;
    boolean mFirstActive;
    
    public FileBasedTimestampSynchronizer()
    throws IOException {
        this(new File("uuid1.lck"), new File("uuid2.lck"));
    }
    
    public FileBasedTimestampSynchronizer(File f1, File f2)
    throws IOException {
        this(f1, f2, 10000L);
    }
    
    public FileBasedTimestampSynchronizer(File f1, File f2, long interval)
    throws IOException {
        boolean ok;
        mInterval = 10000L;
        mFirstActive = false;
        mInterval = interval;
        mLocked1 = new LockedFile(f1);
        ok = false;
        mLocked2 = new LockedFile(f2);
        ok = true;
        
        try{
            if(!ok) {
                mLocked1.deactivate();
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
        
//        break MISSING_BLOCK_LABEL_83;
//        Exception exception;
//        exception;
//        if(!ok) {
//            mLocked1.deactivate();
//        }
//        throw exception;
    }
    
    public void setUpdateInterval(long interval) {
        if(interval < 1L) {
            throw new IllegalArgumentException("Illegal value (" + interval + "); has to be a positive integer value");
        } else {
            mInterval = interval;
            return;
        }
    }
    
    protected long initialize()
    throws IOException {
        long ts1 = mLocked1.readStamp();
        long ts2 = mLocked2.readStamp();
        long result;
        if(ts1 > ts2) {
            mFirstActive = true;
            result = ts1;
        } else {
            mFirstActive = false;
            result = ts2;
        }
        if(result <= 0L) {
            System.out.println("Could not determine safe timer starting point: assuming current system time is acceptable");
        } else {
            long now = System.currentTimeMillis();
            long diff = now - result;
            if(now + mInterval < result) {
                System.out.println("Safe timestamp read is " + (result - now) + " milliseconds in future, and is greater than the inteval (" + mInterval + ")");
            }
        }
        return result;
    }
    
    public void deactivate()
    throws IOException {
        doDeactivate(mLocked1, mLocked2);
    }
    
    public long update(long now)
    throws IOException {
        long nextAllowed = now + mInterval;
        if(mFirstActive) {
            mLocked2.writeStamp(nextAllowed);
        } else {
            mLocked1.writeStamp(nextAllowed);
        }
        mFirstActive = !mFirstActive;
        return nextAllowed;
    }
    
    protected static void doDeactivate(LockedFile lf1, LockedFile lf2) {
        if(lf1 != null) {
            lf1.deactivate();
        }
        if(lf2 != null) {
            lf2.deactivate();
        }
    }

}
