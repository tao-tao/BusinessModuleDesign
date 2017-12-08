package avicit.ui.platform.common.data;

import java.util.List;

/**
 * 脚本参数类
 * @author Administrator
 *
 */
public class ScriptParamParsedFile {
	
	private String scriptPath;
	private String scriptName;
	private String osName;
	private String userNme;
	private String callType;
	private String relCallType;
	private String suffix;
	private String stepId;
	private String stepName;
	private String stepClass;
	private String repFlag;
	private String listenerName;
	private String jobId;
	private String jobName;
	private String splitType;
	private String joinType;
	private List paramList;//参数列表.
	private String isThread;
	private String threadSize;
	
	public String getIsThread() {
		return isThread;
	}
	public void setIsThread(String isThread) {
		this.isThread = isThread;
	}
	public String getThreadSize() {
		return threadSize;
	}
	public void setThreadSize(String threadSize) {
		this.threadSize = threadSize;
	}
	public String getRelCallType() {
		return relCallType;
	}
	public void setRelCallType(String relCallType) {
		this.relCallType = relCallType;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getSplitType() {
		return splitType;
	}
	public void setSplitType(String splitType) {
		this.splitType = splitType;
	}
	public String getJoinType() {
		return joinType;
	}
	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}
	public List getParamList() {
		return paramList;
	}
	public void setParamList(List paramList) {
		this.paramList = paramList;
	}
	public String getScriptPath() {
		return scriptPath;
	}
	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	public String getOsName() {
		return osName;
	}
	public void setOsName(String osName) {
		this.osName = osName;
		if("windows".equals(osName)){
			this.setSuffix(".bat");
		}else{
			this.setSuffix(".sh");
		}
	}
	public String getUserNme() {
		return userNme;
	}
	public void setUserNme(String userNme) {
		this.userNme = userNme;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
		if("动态运算构件方式".equals(callType)){
			this.setRelCallType("1");
		}else if("BIZ流方式".equals(callType)){
			this.setRelCallType("2");
		}
	}
	public String getStepId() {
		return stepId;
	}
	public void setStepId(String stepId) {
		this.stepId = stepId;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getStepClass() {
		return stepClass;
	}
	public void setStepClass(String stepClass) {
		this.stepClass = stepClass;
	}
	public String getRepFlag() {
		return repFlag;
	}
	public void setRepFlag(String repFlag) {
		this.repFlag = repFlag;
	}
	public String getListenerName() {
		return listenerName;
	}
	public void setListenerName(String listenerName) {
		this.listenerName = listenerName;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
}
