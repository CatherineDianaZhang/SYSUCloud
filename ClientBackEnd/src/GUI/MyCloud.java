package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MyCloud extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	public MyCloud(Frame cloud) {
		cloud.setTitle("SYSUCloud");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0,0,0,0));
		contentPane.setLayout(new GridLayout(1,1));
		contentPane.setBackground(Color.white);
		//选项卡面板
		JTabbedPane pages = new JTabbedPane();
		pages.setBackground(Color.white);
		
		JPanel filePage = makeFilePage(cloud);
		pages.addTab("我的网盘",filePage);
		
		JScrollPane sharePage = makeSharePage(cloud);
		pages.addTab("我的分享", sharePage);
		
		JScrollPane transPage = makeTransPage(cloud);
		pages.addTab("我的传输", transPage);
		
		contentPane.add(pages);
		
		//添加整个页面
		JPanel cards = (JPanel)cloud.getContentPane().getComponent(0);
		cards.add(contentPane,"mainPage");
		CardLayout card = (CardLayout)(cards.getLayout());
		card.show(cards, "mainPage");
	}
	
	protected JPanel makeFilePage(JFrame cloud) {
		JPanel filePage = new JPanel();
		filePage.setBackground(Color.white);
		
		JPanel addressAndUpdate = new JPanel();
		JScrollPane files = new JScrollPane();
		JPanel quickShare = new JPanel();
		//addressAndUpdate
		addressAndUpdate.setPreferredSize(new Dimension(770,30));
		addressAndUpdate.setBackground(Color.white);
		GridBagLayout bag = new GridBagLayout();
		addressAndUpdate.setLayout(bag);
		GridBagConstraints constraints=new GridBagConstraints();
		constraints.fill=GridBagConstraints.BOTH;
		constraints.insets=new Insets(0,0,0,5);
		constraints.weightx = 1;
		
		JTextField showFileWay = new JTextField("我的网盘 / ");
		showFileWay.setPreferredSize(new Dimension(700,30));
		showFileWay.setBackground(Color.white);
		showFileWay.setForeground(Color.gray);
		showFileWay.setEditable(false);
		bag.setConstraints(showFileWay,constraints);
		addressAndUpdate.add(showFileWay);
		
		JButton updateButton = new JButton("上传");
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		bag.setConstraints(updateButton,constraints);
		addressAndUpdate.add(updateButton);

		//files
		files.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.lightGray));
		files.setPreferredSize(new Dimension(770,400));
		JPanel fileContent = new JPanel();
		fileContent.setPreferredSize(new Dimension(760,390));
		fileContent.setBackground(Color.white);
		files.setViewportView(fileContent);
		
			//文件列表	
		
		
		
			//右键弹出下载、分享、删除
		
		
		
		
				
		//quickShare
		quickShare.setPreferredSize(new Dimension(770,80));
		quickShare.setBackground(Color.white);
		JLabel filler=new JLabel("<html><p align=\"center\">右键点击或将文件拖拽到这里进行分享</p><br><p align=\"center\">支持扩展名：.rar .zip .doc .docx .pdf .jpg...</p></html>");
	    filler.setHorizontalAlignment(JLabel.CENTER);
	    quickShare.setLayout(new GridLayout(1,1));
	    quickShare.add(filler);
	    
		//添加三个页面
		filePage.add(addressAndUpdate,BorderLayout.NORTH);
		filePage.add(files);
		filePage.add(quickShare,BorderLayout.SOUTH);
		return filePage;
	}
	
	protected JScrollPane makeSharePage(JFrame cloud) {
		JScrollPane sharePage = new JScrollPane();
		JPanel shareContent = new JPanel();
		shareContent.setBackground(Color.white);
		sharePage.setViewportView(shareContent);
		
		
		
		
		
		return sharePage;
	}
	
	protected JScrollPane makeTransPage(JFrame cloud) {
		JScrollPane transPage = new JScrollPane();
		JPanel transContent = new JPanel();
		transContent.setBackground(Color.white);
		transPage.setViewportView(transContent);
		
		
		return transPage;
	}
}
