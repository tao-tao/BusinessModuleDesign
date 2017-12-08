/**
 * 
 */
package net.java.amateras.uml.sequencediagram.figure;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.sequencediagram.figure.ext.ActivationFigureEx;
import net.java.amateras.uml.sequencediagram.figure.ext.InstanceFigureEx;

/**
 * @author shidat
 *
 */
public class SequenceFigureFactory {

	/**
	 * �A�N�e�B�x�C�V�����̐}���擾����.
	 * @return
	 */
	public static ActivationFigure getActivationFigure() { 
		if (UMLPlugin.getDefault().getPreferenceStore().getBoolean(UMLPlugin.PREF_NEWSTYLE)) {
			return new ActivationFigureEx();
		}
		return new ActivationFigure();
	}
	
	/**
	 * �C���X�^���X�̐}���擾����.
	 * @return
	 */
	public static InstanceFigure getInstanceFigure() {
		if (UMLPlugin.getDefault().getPreferenceStore().getBoolean(UMLPlugin.PREF_NEWSTYLE)) {
			return new InstanceFigureEx();
		}
		return new InstanceFigure();
	}
}
