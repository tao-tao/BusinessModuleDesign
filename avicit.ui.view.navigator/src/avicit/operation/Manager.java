package avicit.operation;


import java.util.Vector;

public class Manager {
	public Vector<MakeJava> v = new Vector<MakeJava>();
	public Vector<EcOperation> refresh = new Vector<EcOperation>();
	public static Manager manager;

	public static Manager getInstance() {
		if (manager == null) {
			manager = new Manager();
		}
		return manager;
	}
	public void add(MakeJava lis){
		v.add(lis);
	}
	public void fire(MakeJavaEvent name){
		for(int i=0;i<v.size();i++){
			v.get(i).finish(name);
		}
	}
	public void addr(EcOperation lis){
		refresh.add(lis);
	}
	public void firer(){
		for(int i=0;i<refresh.size();i++){
			refresh.get(i).refresh();
		}
	}
	public void remove(MakeJava lis){
		v.remove(lis);
	}
}
