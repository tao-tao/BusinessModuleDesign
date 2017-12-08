package avicit.ui.platform.common.exception;

public class TansunException extends RuntimeException {

	private static final long serialVersionUID = 7106302878421047370L;

	private String msg;
	public TansunException(String str){
		super(str);
		this.msg = str;
	}
	
	public TansunException(Throwable e) {
		super(e);
	}

	public TansunException() {
		super();
	}

	public String toString(){
		return msg;
	}
	
	public static String BadInput ="The project is not valid.";
	
	
	public static void throwe() throws Exception  {
		throw new TansunException();
	}
	
	public static void throwe(Throwable e)  {
		throw new TansunException(e);
	}
}
