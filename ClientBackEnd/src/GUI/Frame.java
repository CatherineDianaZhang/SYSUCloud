package GUI;

import java.awt.*;
import javax.swing.*;

public class Frame extends JFrame{
	private static final long serialVersionUID = 1L;

	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //设置窗体退出时操作
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
