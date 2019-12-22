package GUI;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class TextFieldHintListener implements FocusListener{		
	private String hintText;	
	private JTextField textField;		
	public TextFieldHintListener(JTextField userName,String hintText) {		
		this.textField = userName;		
		this.hintText = hintText;		
		userName.setText(hintText);  //默认直接显示		
		userName.setForeground(Color.GRAY);	
	} 	
	@Override	
	public void focusGained(FocusEvent e) {		//获取焦点时，清空提示内容		
		String temp = textField.getText();		
		if(temp.equals(hintText)) {			
			textField.setText("");			
			textField.setForeground(Color.BLACK);		
		}	
	} 	
	@Override	public void focusLost(FocusEvent e) {		//失去焦点时，没有输入内容，显示提示内容		
		String temp = textField.getText();		
		if(temp.equals("")) {			
			textField.setForeground(Color.GRAY);			
			textField.setText(hintText);		
		}	
	} 
}
