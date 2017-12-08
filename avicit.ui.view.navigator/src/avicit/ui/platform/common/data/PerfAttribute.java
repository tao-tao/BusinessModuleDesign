package avicit.ui.platform.common.data;

import java.util.ArrayList;
import java.util.List;

public class PerfAttribute {
	public String beanid;
	public String mName = "";
	public String group = "";
	public String priority = "";
	public String interfaces = "";
	public boolean logAll = false;
	public org.w3c.dom.Node domNode;
	public List methods = new ArrayList();
}
