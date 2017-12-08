package avicit.ui.platform.common.exception;


public class MsgException extends RuntimeException {

	private static final long serialVersionUID = 7106302878421047370L;

	private String msg;
	public MsgException(String str){
		super(str);
		this.msg = str;
	}
	
	public String toString(){
		return msg;
	}
	
	public static String BadInput ="badinput";
	
	public static void badInput(){
		throw new MsgException(BadInput);
	}
	
}
