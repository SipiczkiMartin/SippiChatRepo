package frontend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.beans.factory.parsing.PassThroughSourceExtractor;

import networking.NetworkComm;
import utilities.Communication;
import utilities.Friend;
import utilities.Login;
import utilities.State;

public class GUI {

	private static String user;
	private static String receiver;
	private static JTextArea chatArea;
	private static JList<Friend> nameList;
	private static JScrollPane namesPane = null;
	
	
	private static NetworkComm netCom;
	
	
	public GUI(String serverUrl) {
		GUI.chatArea = new JTextArea();
		GUI.netCom = new NetworkComm(serverUrl);
		GUI.receiver = null;
	}
	
	
	
	public static void login() {
	
		JFrame frame = new JFrame("SippiChat Login");
		
		
		JTextField name = new JTextField();
		name.setBounds(120, 50, 150, 30);
		
		JPasswordField pass = new JPasswordField();
		pass.setBounds(120, 100, 150, 30);
		
		JTextField port = new JTextField();
		port.setBounds(120, 150, 150, 30);
		
		JLabel nameLabel = new JLabel("Name: ");
		nameLabel.setBounds(30, 50, 50, 30);
		JLabel passLabel = new JLabel("Password: ");
		passLabel.setBounds(30, 100, 70, 30);
		
		JLabel portLabel =  new JLabel("Port: ");
		portLabel.setBounds(30, 150, 50, 30);
		
		JButton okButton = new JButton("OK");
		okButton.setBounds(30, 200, 100, 30);
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String enteredName = name.getText();
				char[] enteredPass = pass.getPassword();
				String passString = new String(enteredPass);
				
				user = enteredName;
				
				boolean loggedIn = GUI.netCom.login(enteredName, passString);
				
				if(loggedIn) {  
					frame.dispose();
					chatMainWindow();
				}else {
					JOptionPane.showMessageDialog(frame, "Could not log in!");
				}
			}
		});
		
		JButton newButton = new JButton("New User");
		newButton.setBounds(160, 200, 100, 30);
		
		newButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("New User");
				
				JLabel nameLabel = new JLabel("Username:");
				nameLabel.setBounds(10, 10, 90, 30);
				
				JTextField nameText = new JTextField();
				nameText.setBounds(120, 10, 150, 30);
				
				JLabel passLabel = new JLabel("Password:");
				passLabel.setBounds(10, 50, 90, 30);
				
				JPasswordField passField = new JPasswordField();
				passField.setBounds(120, 50, 150, 30);
				
				JLabel repeatLabel = new JLabel("Password again:");
				repeatLabel.setBounds(10, 90, 110, 30);
				
				JPasswordField repeatField = new JPasswordField();
				repeatField.setBounds(120, 90, 150, 30);
				
				JButton Button = new JButton("OK");
				Button.setBounds(10, 140, 100, 30);
				
				Button.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						String name = nameText.getText();
						
						char[] enteredPass = passField.getPassword();
						String passString = new String(enteredPass);
						
						char[] repeatedPass = repeatField.getPassword();
						String repeatString = new String(repeatedPass);
						
						if(passString.contentEquals(repeatString)) {
							Login login = new Login(name, passString);
							boolean check = netCom.newUser(login);
							if(check) {
								JOptionPane.showMessageDialog(frame, "User created! Please log in!");
								frame.dispose();
							}else {
								JOptionPane.showMessageDialog(frame, "Username taken try again");
								nameText.setText("");
								passField.setText("");
								repeatField.setText("");
							}
							
						}else {
							JOptionPane.showMessageDialog(frame, "Passwords don't match! Try again");
							passField.setText("");
							repeatField.setText("");
						}
						
					}
				});
				
				frame.add(Button);
				frame.add(nameLabel);
				frame.add(nameText);
				frame.add(passLabel);
				frame.add(passField);
				frame.add(repeatLabel);
				frame.add(repeatField);
				
				frame.setSize(300,230);
				frame.setLayout(null);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
				
			}
		});
		
		frame.add(newButton);
		frame.add(okButton);
		frame.add(passLabel);
		frame.add(nameLabel);
		frame.add(pass);
		frame.add(name);
		frame.add(portLabel);
		frame.add(port);
		
		frame.setSize(300, 300);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private static void chatMainWindow() {
		
		final String newline = "\n";
		JFrame frame = new JFrame("SippiChat");
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenu helpMenu = new JMenu("Help");
		
		JMenuItem i1 = new JMenuItem("BE A MAN");
		JMenuItem i2 = new JMenuItem("NUKE CANADA");
		
		i2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chatArea.append("Informing authorities..." + "\n");
				
			}
		});
		
		JMenuItem i3 = new JMenuItem("Add Friend");
		
		i3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input = JOptionPane.showInputDialog("Friends name:");
			    boolean check = netCom.addFriend(user,input);
			    if(!check) {
			    	updateNames();
			    	JOptionPane.showMessageDialog(frame, "Friend added");
			    }else {
			    	JOptionPane.showMessageDialog(frame, "Friend could not be found...");
			    }
			}
		});
		
		helpMenu.add(i1);
		menu.add(i3);
		menu.add(i2);
		
		menuBar.add(menu);
		menuBar.add(helpMenu);
		
		
		chatArea.setBounds(120, 20, 400, 370);
		JScrollPane scrollPane = new JScrollPane(chatArea);
		chatArea.setEditable(false);
		
		JTextField chatField = new JTextField();
		chatField.setBounds(120, 390, 300, 30);
		chatField.setToolTipText("Write something");
		
		JButton send = new JButton("Send");
		send.setBounds(440, 390, 70, 30);
		
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = chatField.getText();
				if(text != null && text.length() > 0) {
					chatArea.append(user + ": " + text + newline);
					chatField.setText("");
					if(GUI.receiver == null) {
						JOptionPane.showMessageDialog(frame, "No receiver selected from friends list");
					}else {
						netCom.send(user, receiver, text); 
					}
				}
				
			}
		});
		
		frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				int a = JOptionPane.showConfirmDialog(frame, "Are you sure?");
				if(a == JOptionPane.YES_OPTION) {
					netCom.disconnectFromServer(user);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		ArrayList<Friend> friends = netCom.getFriends(user); //get friends from db
		DefaultListModel<Friend> listModel = null;
		
		if(friends != null) {
			
			listModel = new DefaultListModel<Friend>(); //add method for filling model with names...
			for(Friend f : friends) {
				listModel.addElement(f);
			}
		
		}else {
			listModel = new DefaultListModel<Friend>();
		}
		
		nameList = new JList<Friend>(listModel);
		nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		nameList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					final java.util.List<Friend> selectedValues = nameList.getSelectedValuesList();
					if(selectedValues.size() > 0) {
						for(Friend f : selectedValues) {
							GUI.receiver = f.getName();
						}
					}
				}
				
			}
		});
		
		namesPane = new JScrollPane(nameList);
		namesPane.setBounds(10, 20, 100, 400);
		frame.add(namesPane);
		
		frame.add(scrollPane);
		frame.add(chatArea);
		frame.add(send);
		frame.add(chatField);
		frame.setJMenuBar(menuBar);
		
		frame.setSize(560, 500);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public void checkForMessages() {
		ArrayList<Communication> comms = netCom.check(user);
		
		for(Communication c : comms) {
			if(c.getState() == State.Okay && c.getMessage() != null) {
				chatArea.append(c.getSenderName() + ": " + c.getMessage() + "\n");
			}
		}
		
		
	}
	
	public static void updateNames() {
		ArrayList<Friend> friends = netCom.getFriends(user); //get friends from db
		if(nameList != null) {
			DefaultListModel<Friend> listModel = (DefaultListModel<Friend>) nameList.getModel();
			
			listModel.clear();
			
			for(Friend f : friends) {
				listModel.addElement(f);
			}
		}
	}
	
	public static void chatAreaAccess(String text,String sender) {
		chatArea.append(sender + ": " + text + "\n");
	}

	

}
