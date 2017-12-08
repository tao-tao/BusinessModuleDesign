package avicit.platform6.tools.codegeneration.core.util.id;

import java.io.IOException;
import java.util.Random;

// Referenced classes of package org.navwork.frameworkimpl.id.uuidimpl:
//            TimestampSynchronizer

class UUIDTimer {
    
    private static final long kClockOffset = 0x1b21dd213814000L;
    private static final long kClockMultiplier = 10000L;
    private static final long kClockMultiplierL = 10000L;
    private static final long kMaxClockAdvance = 100L;
    private final Random mRnd;
    private final byte mClockSequence[] = new byte[3];
    private long mLastSystemTimestamp;
    private long mLastUsedTimestamp;
    private long mFirstUnsafeTimestamp;
    private int mClockCounter;
    private TimestampSynchronizer mSync;
    private static final int MAX_WAIT_COUNT = 50;
    
    UUIDTimer(Random rnd) {
        mLastSystemTimestamp = 0L;
        mLastUsedTimestamp = 0L;
        mFirstUnsafeTimestamp = 0x7fffffffffffffffL;
        mClockCounter = 0;
        mSync = null;
        mRnd = rnd;
        initCounters(rnd);
        mLastSystemTimestamp = 0L;
        mLastUsedTimestamp = 0L;
    }
    
    private void initCounters(Random rnd) {
        rnd.nextBytes(mClockSequence);
        mClockCounter = mClockSequence[2] & 0xff;
    }
    
    public void getTimestamp(byte uuidData[]) {
        uuidData[8] = mClockSequence[0];
        uuidData[9] = mClockSequence[1];
        long systime = System.currentTimeMillis();
        if(systime < mLastSystemTimestamp) {
            System.err.println("System time going backwards! (got value " + systime + ", last " + mLastSystemTimestamp);
            mLastSystemTimestamp = systime;
        }
        if(systime <= mLastUsedTimestamp) {
            if((long)mClockCounter < 10000L) {
                systime = mLastUsedTimestamp;
            } else {
                long actDiff = mLastUsedTimestamp - systime;
                long origTime = systime;
                systime = mLastUsedTimestamp + 1L;
                System.err.println("Timestamp over-run: need to reinitialize random sequence");
                initCounters(mRnd);
                if(actDiff >= 100L) {
                    slowDown(origTime, actDiff);
                }
            }
        } else {
            mClockCounter &= 0xff;
        }
        mLastUsedTimestamp = systime;
        if(mSync != null && systime >= mFirstUnsafeTimestamp) {
            try {
                mFirstUnsafeTimestamp = mSync.update(systime);
            } catch(IOException ioe) {
                throw new RuntimeException("Failed to synchronize timestamp: " + ioe);
            }
        }
        systime *= 10000L;
        systime += 0x1b21dd213814000L;
        systime += mClockCounter;
        mClockCounter++;
        int clockHi = (int)(systime >>> 32);
        int clockLo = (int)systime;
        uuidData[6] = (byte)(clockHi >>> 24);
        uuidData[7] = (byte)(clockHi >>> 16);
        uuidData[4] = (byte)(clockHi >>> 8);
        uuidData[5] = (byte)clockHi;
        uuidData[0] = (byte)(clockLo >>> 24);
        uuidData[1] = (byte)(clockLo >>> 16);
        uuidData[2] = (byte)(clockLo >>> 8);
        uuidData[3] = (byte)clockLo;
    }
    
    public void setSynchronizer(TimestampSynchronizer sync)
    throws IOException {
        TimestampSynchronizer old = mSync;
        if(old != null) {
            try {
                old.deactivate();
            } catch(IOException ioe) {
                System.err.println("Failed to deactivate the old synchronizer: " + ioe);
            }
        }
        mSync = sync;
        if(sync != null) {
            long lastSaved = sync.initialize();
            if(lastSaved > mLastUsedTimestamp) {
                mLastUsedTimestamp = lastSaved;
            }
        }
        mFirstUnsafeTimestamp = 0L;
    }
    
    private static final void slowDown(long startTime, long actDiff) {
        long ratio = actDiff / 100L;
        long delay;
        if(ratio < 2L) {
            delay = 1L;
        } else
            if(ratio < 10L) {
            delay = 2L;
            } else
                if(ratio < 600L) {
            delay = 3L;
                } else {
            delay = 5L;
                }
        System.err.println("Need to wait for " + delay + " milliseconds; virtual clock advanced too far in the future");
        long waitUntil = startTime + delay;
        int counter = 0;
        do
        {
            try {
                Thread.sleep(delay);
            } catch(InterruptedException ie) { }
            delay = 1L;
        } while(++counter <= 50 && System.currentTimeMillis() < waitUntil);
    }
}
