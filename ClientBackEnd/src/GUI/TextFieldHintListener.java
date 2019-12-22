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
		userName.setText(hintText);  //Ĭ��ֱ����ʾ		
		userName.setForeground(Color.GRAY);	
	} 	
	@Override	
	public void focusGained(FocusEvent e) {		//��ȡ����ʱ�������ʾ����		
		String temp = textField.getText();		
		if(temp.equals(hintText)) {			
			textField.setText("");			
			textField.setForeground(Color.BLACK);		
		}	
	} 	
	@Override	public void focusLost(FocusEvent e) {		//ʧȥ����ʱ��û���������ݣ���ʾ��ʾ����		
		String temp = textField.getText();		
		if(temp.equals("")) {			
			textField.setForeground(Color.GRAY);			
			textField.setText(hintText);		
		}	
	} 
}
