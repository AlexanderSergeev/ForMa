package gui;

import javax.swing.JButton;

public class ButtonClass {

	private JButton button;
	private boolean flag;
	private String path;
	
	public ButtonClass(JButton button, String path){
		this.button = button;
		flag = false;
		this.path = path;
	}

	public boolean getFlag(){
		return flag;
	}
	
	public JButton getButton(){
		return button;
	}
	
	public String getPath(){
		return path;
	}
	
	public void setFlag(boolean flag){
		this.flag = flag;
	}
	
}
