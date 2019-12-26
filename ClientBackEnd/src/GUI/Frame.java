package GUI;

import ClientEnd.ClientEnd;
import okhttp3.OkHttpClient;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
        Frame cloud = new Frame();
		ClientEnd clientEnd = new ClientEnd();
        SignIn signIn = new SignIn(cloud,clientEnd);
        cloud.pack();
        cloud.setVisible(true);
    }
}
