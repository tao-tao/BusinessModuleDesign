package avicit.platform6.tools.codegeneration.core.util.id;

import java.io.IOException;

abstract class TimestampSynchronizer {
    
    protected TimestampSynchronizer() {
    }
    
    protected abstract long initialize()
    throws IOException;
    
    protected abstract void deactivate()
    throws IOException;
    
    protected abstract long update(long l)
    throws IOException;
}
