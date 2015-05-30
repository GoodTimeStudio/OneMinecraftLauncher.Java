package com.mcgoodtime.gtgames.client.gui;

import com.mcgoodtime.gtgames.core.Auth;
import com.mcgoodtime.gtgames.resources.ResourcesManager;

import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import com.mcgoodtime.gtgames.client.panel.LoginPanel;

public class MainFrame extends JFrame {

	protected static JPanel mainPanel;
	private static JPanel container;
	public static JPanel containerLogin;
	private static JEditorPane notePanel;


	private static JLabel labLogin;
	private static JLabel labLoginState;
	
	int mx, my, fx, fy;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		/* ***********main Window************ */
		setUndecorated(true);
		setTitle("GoodTime Games");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 450);
		mainPanel = new JPanel() {
			@Override
			protected void paintBorder(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setColor(Color.black);
				Stroke s = new BasicStroke(3);
				g2d.setStroke(s);
				g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
			}
		};
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		mainPanel.setLayout(null);
		
		//change Look And Feel
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		JPanel title = new JPanel() {
			@Override
			protected void paintComponent(Graphics arg0) {
				super.paintComponent(arg0);
				Graphics2D g2d = (Graphics2D) arg0;//Graphics2D
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2d.setColor(Color.black);//draw bar
				g2d.setClip(0, 0, getWidth(), 30);
				g2d.fillRect(0, 0, getWidth(), getHeight());
				g2d.setClip(null);

				g2d.setColor(Color.WHITE);//set color
				g2d.setFont(new Font("微软雅黑", Font.PLAIN, 15));//set font
				g2d.drawString("GoodTime游戏平台", 20, 20);//draw title
			}
		};
		title.setBounds(0, 0, getWidth(), 30);

		title.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				setLocation(fx + (arg0.getXOnScreen() - mx), fy + (arg0.getYOnScreen() - my));//鼠标拖拽
			}
		});
		//鼠标拖住监听
		title.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				mx = arg0.getXOnScreen();
				my = arg0.getYOnScreen();
				fx = getX();
				fy = getY();
			}
		});
		
		/*  All Items */
		
		// close button
		ImageIcon iconClose = new ImageIcon(ResourcesManager.getTexture("close.png")); //get icon
		JLabel labClose = new JLabel(iconClose);
		labClose.setBounds(670, 0, 30, 30);
		//单机事件
		labClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.exit(0);
			}
		});
				
		//add item to main pane
		mainPanel.add(labClose);
		mainPanel.add(title);

		new LoginPage();
		
	}

	static class LoginPage {
		private static JTextField textUsername;
		private static JPasswordField textPassword;

		public LoginPage() {
			/* Auth Container */
			containerLogin = new LoginPanel();
			containerLogin.setBounds(2, 30, 696, 418);
			containerLogin.setLayout(null);
			mainPanel.add(containerLogin);
			/* *************** */
			
			/* **** text **** */
			textUsername = new JTextField();
			textUsername.setFont(new Font("微软雅黑", Font.PLAIN, 12));
			textUsername.setBounds(containerLogin.getWidth() / 2 - 100, containerLogin.getHeight() / 2 - 80, 200, 30);
			textUsername.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					if (e.getKeyChar() == '\n') {
						login();
					}
				}
			});

			textPassword = new JPasswordField();
			textPassword.setFont(new Font("微软雅黑", Font.PLAIN, 12));
			textPassword.setBounds(containerLogin.getWidth() / 2 - 100, containerLogin.getHeight() / 2 - 40, 200, 30);
			textPassword.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					if (e.getKeyChar() == '\n') {
						login();
					}
				}
			});

			/* **** button **** */
			//login
			ImageIcon iconLogin = new ImageIcon(ResourcesManager.getTexture("next.png")); //get icon
			labLogin = new JLabel(iconLogin);
			labLogin.setToolTipText("登陆");
			labLogin.setBounds(containerLogin.getWidth() / 2 + 80, containerLogin.getHeight() / 2 - 95, 100, 100);

			labLogin.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					login();
				}
			});

			//login state
			labLoginState = new JLabel();
			labLoginState.setFont(new Font("微软雅黑", Font.PLAIN, 12));
			labLoginState.setBounds(containerLogin.getWidth() / 2 - 100, containerLogin.getHeight() / 2, 200, 20);


			//add item to login page
			containerLogin.add(textUsername);
			containerLogin.add(textPassword);

			containerLogin.add(labLogin);
			containerLogin.add(labLoginState);
		}

		private static void login() {
			String textUsernameText = textUsername.getText();

			if (!textUsernameText.isEmpty()) {
				char[] password = textPassword.getPassword();
				if (password.length < 1) {
					labLoginState.setForeground(Color.RED);
					labLoginState.setText("请输入正确的密码");
				} else {
					//auth
					ImageIcon iconSignUp = new ImageIcon(ResourcesManager.getTexture("loading.gif")); //get icon
					labLoginState.setIcon(iconSignUp);
					labLoginState.setText("正在连接登陆服务器...");

					boolean loginServerState = Auth.getLoginServerState(); //get Server State(WIP)
					if (loginServerState) {
								/*
								 * Login, if return true, hide the login page,go to next.
								 */
						boolean login = Auth.Login(textUsernameText, password);
						if (login) {
							//login success, go to main page
							containerLogin.setVisible(false);
							mainPanel.remove(containerLogin); //disable login page.
							new MainPage();// go to next.
						} else {
							textPassword.setText(null);
						}
					}
				}
			} else {
				labLoginState.setForeground(Color.RED);
				labLoginState.setText("请输入正确的用户名");
			}
		}
	}

	static class MainPage {
		public MainPage() {

			/* **********container******* */
			container = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2d = (Graphics2D) g;
					InputStream inMainBackground = ResourcesManager.getTextureAsStream("mainBackground.png");
					BufferedImage imageMainBackground = null;
					try {
						imageMainBackground = ImageIO.read(inMainBackground);
					} catch (IOException e) {
						e.printStackTrace();
					}
					g2d.drawImage(imageMainBackground, 0, 0, container.getWidth(), container.getHeight(), null);
				}
			};
			container.setBounds(2, 30, 696, 418);
			container.setLayout(null);
			container.setVisible(true);
			mainPanel.add(container);
			/* ************************** */

			/* Notes Panel */
			notePanel = new JEditorPane() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2d = (Graphics2D) g;

					g2d.setColor(new Color(255, 255, 255, 75));
					g2d.fillRect(1, 1, getWidth() - 2, getHeight() - 2);

					super.paintComponent(g);
				}
			};
			notePanel.setBounds(0, 0, 200, container.getHeight() - 81);
			notePanel.setEditable(false);
			notePanel.setOpaque(false);

			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						notePanel.setPage("http://minecraft-goodtime.github.io/UpdateNotes/MinecraftGoodTime.html"); //get page to note
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

			/* ========Note Panel=========*/

			/* Server Panel */
			JPanel containerServers = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2d = (Graphics2D) g;

					g2d.setColor(new Color(255, 255, 255, 75));
					g2d.fillRect(0, 0, getWidth(), getHeight());
				}
			};
			containerServers.setBounds(container.getWidth() - 200, 0, 200, container.getHeight());
			containerServers.setOpaque(false);
			containerServers.setLayout(null);
			/* =============== */

			/* button */
			//launch button
			ImageIcon iconLaunch = new ImageIcon(ResourcesManager.getTexture("next.png"));
			JLabel labLaunch = new JLabel(iconLaunch);
			labLaunch.setToolTipText("启动客户端");
			labLaunch.setBounds(containerServers.getWidth() - 60, containerServers.getHeight() - 65, 50, 50);

			//setting button
			ImageIcon iconSetting = new ImageIcon(ResourcesManager.getTexture("setting.png"));
			JLabel labSetting = new JLabel(iconSetting);
			labSetting.setToolTipText("设置");
			labSetting.setBounds(containerServers.getWidth() - 120, containerServers.getHeight() - 65, 50, 50);
			labSetting.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					container.setVisible(false);
					new SettingPage();
				}
			});

			//add item to container Servers
			containerServers.add(labLaunch);
			containerServers.add(labSetting);

			/* ======Server Panel======= */


			/* Info Panel */
			JPanel infoPanel = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2d = (Graphics2D) g;

					g2d.setColor(new Color(64, 64, 64, 150));
					g2d.fillRect(0, 0, getWidth(), getHeight());
				}
			};
			infoPanel.setBounds(0, container.getHeight() - 80, container.getWidth() - 200, 80);
			infoPanel.setOpaque(false);
			infoPanel.setLayout(null);

			/* label */

			//user photo
			JLabel labUserPhoto = new JLabel();
			labUserPhoto.setBounds(0, 0, 80, 80);
			InputStream inUserPhoto = ResourcesManager.getTextureAsStream("steve.png");
			BufferedImage imageUserPhoto = null;
			try {
				imageUserPhoto = ImageIO.read(inUserPhoto);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ImageIcon iconUserPhoto = new ImageIcon(imageUserPhoto.getScaledInstance(80, 80, Image.SCALE_SMOOTH)); //change icon size.
			labUserPhoto.setIcon(iconUserPhoto);

			//add item to info panel
			infoPanel.add(labUserPhoto);

			/* ====info panel===== */

			//add item to container
			container.add(notePanel);
			container.add(infoPanel);
			container.add(containerServers);
		}
	}

	static class SettingPage {
		public SettingPage() {
			final JPanel containerSetting = new JPanel() {
				@Override
				protected void paintComponent(Graphics arg0) {
					super.paintComponent(arg0);
					Graphics2D g2d = (Graphics2D) arg0;//Graphics2D
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                	/* draw String */
					g2d.setFont(new Font("微软雅黑", Font.PLAIN, 30));
					g2d.drawString("设置", 50, 50);

					g2d.setFont(new Font("微软雅黑", Font.PLAIN, 15));
					g2d.drawString("更改启动设置和平台设置（高级功能，如非必要，请勿修改）", 50, 70);
				}
			};
			containerSetting.setBounds(2, 30, 696, 418);
			containerSetting.setLayout(null);
			mainPanel.add(containerSetting);

			/* button */
			//save option
			ImageIcon iconSave = new ImageIcon(ResourcesManager.getTexture("save.png"));
			JLabel labSave = new JLabel(iconSave);
			labSave.setBounds(containerSetting.getWidth() - 60, containerSetting.getHeight() - 60, 50, 50);
			labSave.setToolTipText("保存设置");

			//cancel button
			ImageIcon iconCancel = new ImageIcon(ResourcesManager.getTexture("cancel.png"));
			JLabel labCancel = new JLabel(iconCancel);
			labCancel.setBounds(containerSetting.getWidth() - 120, containerSetting.getHeight() - 60, 50, 50);
			labCancel.setToolTipText("取消并返回");
			labCancel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					containerSetting.setVisible(false);
					container.setVisible(true);
					mainPanel.remove(containerSetting);
				}
			});

			//add item to container Setting
			containerSetting.add(labSave);
			containerSetting.add(labCancel);
		}
	}
}
