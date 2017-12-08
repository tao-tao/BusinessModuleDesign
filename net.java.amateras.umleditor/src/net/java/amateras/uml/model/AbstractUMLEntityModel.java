package net.java.amateras.uml.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.java.amateras.uml.classdiagram.model.AggregationModel;
import net.java.amateras.uml.classdiagram.model.AssociationModel;
import net.java.amateras.uml.classdiagram.model.ClassModel;
import net.java.amateras.uml.classdiagram.model.CompositeModel;
import net.java.amateras.uml.classdiagram.model.DependencyModel;
import net.java.amateras.uml.classdiagram.model.GeneralizationModel;
import net.java.amateras.uml.classdiagram.model.InterfaceModel;
import net.java.amateras.uml.classdiagram.model.RealizationModel;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.RGB;

public abstract class AbstractUMLEntityModel extends AbstractUMLModel {

	private Rectangle constraint;
	// ���̃��f������L�тĂ���R�l�N�V�����̃��X�g
	private List<AbstractUMLConnectionModel> sourceConnections = new ArrayList<AbstractUMLConnectionModel>();
	// ���̃��f���Ɍ���Ē����Ă���R�l�N�V�����̃��X�g
	private List<AbstractUMLConnectionModel> targetConnections = new ArrayList<AbstractUMLConnectionModel>();

	private List<AbstractUMLModel> children = new ArrayList<AbstractUMLModel>();
	// flags of show/hide property
	private Map<String, Boolean> filterProperty = new HashMap<String, Boolean>();

	public static final String P_CONSTRAINT = "_constraint";
	public static final String P_SOURCE_CONNECTION = "_source_connection";
	public static final String P_TARGET_CONNECTION = "_target_connection";
	public static final String P_CHILDREN = "_children";
	public static final String P_FILTER = "_filter";
	public static final String P_FORCE_UPDATE = "_force_update";

	public Map<String, Boolean> getFilterProperty() {
		return filterProperty;
	}

	public void setFilterProperty(Map<String, Boolean> filterProperty) {
		this.filterProperty = filterProperty;
		firePropertyChange(P_FILTER, null, filterProperty);
	}

	public Rectangle getConstraint() {
		return constraint;
	}

	public void addChild(AbstractUMLModel model) {
		children.add(model);
		model.setParent(this);
		firePropertyChange(P_CHILDREN, null, model);
	}

	public void removeChild(AbstractUMLModel model) {
		children.remove(model);
		model.setParent(this);
		firePropertyChange(P_CHILDREN, null, model);
	}

	public void forceUpdate() {
		firePropertyChange(P_FORCE_UPDATE, null, null);
	}

	public List<AbstractUMLModel> getChildren() {
		return this.children;
	}

	public void setConstraint(Rectangle constraint) {
		if (constraint.x < 0) {
			constraint.x = 0;
		}
		if (constraint.y < 0) {
			constraint.y = 0;
		}
		this.constraint = constraint;
		firePropertyChange(P_CONSTRAINT, null, constraint);
	}

	/** ���̃��f������o��R�l�N�V���� ���f���̒ǉ� */
	public void addSourceConnection(AbstractUMLConnectionModel connx) {
		sourceConnections.add(connx);
		firePropertyChange(P_SOURCE_CONNECTION, null, connx);
	}

	/** ���̃��f���ɐڑ������R�l�N�V���� ���f���̒ǉ� */
	public void addTargetConnection(AbstractUMLConnectionModel connx) {
		targetConnections.add(connx);
		firePropertyChange(P_TARGET_CONNECTION, null, connx);
	}

	/** ���̃��f����ڑ����Ƃ���R�l�N�V�����̃��X�g��Ԃ� */
	public List<AbstractUMLConnectionModel> getModelSourceConnections() {
		return sourceConnections;
	}

	/** ���̃��f����ڑ���Ƃ���R�l�N�V�����̃��X�g��Ԃ� */
	public List<AbstractUMLConnectionModel> getModelTargetConnections() {
		return targetConnections;
	}

	/** ���̃��f�����R�l�N�V�����̃\�[�X����؂藣�� */
	public void removeSourceConnection(AbstractUMLConnectionModel connx) {
		sourceConnections.remove(connx);
		firePropertyChange(P_SOURCE_CONNECTION, connx, null);
	}

	/** ���̃��f�����R�l�N�V�����̃^�[�Q�b�g����؂藣�� */
	public void removeTargetConnection(AbstractUMLConnectionModel connx) {
		targetConnections.remove(connx);
		firePropertyChange(P_TARGET_CONNECTION, connx, null);
	}

	public void setBackgroundColor(RGB backgroundColor) {
		for (Iterator<AbstractUMLModel> iter = children.iterator(); iter
				.hasNext();) {
			AbstractUMLModel element = (AbstractUMLModel) iter.next();
			element.setBackgroundColor(backgroundColor);
		}
		super.setBackgroundColor(backgroundColor);
	}

	public void setForegroundColor(RGB foregroundColor) {
		for (Iterator<AbstractUMLModel> iter = children.iterator(); iter
				.hasNext();) {
			AbstractUMLModel element = (AbstractUMLModel) iter.next();
			element.setForegroundColor(foregroundColor);
		}
		super.setForegroundColor(foregroundColor);
	}

	public void setShowIcon(boolean showIcon) {
		for (Iterator<AbstractUMLModel> iter = children.iterator(); iter
				.hasNext();) {
			AbstractUMLModel element = (AbstractUMLModel) iter.next();
			element.setShowIcon(showIcon);
		}
		super.setShowIcon(showIcon);
	}

	public boolean canTarget(AbstractUMLConnectionModel conn) {
		// TODO Auto-generated method stub
		PreArchorModelCheck pc=new PreArchorModelCheck();
		RealizationCheck rc = new RealizationCheck();
		DependenCheck dc = new DependenCheck();
		GeneralizationModelCheck gc = new GeneralizationModelCheck();
		AggregationModelCheck agc = new AggregationModelCheck();
		AssociationModelCheck asc = new AssociationModelCheck();
		CompositeModelCheck cc = new CompositeModelCheck();
		ArchorModelCheck acc=new ArchorModelCheck();
		pc.setNowCheck(rc);
		rc.setNowCheck(dc);
		dc.setNowCheck(gc);

		gc.setNowCheck(agc);
		agc.setNowCheck(asc);
		asc.setNowCheck(cc);
		cc.setNowCheck(acc);
		return pc.check(conn);
		// return true;
	}

	public abstract class Check {
		public abstract boolean check(AbstractUMLConnectionModel conn);

		public Check nowCheck;

		public Check getNowCheck() {
			return nowCheck;
		}

		public void setNowCheck(Check nowCheck) {
			this.nowCheck = nowCheck;
		}
	}

	class RealizationCheck extends Check {

		@Override
		public boolean check(AbstractUMLConnectionModel conn) {
			// TODO Auto-generated method stub
			if (conn instanceof RealizationModel) {
				System.out.println("ʵ��");
				if (conn.getSource() instanceof ClassModel) {
					if (conn.getTarget() instanceof InterfaceModel) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				if (this.getNowCheck() != null) {
					return this.getNowCheck().check(conn);
				}

			}
			return true;
		}

	}

	class DependenCheck extends Check {

		@Override
		public boolean check(AbstractUMLConnectionModel conn) {
			// TODO Auto-generated method stub
			if (conn instanceof DependencyModel) {
				if (conn.getSource() instanceof NoteModel) {
					return false;
				}if (conn.getTarget() instanceof NoteModel) {
					return false;
				}
				return true;
			} else {
				if (this.getNowCheck() != null) {
					return this.getNowCheck().check(conn);
				} else {
					return true;
				}

			}

		}

	}

	class GeneralizationModelCheck extends Check {

		@Override
		public boolean check(AbstractUMLConnectionModel conn) {
			// TODO Auto-generated method stub
			if (conn instanceof GeneralizationModel) {
				if (conn.getSource() instanceof NoteModel) {
					return false;
				}if (conn.getTarget() instanceof NoteModel) {
					return false;
				}
				//System.out.println("�̳�"+conn.getSource()+"  "+conn.getTarget());
				if (conn.getSource() instanceof InterfaceModel) {
					if (conn.getTarget() instanceof InterfaceModel) {
						return true;
					} else {
						return false;
					}
				} else {
					if (conn.getTarget() instanceof InterfaceModel) {
						return false;
					} else {
						return true;
					}
				}
			} else {
				if (this.getNowCheck() != null) {
					return this.getNowCheck().check(conn);
				} else {
					return true;
				}

			}

		}

	}

	class AggregationModelCheck extends Check {

		@Override
		public boolean check(AbstractUMLConnectionModel conn) {
			// TODO Auto-generated method stub
			if (conn instanceof AggregationModel) {
				if (conn.getSource() instanceof NoteModel) {
					return false;
				}if (conn.getTarget() instanceof NoteModel) {
					return false;
				}
				return true;
			} else {
				if (this.getNowCheck() != null) {
					return this.getNowCheck().check(conn);
				} else {
					return true;
				}

			}

		}

	}

	class AssociationModelCheck extends Check {

		@Override
		public boolean check(AbstractUMLConnectionModel conn) {
			// TODO Auto-generated method stub
			if (conn instanceof AssociationModel) {
				if (conn.getSource() instanceof NoteModel) {
					return false;
				}if (conn.getTarget() instanceof NoteModel) {
					return false;
				}
				return true;
			} else {
				if (this.getNowCheck() != null) {
					return this.getNowCheck().check(conn);
				} else {
					return true;
				}
			}

		}

	}

	class CompositeModelCheck extends Check {

		@Override
		public boolean check(AbstractUMLConnectionModel conn) {
			// TODO Auto-generated method stub
			if (conn instanceof CompositeModel) {
				if (conn.getSource() instanceof NoteModel) {
					return false;
				}if (conn.getTarget() instanceof NoteModel) {
					return false;
				}
				return true;
			} else {
				if (this.getNowCheck() != null) {
					return this.getNowCheck().check(conn);
				} else {
					return true;
				}

			}

		}

	}
	class ArchorModelCheck extends Check {

		@Override
		public boolean check(AbstractUMLConnectionModel conn) {
			// TODO Auto-generated method stub
			
			if (conn instanceof AnchorModel) {
				if (conn.getSource() instanceof NoteModel&&!(conn.getTarget() instanceof NoteModel)) {
					return true;
				}
				return false;
			} else {
				if (this.getNowCheck() != null) {
					return this.getNowCheck().check(conn);
				} else {
					return true;
				}

			}

		}

	}
	class PreArchorModelCheck extends Check {

		@Override
		public boolean check(AbstractUMLConnectionModel conn) {
			// TODO Auto-generated method stub
			if (conn.getTarget() instanceof NoteModel||conn.getSource() instanceof NoteModel) {
				if(conn instanceof AnchorModel){
					return true;
				}
				return false;
			} else {
				if (this.getNowCheck() != null) {
					return this.getNowCheck().check(conn);
				} else {
					return true;
				}

			}

		}

	}

}
