package avicit.platform6.tools.codegeneration.wizard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test {

	/**
	 * 方法名称    :main
	 * 功能描述    :
	@see
				   :
	@see
	 * 逻辑描述    :
	 * @param   :无
	 * @return  :void
	 * @throws  :无
	 * @since   :Ver 1.00
	 */
	public static void main(String[] args) {
		
		
		Set set = new HashSet();
		List list1 = new ArrayList();
		ListDataField data = new ListDataField();
		System.out.println(data.hashCode());
		data.setClstype(1);
		ListDataField data1 = new ListDataField();
		System.out.println(data1.hashCode());
		data1.setClstype(0);
		set.add(data);
		list1.add(data);
		data = new ListDataField();
		data.setClstype(1);
		List list = Arrays.asList(set.toArray());
		System.out.println(list1.contains(data1));
		
		
	}

}
