package GUI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import ClientEnd.CallBackFunc;
import ClientEnd.CallBackFunArg;
import ClientEnd.ClientEnd;
import com.sun.corba.se.pept.protocol.ClientDelegate;


public class SignIn extends JFrame{
	private static final long serialVersionUID = 1L;

	private JTextField userName;
	private JTextField password;
	private JTextField alert;

	public SignIn(Frame cloud, ClientEnd clientEnd) {
		cloud.setTitle("SYSUCloud--SignIn");
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(200,300,200,300));
		contentPane.setLayout(new GridLayout(4,1));
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
		
		JButton signInButton = new JButton("登录");
		bag.setConstraints(signInButton,constraints);
		signInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				alert.setText("");
				String a = userName.getText();
				String b = password.getText();
				try {
					clientEnd.signIn(a,b,new CallBackFunc() {
						@Override
						public void done(CallBackFunArg callBackFunArg) throws Exception{
							if(callBackFunArg.bool) showCloud(cloud,clientEnd);
							else alert.setText("用户名或密码错误！");
						}
					});
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		panel3.add(signInButton);
		
		JButton signUpButton = new JButton("注册");
		bag.setConstraints(signUpButton,constraints);
		signUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				alert.setText("");
				SignUp signUp = new SignUp(cloud,clientEnd);
			}
		});
		panel3.add(signUpButton);

		JPanel panel4 = new JPanel();
		panel4.setBackground(Color.white);
		alert = new JTextField();
		alert.setEditable(false);
		alert.setBackground(Color.white);
		alert.setPreferredSize(new Dimension(150,30));
		alert.setBorder(BorderFactory.createLineBorder(Color.white));
		//alert.setForeground(Color.red);
		panel4.add(alert);

		contentPane.add(panel1,BorderLayout.NORTH);
		contentPane.add(panel2,BorderLayout.CENTER);
		contentPane.add(panel3);
		contentPane.add(panel4,BorderLayout.SOUTH);
		
		JPanel cards = new JPanel(new CardLayout());
		cards.add(contentPane,"signIn");
		CardLayout card = (CardLayout)(cards.getLayout());
		card.show(cards, "signIn");
		cloud.getContentPane().add(cards);
	}

	private void showCloud(Frame cloud, ClientEnd clientEnd) {
		MyCloud mainPage = new MyCloud(cloud,clientEnd);
	}
}
