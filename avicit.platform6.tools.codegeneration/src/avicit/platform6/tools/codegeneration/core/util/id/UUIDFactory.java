package avicit.platform6.tools.codegeneration.core.util.id;

import java.io.IOException;
import java.security.*;
import java.util.Random;



public final class UUIDFactory {
    
    private static final UUIDFactory sSingleton = new UUIDFactory();
    private Random mRnd;
    private final Object mDummyAddressLock = new Object();
    private EthernetAddress mDummyAddress;
    private final Object mTimerLock = new Object();
    private UUIDTimer mTimer;
    private MessageDigest mHasher;
    
    private UUIDFactory() {
        mRnd = null;
        mDummyAddress = null;
        mTimer = null;
        mHasher = null;
    }
    
    public static UUIDFactory getInstance() {
        return sSingleton;
    }
    
    public void synchronizeExternally(TimestampSynchronizer sync)
    throws IOException {
        synchronized(mTimerLock) {
            if(mTimer == null) {
                mTimer = new UUIDTimer(getRandomNumberGenerator());
            }
            mTimer.setSynchronizer(sync);
        }
    }
    
    public EthernetAddress getDummyAddress() {
        synchronized(mDummyAddressLock) {
            if(mDummyAddress == null) {
                Random rnd = getRandomNumberGenerator();
                byte dummy[] = new byte[6];
                rnd.nextBytes(dummy);
                dummy[0] |= 1;
                try {
                    mDummyAddress = new EthernetAddress(dummy);
                } catch(NumberFormatException nex) { }
            }
        }
        return mDummyAddress;
    }
    
    public Random getRandomNumberGenerator() {
        if(mRnd == null) {
            mRnd = new SecureRandom();
        }
        return mRnd;
    }
    
    public void setRandomNumberGenerator(Random r) {
        mRnd = r;
    }
    
    public MessageDigest getHashAlgorithm() {
        if(mHasher == null) {
            try {
                mHasher = MessageDigest.getInstance("MD5");
            } catch(NoSuchAlgorithmException nex) {
                throw new Error("Couldn't instantiate an MD5 MessageDigest instance: " + nex.toString());
            }
        }
        return mHasher;
    }
    
    public UUID generateRandomBasedUUID() {
        return generateRandomBasedUUID(getRandomNumberGenerator());
    }
    
    public UUID generateRandomBasedUUID(Random randomGenerator) {
        byte rnd[] = new byte[16];
        randomGenerator.nextBytes(rnd);
        return new UUID(4, rnd);
    }
    
    public UUID generateTimeBasedUUID() {
        return generateTimeBasedUUID(getDummyAddress());
    }
    
    public UUID generateTimeBasedUUID(EthernetAddress addr) {
        byte contents[] = new byte[16];
        addr.toByteArray(contents, 10);
        synchronized(mTimerLock) {
            if(mTimer == null) {
                mTimer = new UUIDTimer(getRandomNumberGenerator());
            }
            mTimer.getTimestamp(contents);
        }
        return new UUID(1, contents);
    }
    
    public UUID generateNameBasedUUID(UUID nameSpaceUUID, String name, MessageDigest digest) {
        digest.reset();
        if(nameSpaceUUID != null) {
            digest.update(nameSpaceUUID.asByteArray());
        }
        digest.update(name.getBytes());
        return new UUID(3, digest.digest());
    }
    
    public UUID generateNameBasedUUID(UUID nameSpaceUUID, String name) {
//        MessageDigest hasher = getHashAlgorithm();
//        MessageDigest messagedigest = hasher;
//        JVM INSTR monitorenter ;
//        return generateNameBasedUUID(nameSpaceUUID, name, getHashAlgorithm());
//        Exception exception;
//        exception;
//        throw exception;
        
        MessageDigest hasher = getHashAlgorithm();
        MessageDigest messagedigest = hasher;
        return generateNameBasedUUID(nameSpaceUUID, name, getHashAlgorithm());
    }
    
    public UUID generateTagURIBasedUUID(TagURI name) {
        return generateNameBasedUUID(null, name.toString());
    }
    
    public UUID generateTagURIBasedUUID(TagURI name, MessageDigest hasher) {
        return generateNameBasedUUID(null, name.toString(), hasher);
    }
    
}
