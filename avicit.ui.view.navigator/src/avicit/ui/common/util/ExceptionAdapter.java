package avicit.ui.common.util;



public final class ExceptionAdapter extends RuntimeException
{

    public ExceptionAdapter(Throwable r_Cause)
    {
        super(r_Cause);
        errorCode = 0;
    }

    public ExceptionAdapter(String r_Message, Throwable r_Cause)
    {
        super(r_Message, r_Cause);
        errorCode = 0;
    }

    public ExceptionAdapter(int r_ErrorCode, Throwable r_Cause)
    {
        super(r_Cause);
        errorCode = 0;
        errorCode = r_ErrorCode;
    }

    public ExceptionAdapter(int r_ErrorCode, String r_Message)
    {
        super(r_Message);
        errorCode = 0;
        errorCode = r_ErrorCode;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    private static final long serialVersionUID = 1L;
    public static final int NONE = 0;
    private int errorCode;
}