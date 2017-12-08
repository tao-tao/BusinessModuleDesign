package avicit.ui.platform.common.ui;

import java.util.List;

/**
 * ����˳��任�ӿ�.
 * @author pc063
 *
 */
public interface ShiftOrder {

	/**
	 * ����.
	 * @param o		����˳��任�Ķ���		
	 * @param all	������������ж���
	 * @return
	 */
	public boolean shiftDown(Object o, List all);
	
	/**
	 * ����
	 * @param o		����˳��任�Ķ���	
	 * @param all	������������ж���
	 * @return
	 */
	public boolean shiftUp(Object o, List all);
}
