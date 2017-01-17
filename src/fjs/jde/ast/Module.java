package fjs.jde.ast;

public class Module {
	
	private String pkg;
	private String string;
	public Module(String pkg, String string) {
		this.pkg=pkg;
		this.string=string;
	}
	public String getPkg() {
		return pkg;
	}
	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

}
