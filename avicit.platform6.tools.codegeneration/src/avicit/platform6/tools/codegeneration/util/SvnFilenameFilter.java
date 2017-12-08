package avicit.platform6.tools.codegeneration.util;

import java.io.File;
import java.io.FilenameFilter;

public class SvnFilenameFilter implements FilenameFilter {

	public boolean accept(File dir, String name) {
		if (name.equals(".svn")) {
			return false;
		}
		return true;
	}

}