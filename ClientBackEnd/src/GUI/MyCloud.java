package GUI;

import ClientEnd.ClientEnd;
import ClientEnd.CallBackFunc;
import ClientEnd.CallBackFunArg;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import javafx.beans.binding.BooleanExpression;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MyCloud extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ImageIcon folder;
	private ImageIcon txt;
	private JTextField showFileWay;
	private String path;
	private JPanel fileContent;
	private JTable shareContent;
	private ClientEnd clientEnd;
	private JFrame cloud;

	public MyCloud(Frame cloud, ClientEnd clientEnd) {
		this.clientEnd = clientEnd;
		this.cloud = cloud;
		cloud.setTitle("SYSUCloud");

		this.folder = new ImageIcon(MyCloud.class.getResource("folder.png"));
		this.txt = new ImageIcon(MyCloud.class.getResource("folder.png"));

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
		
		contentPane.add(pages);
		
		//添加整个页面
		JPanel cards = (JPanel)cloud.getContentPane().getComponent(0);
		cards.add(contentPane,"mainPage");
		CardLayout card = (CardLayout)(cards.getLayout());
		card.show(cards, "mainPage");
		showShare();

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
				System.out.println("Current: " + path);
				if (path.equals("/") || path.equals("")) {
					return;
				}
				int index1 = path.lastIndexOf("/");
				String newPath = path.substring(0, index1);
				System.out.println(newPath);
				int index2 = newPath.lastIndexOf("/");
				String newPath2 = newPath.substring(0, index2);
				System.out.println(newPath2);
				showFile(newPath2);
				path = newPath2;
				String newPath3 = showFileWay.getText().substring(0,showFileWay.getText().lastIndexOf("/"));
				showFileWay.setText(newPath3);
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
					JFrame progressWin = new JFrame("上传进度");
					JProgressBar progressBar = new JProgressBar();
					progressBar.setValue(0);
					progressBar.setStringPainted(true);
					progressWin.setSize(360, 80);
					progressWin.add(progressBar);
					progressWin.setLocationRelativeTo(null);
					progressWin.setVisible(true);
					try {
						clientEnd.upload(file, path, new CallBackFunc() {
							@Override
							public void done(CallBackFunArg callBackFunArg) throws Exception {
								if (callBackFunArg.bool) showFile(path);
								progressWin.setTitle("上传完成");
								JOptionPane.showMessageDialog(progressWin, "上传完成");
								progressWin.setVisible(false);
								progressWin.dispose();
								System.out.println(callBackFunArg.bool);
							}
						}, new CallBackFunc() {
							@Override
							public void done(CallBackFunArg callBackFunArg) throws Exception {
								progressBar.setValue((Integer) callBackFunArg.jsonObject.get("length"));
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
		files.setBorder(BorderFactory.createMatteBorder(0,0,0,0,Color.white));
		files.setPreferredSize(new Dimension(770,500));
		fileContent = new JPanel();
		fileContent.setBackground(Color.white);
		files.setViewportView(fileContent);

		//右键点击新建文件夹
		JPopupMenu jPopupMenuOne=new JPopupMenu();
		JMenuItem create=new JMenuItem("新建文件夹");
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame dialog = new JFrame();
				String name = JOptionPane.showInputDialog(dialog, "请输入文件夹名称", "输入文件夹名称", JOptionPane.PLAIN_MESSAGE);
				String temp = path+name+"/";
				try {
					clientEnd.createFolder(temp, new CallBackFunc() {
						@Override
						public void done(CallBackFunArg callBackFunArg) throws Exception {
							if(callBackFunArg.bool) showFile(path);
						}
					});
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		jPopupMenuOne.add(create);
		MouseListener popupListener=new PopupListener(jPopupMenuOne);
		fileContent.addMouseListener(popupListener);
			//文件列表
		showFile("/");
	    
		//添加页面
		filePage.add(addressAndUpdate,BorderLayout.NORTH);
		filePage.add(files, BorderLayout.SOUTH);
		return filePage;
	}

	private void showFile(String path){
		System.out.println("a");
		fileContent.removeAll();
		GridBagLayout fileBag = new GridBagLayout();
		fileContent.setLayout(fileBag);
		GridBagConstraints fileConstraints=new GridBagConstraints();
		fileConstraints.anchor = GridBagConstraints.WEST;
		fileConstraints.fill=GridBagConstraints.NONE;
		fileConstraints.insets=new Insets(10,10,10,10);
		try {
			clientEnd.getFileList(path,new CallBackFunc() {
				@Override
				public void done(CallBackFunArg callBackFunArg) throws Exception{
					System.out.println(callBackFunArg.jsonObject.toJSONString());
					JSONArray fileList = callBackFunArg.jsonObject.getJSONArray("children");
					for(int i=0;i<fileList.size();i++){
						JSONObject obj = (JSONObject) fileList.get(i);
						JButton b = new JButton(obj.getString("name"));
						if(obj.get("type").toString().equals("FOLDER")) b.setIcon(folder);
						else b.setIcon(txt);
						b.setVerticalTextPosition(JButton.BOTTOM);
						b.setHorizontalTextPosition(JButton.CENTER);
						b.setBackground(Color.white);
						b.setBorder(BorderFactory.createLineBorder(Color.white));
						b.setPreferredSize(new Dimension(90,100));
						b.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								if(e.getClickCount() == 2 && obj.get("type").toString().equals("FOLDER")) {
									try {
										getInNextLevel(obj.getString("fullPath"),clientEnd,fileContent);
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
							}
						});
						MouseListener popupListener = rightClick(obj.get("id"),obj.getString("type"), obj.getString("fullPath"), fileContent,b);
						b.addMouseListener(popupListener);
						fileBag.setConstraints(b,fileConstraints);
						if(i%10 == 0) fileConstraints.gridwidth=GridBagConstraints.REMAINDER;
						else fileConstraints.gridwidth = 1;
						fileContent.add(b);
					}
					fileContent.revalidate();
					fileContent.repaint();
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void getInNextLevel(String p,ClientEnd clientEnd,JPanel fileContent) throws Exception {
		clientEnd.getFileList(p, new CallBackFunc() {
			@Override
			public void done(CallBackFunArg callBackFunArg) throws Exception {
				JSONObject obj = callBackFunArg.jsonObject;
				String temp = showFileWay.getText()+"/"+obj.getString("name");
				showFileWay.setText(temp);
				path = obj.getString("fullPath");
				showFile(path);
			}
		});
	}

	private MouseListener rightClick(Object id,String type, String fullPath, JPanel content,JButton button){
		int ID = (int)id;
		//右键弹出下载、分享、删除
		JPopupMenu jPopupMenuOne = new JPopupMenu();
		JMenuItem down = new JMenuItem("下载");
		down.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setSelectedFile(new File(button.getText()));
					int userChoice = fileChooser.showSaveDialog(content);
					if (userChoice == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();
						JFrame progressWin = new JFrame("下载进度");
						JProgressBar progressBar = new JProgressBar();
						progressBar.setValue(0);
						progressBar.setStringPainted(true);
						progressWin.setSize(360, 80);
						progressWin.add(progressBar);
						progressWin.setLocationRelativeTo(null);
						progressWin.setVisible(true);
						clientEnd.download(ID, selectedFile.getAbsolutePath(), new CallBackFunc() {
							@Override
							public void done(CallBackFunArg callBackFunArg) throws Exception {
								progressWin.setTitle("下载完成");
								JOptionPane.showMessageDialog(progressWin, "下载完成");
								progressWin.setVisible(false);
								progressWin.dispose();
							}
						}, new CallBackFunc() {
							@Override
							public void done(CallBackFunArg callBackFunArg) throws Exception {
								progressBar.setValue((Integer) callBackFunArg.jsonObject.get("length"));
							}
						});
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		if(type.equals("FILE")){
			jPopupMenuOne.add(down);
			jPopupMenuOne.addSeparator();
		}
		JMenuItem share = new JMenuItem("分享");
		share.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clientEnd.shareFile(ID, new CallBackFunc() {
					@Override
					public void done(CallBackFunArg callBackFunArg) throws Exception {
						String temp = callBackFunArg.jsonObject.getString("id");
						String link = "http://cloud.sysu.rwong.tech:8080/share/"+temp;
						JFrame linkWin = new JFrame("分享链接已生成，快去分享吧");
						linkWin.setSize(360, 80);
						linkWin.add(new JTextField(link));
						linkWin.setLocationRelativeTo(null);
						linkWin.setVisible(true);
						showShare();
					}
				});
			}
		});
		if(type.equals("FILE")){
			jPopupMenuOne.add(share);
			jPopupMenuOne.addSeparator();
		}
		JMenuItem delete = new JMenuItem("删除");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (type.equals("FILE")) {
						clientEnd.delFile(ID, new CallBackFunc() {
							@Override
							public void done(CallBackFunArg callBackFunArg) throws Exception {
								if(callBackFunArg.bool) {
									showFile(path);
								}
							}
						});
					} else {
						clientEnd.delFolder(fullPath, new CallBackFunc() {
							@Override
							public void done(CallBackFunArg callBackFunArg) throws Exception {
								if(callBackFunArg.bool) {
									showFile(path);
								}
							}
						});
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		jPopupMenuOne.add(delete);
		return new PopupListener(jPopupMenuOne);
	}

	protected JScrollPane makeSharePage(JFrame cloud,ClientEnd clientEnd) {
		JScrollPane sharePage = new JScrollPane();
		shareContent = new JTable();
		shareContent.setBackground(Color.white);
		DefaultTableModel model = (DefaultTableModel)shareContent.getModel();
		model.setColumnIdentifiers(new Object[]{"文件名称","分享链接","分享时间"});
		shareContent.setModel(model);
		shareContent.getTableHeader().setReorderingAllowed(false);
		sharePage.getViewport().setBackground(shareContent.getBackground());
		sharePage.setViewportView(shareContent);
		return sharePage;
	}

	protected void showShare(){
		try {
			clientEnd.getShareList(new CallBackFunc() {
				@Override
				public void done(CallBackFunArg callBackFunArg) throws Exception {
					JSONArray list= callBackFunArg.jsonArray;
					DefaultTableModel model = (DefaultTableModel)shareContent.getModel();
					if (model.getRowCount() > 0) {
						for (int i = model.getRowCount() - 1; i > -1; i--) {
							model.removeRow(i);
						}
					}
					for(int i=0;i<list.size();i++){
						JSONObject obj = (JSONObject) list.get(i);
						String temp = obj.getString("id");
						String link = "http://cloud.sysu.rwong.tech:8080/share/"+temp;
						model.addRow(new Object[]{obj.getString("name"),link,obj.getString("createdAt")});
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
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
				popupMenu.show(e.getComponent(),e.getX(),e.getY());
			}
		}
	}
}

