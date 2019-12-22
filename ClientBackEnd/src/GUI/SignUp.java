package GUI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import ClientEnd.CallBackFunArg;


public class SignUp extends JFrame implements CallBackFunc{
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField userName;
	private JTextField password;
	
	public SignUp(Frame cloud) {
		cloud.setTitle("SYSUCloud--SignUp");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(200,300,200,300));
		contentPane.setLayout(new GridLayout(3,1));
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
		panel3.setPreferredSize(new Dimension(150,30));
		GridBagLayout bag = new GridBagLayout();
		panel3.setLayout(bag);
		GridBagConstraints constraints=new GridBagConstraints();
		constraints.fill=GridBagConstraints.BOTH;
		constraints.insets=new Insets(20,20,20,20);
		
		JButton signUpButton = new JButton("登录");
		bag.setConstraints(signUpButton,constraints);
		signUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String a = userName.getText();
				String b = password.getName();
				boolean ifSuccess = signUp(a,b,new CallBackFunc() {
					@Override
					public void done(CallBackFunArg callBackFunArg) throws Exception{
						System.out.println(callBackFunArg.bool);
					}
				});
				if(ifSuccess) {
					MyCloud mainPage = new MyCloud(cloud);
				}
				else {
					
				}
			}
		});
		panel3.add(signUpButton);
		
		JButton signInButton = new JButton("注册");
		bag.setConstraints(signInButton,constraints);
		signInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				SignIn signIn = new SignIn(cloud);
			}
		});
		panel3.add(signInButton);
		
		contentPane.add(panel1,BorderLayout.NORTH);
		contentPane.add(panel2,BorderLayout.CENTER);
		contentPane.add(panel3,BorderLayout.SOUTH);
		
		JPanel cards = new JPanel(new CardLayout());
		cards.add(contentPane,"signUp");
		CardLayout card = (CardLayout)(cards.getLayout());
		card.show(cards, "signUp");
		cloud.getContentPane().add(cards);
	}
	
	//点击登录，成功跳转到MyCloud界面，不成功则显示“用户名或密码错误”；点击注册，跳转到注册界面
}
