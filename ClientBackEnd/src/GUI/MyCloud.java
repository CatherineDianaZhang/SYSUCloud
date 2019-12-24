package GUI;

import ClientEnd.ClientEnd;
import ClientEnd.CallBackFunc;
import ClientEnd.CallBackFunArg;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MyCloud extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ImageIcon folder = new ImageIcon("folder.png");
	private ImageIcon txt = new ImageIcon("txt.png");;
	private JTextField showFileWay;
	private String path;
	private JPanel fileContent;
	private ClientEnd clientEnd;

	public MyCloud(Frame cloud, ClientEnd clientEnd) {
		this.clientEnd = clientEnd;
		cloud.setTitle("SYSUCloud");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0,0,0,0));
		contentPane.setLayout(new GridLayout(1,1));
		contentPane.setBackground(Color.white);
		//选项卡面板
		JTabbedPane pages = new JTabbedPane();
		pages.setBackground(Color.white);
		
		JPanel filePage = makeFilePage(cloud,clientEnd);
		pages.addTab("我的网盘",filePage);
		
		JScrollPane sharePage = makeSharePage(cloud,clientEnd);
		pages.addTab("我的分享", sharePage);
		
		JScrollPane transPage = makeTransPage(cloud,clientEnd);
		pages.addTab("我的传输", transPage);
		
		contentPane.add(pages);
		
		//添加整个页面
		JPanel cards = (JPanel)cloud.getContentPane().getComponent(0);
		cards.add(contentPane,"mainPage");
		CardLayout card = (CardLayout)(cards.getLayout());
		card.show(cards, "mainPage");
	}
	
	protected JPanel makeFilePage(JFrame cloud,ClientEnd clientEnd) {
		JPanel filePage = new JPanel();
		filePage.setBackground(Color.white);
		path = "/";
		
		JPanel addressAndUpdate = new JPanel();
		JScrollPane files = new JScrollPane();
		JPanel quickShare = new JPanel();
		//addressAndUpdate
		addressAndUpdate.setPreferredSize(new Dimension(760,30));
		addressAndUpdate.setBackground(Color.white);
		GridBagLayout bag = new GridBagLayout();
		addressAndUpdate.setLayout(bag);
		GridBagConstraints constraints=new GridBagConstraints();
		constraints.fill=GridBagConstraints.BOTH;
		constraints.insets=new Insets(0,0,0,5);
		constraints.weightx = 1;

		showFileWay = new JTextField("我的网盘");
		showFileWay.setPreferredSize(new Dimension(620,30));
		showFileWay.setBackground(Color.white);
		showFileWay.setBorder(BorderFactory.createLineBorder(Color.white));
		showFileWay.setForeground(Color.gray);
		showFileWay.setEditable(false);
		bag.setConstraints(showFileWay,constraints);
		addressAndUpdate.add(showFileWay);

		JButton backButton = new JButton("返回");
		backButton.setPreferredSize(new Dimension(60,30));
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index1 = path.lastIndexOf("/");
				String newPath = path.substring(0,index1);
				showFile(newPath);
				path = newPath;
				String newPath2 = showFileWay.getText().substring(0,showFileWay.getText().lastIndexOf("/"));
				showFileWay.setText(newPath2);
			}
		});
		bag.setConstraints(backButton,constraints);
		addressAndUpdate.add(backButton);
		
		JButton updateButton = new JButton("上传");
		updateButton.setPreferredSize(new Dimension(60,30));
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser f = new JFileChooser("F:\\");
				int val = f.showOpenDialog(null);
				File file = null;
				if(val == f.APPROVE_OPTION) {
					file = f.getSelectedFile();
					try {
						clientEnd.upload(file,path,new CallBackFunc() {
							@Override
							public void done(CallBackFunArg callBackFunArg) throws Exception {
								if(callBackFunArg.bool) showFile(path);
								System.out.println(callBackFunArg.bool);
								System.out.println(path);
							}
						});
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		bag.setConstraints(updateButton,constraints);
		addressAndUpdate.add(updateButton);

		//files
		files.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.lightGray));
		files.setPreferredSize(new Dimension(770,400));
		fileContent = new JPanel();
		fileContent.setPreferredSize(new Dimension(760,390));
		fileContent.setBackground(Color.white);
		files.setViewportView(fileContent);

			//文件列表
		showFile("/");
				
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

	private void showFile(String path){
		System.out.println("a");
		fileContent.removeAll();
		GridBagLayout fileBag = new GridBagLayout();
		fileContent.setLayout(fileBag);
		GridBagConstraints fileConstraints=new GridBagConstraints();
		fileConstraints.fill=GridBagConstraints.BOTH;
		fileConstraints.insets=new Insets(20,20,20,20);
		try {
			clientEnd.getFileList(path,new CallBackFunc() {
				@Override
				public void done(CallBackFunArg callBackFunArg) throws Exception{
					JSONArray fileList = callBackFunArg.jsonObject.getJSONArray("children");
					//System.out.println(fileList.size());
					for(int i=0;i<fileList.size();i++){
						JSONObject obj = (JSONObject) fileList.get(i);
						JButton b = new JButton(obj.get("name").toString());
						if(obj.get("type").toString() == "FOLDER") b.setIcon(folder);
						else b.setIcon(txt);
						//b.setBackground(Color.white);
						b.setBorder(BorderFactory.createLineBorder(Color.white));
						b.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								if(e.getClickCount() == 2 && obj.get("type") == "FOLDER") {
									try {
										getInNextLevel(obj.get("id"),clientEnd,fileContent);
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
								//else if(e.getClickCount() == 2 && obj.get("type") == "FILE") openFile(obj.get("id"));
							}
						});
						MouseListener popupListener = rightClick(obj.get("id"),fileContent,b);
						b.addMouseListener(popupListener);
						fileBag.setConstraints(b,fileConstraints);
						if(i%10 == 0) fileConstraints.gridwidth=GridBagConstraints.REMAINDER;
						else fileConstraints.gridwidth = 1;
					}
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void getInNextLevel(Object id,ClientEnd clientEnd,JPanel fileContent) throws Exception {
		int ID = (int)id;
		clientEnd.getFileDetails(ID, new CallBackFunc() {
			@Override
			public void done(CallBackFunArg callBackFunArg) throws Exception {
				JSONObject obj = callBackFunArg.jsonObject;
				//JSONArray  children = (JSONArray) obj.get("children");
				String temp = showFileWay.getText()+"/"+obj.get("name");
				showFileWay.setText(temp);
				path = obj.get("fullPath").toString();
				showFile(path);
			}
		});
	}

	private MouseListener rightClick(Object id,JPanel content,JButton button){
		int ID = (int)id;
		//右键弹出下载、分享、删除
		JPopupMenu jPopupMenuOne = new JPopupMenu();
		ButtonGroup buttonGroupOne = new ButtonGroup();
		JMenuItem down = new JMenuItem("下载");
		down.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		jPopupMenuOne.add(down);
		jPopupMenuOne.addSeparator();
		JMenuItem share = new JMenuItem("分享");
		share.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		jPopupMenuOne.add(share);
		jPopupMenuOne.addSeparator();
		JMenuItem delete = new JMenuItem("删除");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					clientEnd.delFile(ID, new CallBackFunc() {
						@Override
						public void done(CallBackFunArg callBackFunArg) throws Exception {
							content.remove(button);
							content.revalidate();
						}
					});
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		jPopupMenuOne.add(delete);
		MouseListener temp = new PopupListener(jPopupMenuOne);
		return temp;
	}

	protected JScrollPane makeSharePage(JFrame cloud,ClientEnd clientEnd) {
		JScrollPane sharePage = new JScrollPane();
		JPanel shareContent = new JPanel();
		shareContent.setBackground(Color.white);
		sharePage.setViewportView(shareContent);
		
		
		
		
		
		return sharePage;
	}
	
	protected JScrollPane makeTransPage(JFrame cloud,ClientEnd clientEnd) {
		JScrollPane transPage = new JScrollPane();
		JPanel transContent = new JPanel();
		transContent.setBackground(Color.white);
		transPage.setViewportView(transContent);
		
		
		return transPage;
	}

	class PopupListener extends MouseAdapter {
		JPopupMenu popupMenu;
		PopupListener(JPopupMenu popupMenu) {
			this.popupMenu=popupMenu;
		}
		public void mousePressed(MouseEvent e) {
			showPopupMenu(e);
		}
		public void mouseReleased(MouseEvent e) {
			showPopupMenu(e);
		}
		private void showPopupMenu(MouseEvent e) {
			if(e.isPopupTrigger())
			{
				//如果当前事件与鼠标事件相关，则弹出菜单
				popupMenu.show(e.getComponent(),e.getX(),e.getY());
			}
		}
	}
}

