package GUI;

import java.awt.*;
import javax.swing.*;

public class Frame extends JFrame{
	private static final long serialVersionUID = 1L;

	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //���ô����˳�ʱ����
	    setBounds(200, 50, 800, 600);
	    setBackground(Color.white);
	}
	
	public static void main(String[] args)
    {
        Frame cloud = new Frame();
        SignUp signUp = new SignUp(cloud);
	   // signUp.upSuccess();
        cloud.setVisible(true);
    }
}
