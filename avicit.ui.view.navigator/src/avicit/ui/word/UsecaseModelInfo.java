package avicit.ui.word;

/**
 * @author Tao Tao
 *
 */
public class UsecaseModelInfo {
	private String usecaseNo;
	private String usecaseName;
	private String businessRule;
	private String usecaseDescription;
	private String authDescription;
	private String participant;
	private String status;
	private String precondition;
	private String postcondition;
	private WorkFlow basicworkingflow;
	private String exceptionOprFlow;
	private String nonFunction;
	private Logging logging;

	public WorkFlow getBasicworkingflow() {
		return basicworkingflow;
	}

	public void setBasicworkingflow(WorkFlow basicworkingflow) {
		this.basicworkingflow = basicworkingflow;
	}

	public String getUsecaseNo() {
		return usecaseNo;
	}

	public void setUsecaseNo(String usecaseNo) {
		if(usecaseNo == null){
			this.usecaseNo = "";
		}

		this.usecaseNo = usecaseNo;
	}

	public String getUsecaseName() {
		return usecaseName;
	}

	public void setUsecaseName(String usecaseName) {
		if(usecaseName == null){
			this.usecaseName = "";
		}

		this.usecaseName = usecaseName;
	}

	public String getUsecaseDescription() {
		return usecaseDescription;
	}

	public void setUsecaseDescription(String usecaseDescription) {
		if(usecaseDescription == null){
			this.usecaseDescription = "";
		}

		this.usecaseDescription = usecaseDescription;
	}

	public String getAuthDescription() {
		return authDescription;
	}

	public void setAuthDescription(String authDescription) {
		if(authDescription == null){
			this.authDescription = "";
		}

		this.authDescription = authDescription;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		if(participant == null){
			this.participant = "";
		}

		this.participant = participant;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if(status == null){
			this.status = "";
		}

		this.status = status;
	}

	public String getPrecondition() {
		return precondition;
	}

	public void setPrecondition(String precondition) {
		if(precondition == null){
			this.precondition = "";
		}

		this.precondition = precondition;
	}

	public String getPostcondition() {
		return postcondition;
	}

	public void setPostcondition(String postcondition) {
		if(postcondition == null){
			this.postcondition = "";
		}

		this.postcondition = postcondition;
	}

	public String getExceptionOprFlow() {
		return exceptionOprFlow;
	}

	public void setExceptionOprFlow(String exceptionOprFlow) {
		if(exceptionOprFlow == null){
			this.exceptionOprFlow = "";
		}

		this.exceptionOprFlow = exceptionOprFlow;
	}

	public String getNonFunction() {
		return nonFunction;
	}

	public void setNonFunction(String nonFunction) {
		if(nonFunction == null){
			this.nonFunction = "";
		}

		this.nonFunction = nonFunction;
	}

	public Logging getLogging() {
		return logging;
	}

	public void setLogging(Logging logging) {
		if(logging == null){
			this.logging = null;
		}

		this.logging = logging;
	}

	public String getBusinessRule() {
		return businessRule;
	}

	public void setBusinessRule(String businessRule) {
		if(businessRule == null){
			this.businessRule = "";
		}

		this.businessRule = businessRule;
	}
}
