package avicit.platform6.tools.codegeneration.wizard;


public interface IErrorHandler {
	public void dialogChanged();
	public void onError (String message, Throwable t);
}
