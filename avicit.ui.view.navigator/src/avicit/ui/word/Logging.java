package avicit.ui.word;

public class Logging {
	private String logging;
	private String logModifier;
	private String loggingTime;
	private String modDescription;

	public String getLogging() {
		return logging;
	}

	public void setLogging(String logging) {
		if(logging == null){
			this.logging = null;
		}

		this.logging = logging;
	}

	public String getLogModifier() {
		return logModifier;
	}

	public void setLogModifier(String logModifier) {
		if(logModifier == null){
			this.logModifier = "";
		}

		this.logModifier = logModifier;
	}

	public String getLoggingTime() {
		return loggingTime;
	}

	public void setLoggingTime(String loggingTime) {
		if(loggingTime == null){
			this.loggingTime = "";
		}

		this.loggingTime = loggingTime;
	}

	public String getModDescription() {
		return modDescription;
	}

	public void setModDescription(String modDescription) {
		if(modDescription == null){
			this.modDescription = "";
		}

		this.modDescription = modDescription;
	}
}
