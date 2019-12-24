package GUI;

import ClientEnd.CallBackFunArg;
import ClientEnd.CallBackFunc;
import ClientEnd.ClientEnd;
import com.sun.security.ntlm.Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SignUp extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField userName;
	private JTextField password;
	private JTextField email;
	private JTextField name;
	private JTextField alert;
	
	
	public SignUp(Frame cloud, ClientEnd clientEnd) {
		cloud.setTitle("SYSUCloud--SignUp");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(150,300,150,300));
		contentPane.setLayout(new GridLayout(6,1));
		contentPane.setBackground(Color.white);
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(Color.white);
		userName = new JTextField();
		userName.setPreferredSize(new Dimension(150,30));
		userName.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		userName.addFocusListener(new TextFieldHintListener(userName, "用户名"));
		panel1.add(userName);
		
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.white);
		password = new JTextField();
		password.setPreferredSize(new Dimension(150,30));
		password.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		password.addFocusListener(new TextFieldHintListener(password, "密码"));
		panel2.add(password);
		
		JPanel panel3 = new JPanel();
		panel3.setBackground(Color.white);
		email = new JTextField();
		email.setPreferredSize(new Dimension(150,30));
		email.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		email.addFocusListener(new TextFieldHintListener(email, "中大邮箱"));
		panel3.add(email);

		JPanel panel4 = new JPanel();
		panel4.setBackground(Color.white);
		name = new JTextField();
		name.setPreferredSize(new Dimension(150,30));
		name.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		name.addFocusListener(new TextFieldHintListener(name, "昵称"));
		panel4.add(name);

		JPanel panel5 = new JPanel();
		panel5.setBackground(Color.white);
		JButton signUpButton = new JButton("注册");
		signUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				alert.setText("");
				String a = userName.getText();
				String b = password.getText();
				String c = email.getText();
				String d = name.getText();
				try {
					clientEnd.signUp(a,b,c,d,new CallBackFunc() {
						@Override
						public void done(CallBackFunArg callBackFunArg) throws Exception{
							if(callBackFunArg.bool) showSignIn(cloud);
							else showError();
						}
					});
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel5.add(signUpButton);

		JPanel panel6 = new JPanel();
		panel6.setBackground(Color.white);
		alert = new JTextField();
		alert.setEditable(false);
		alert.setBackground(Color.white);
		alert.setPreferredSize(new Dimension(150,30));
		alert.setBorder(BorderFactory.createLineBorder(Color.white));
		//alert.setForeground(Color.red);
		panel6.add(alert);
		
		contentPane.add(panel1,BorderLayout.NORTH);
		contentPane.add(panel2);
		contentPane.add(panel3);
		contentPane.add(panel4);
		contentPane.add(panel5);
		contentPane.add(panel6,BorderLayout.SOUTH);
		
		JPanel cards = (JPanel)cloud.getContentPane().getComponent(0);
		cards.add(contentPane,"signUp");
		CardLayout card = (CardLayout)(cards.getLayout());
		card.show(cards, "signUp");
	}

	private void showError() {
		alert.setText("信息重复！");
	}

	private void showSignIn(Frame cloud) {
		JPanel cards = (JPanel)cloud.getContentPane().getComponent(0);
		CardLayout card = (CardLayout)(cards.getLayout());
		card.show(cards, "signIn");
		cloud.setTitle("SYSUCloud--SignIn");
	}
}
