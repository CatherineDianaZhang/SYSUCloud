package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SignIn extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField userName;
	private JTextField password;
	private JTextField email;
	
	
	public SignIn(Frame cloud) {
		cloud.setTitle("SYSUCloud--SignIn");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(200,300,200,300));
		contentPane.setLayout(new GridLayout(4,1));
		contentPane.setBackground(Color.white);
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(Color.white);
		userName = new JTextField();
		userName.setPreferredSize(new Dimension(150,30));
		userName.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		userName.addFocusListener(new TextFieldHintListener(userName, "�û���"));
		panel1.add(userName);
		
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.white);
		password = new JTextField();
		password.setPreferredSize(new Dimension(150,30));
		password.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		password.addFocusListener(new TextFieldHintListener(password, "����"));
		panel2.add(password);
		
		JPanel panel3 = new JPanel();
		panel3.setBackground(Color.white);
		email = new JTextField();
		email.setPreferredSize(new Dimension(150,30));
		email.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		email.addFocusListener(new TextFieldHintListener(email, "�д�����"));
		panel3.add(email);
		
		JPanel panel4 = new JPanel();
		panel4.setBackground(Color.white);
		JButton signInButton = new JButton("ע��");
		signInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//SignUp signUp = new SignUp(cloud);
				JPanel cards = (JPanel)cloud.getContentPane().getComponent(0);
				CardLayout card = (CardLayout)(cards.getLayout());
				card.show(cards, "signUp");
			}
		});
		panel4.add(signInButton);
		
		contentPane.add(panel1,BorderLayout.NORTH);
		contentPane.add(panel2);
		contentPane.add(panel3);
		contentPane.add(panel4,BorderLayout.SOUTH);
		
		JPanel cards = (JPanel)cloud.getContentPane().getComponent(0);
		cards.add(contentPane,"signIn");
		CardLayout card = (CardLayout)(cards.getLayout());
		card.show(cards, "signIn");
	}
	
	//�����¼���ɹ���ת��MyCloud���棬���ɹ�����ʾ���û�����������󡱣����ע�ᣬ��ת��ע�����
	public void inSuccess() {
		
		
		//if(true) MyCloud myCloud = new MyCloud();  
		//else ...
	}
}
