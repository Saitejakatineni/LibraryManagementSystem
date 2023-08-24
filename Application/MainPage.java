import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class MainPage extends JFrame
{
	JFrame mainFrame;
	JPanel controlPanel;
	JLabel headerLabel;
	
	static Connection conn;

	Image logo = Toolkit.getDefaultToolkit().getImage("/Users/apple/Downloads/Library_Management_System-master/Application/logo.jpg");
	private static JLabel backgroundImageLabel;
	MainPage()
	{
		prepareGUI();
	}

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				try{
					MainPage mainPage=new MainPage();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		
	}
	
	void prepareGUI()
	{
		mainFrame=new JFrame("McDermott Library System");
		mainFrame.setSize(700,500);
		mainFrame.setLocation(50, 50);
		mainFrame.setLayout(new GridLayout(2,1));
		mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainFrame.getContentPane().setBackground(Color.ORANGE);
		mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("logo.jpg")));

		controlPanel=new JPanel();
		controlPanel.setBorder(new EmptyBorder(10,10,10,10));
		setContentPane(controlPanel);
		GridBagLayout gbl_controlPanel=new GridBagLayout();
		gbl_controlPanel.columnWidths=new int[]{0,0};
		gbl_controlPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,0,0};
		gbl_controlPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_controlPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		controlPanel.setLayout(gbl_controlPanel);
		controlPanel.setBackground(Color.ORANGE);
		
		//Heading
		headerLabel=new JLabel("McDermott Library System",JLabel.CENTER);
		headerLabel.setFont(new Font("Roboto",Font.BOLD,22));
		headerLabel.setForeground(Color.black);
		headerLabel.setBackground(Color.green);
		
		//search button
		JButton search=new JButton("Search Here");
		search.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
					mainFrame.setVisible(false);
					try {
						new Search();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		GridBagConstraints gbc_btnSearch=new GridBagConstraints();
		gbc_btnSearch.fill=GridBagConstraints.VERTICAL;
		gbc_btnSearch.insets = new Insets(0, 0, 5, 0);
		gbc_btnSearch.gridx = 1;
		gbc_btnSearch.gridy = 0;
		controlPanel.add(search, gbc_btnSearch);
		
				
		
		//Check Out Button
		JButton checkOut=new JButton("Check Out");
		checkOut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				mainFrame.setVisible(false);
				new CheckOut();
			}
		});
		GridBagConstraints gbc_btnCheckOut=new GridBagConstraints();
		gbc_btnCheckOut.fill=GridBagConstraints.VERTICAL;
		gbc_btnCheckOut.insets = new Insets(0, 0, 5, 0);
		gbc_btnCheckOut.gridx = 2;
		gbc_btnCheckOut.gridy = 0;
		controlPanel.add(checkOut,gbc_btnCheckOut);
		
		//Check In Button
		JButton checkIn=new JButton("Check In");
		checkIn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				mainFrame.setVisible(false);
				new CheckIn();
			}
		});
		GridBagConstraints gbc_btnCheckIn=new GridBagConstraints();
		gbc_btnCheckIn.fill=GridBagConstraints.VERTICAL;
		gbc_btnCheckIn.insets = new Insets(0, 0, 5, 0);
		gbc_btnCheckIn.gridx = 3;
		gbc_btnCheckIn.gridy = 0;
		controlPanel.add(checkIn, gbc_btnCheckIn);

		
		//New Borrower Button
		JButton newBorrower = new JButton("Register");
		newBorrower.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				mainFrame.setVisible(false);
				new NewBorrower();
			}
		});
		GridBagConstraints gbc_btnNewBorrower=new GridBagConstraints();
		gbc_btnNewBorrower.fill=GridBagConstraints.VERTICAL;
		gbc_btnNewBorrower.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewBorrower.gridx = 4;
		gbc_btnNewBorrower.gridy = 0;
		controlPanel.add(newBorrower,gbc_btnNewBorrower);
		
		//Fines Button
		JButton fines=new JButton("Fines");
		fines.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				mainFrame.setVisible(false);
				new Fines();
			}
		});
		GridBagConstraints gbc_btnFines=new GridBagConstraints();
		gbc_btnFines.fill=GridBagConstraints.VERTICAL;
		gbc_btnFines.insets = new Insets(0, 0, 5, 0);
		gbc_btnFines.gridx = 5;
		gbc_btnFines.gridy = 0;
		controlPanel.add(fines, gbc_btnFines);		
		
		//JLabel
		JLabel space=new JLabel(" ");
		GridBagConstraints gbc_lblSpace=new GridBagConstraints();
		gbc_lblSpace.fill=GridBagConstraints.VERTICAL;
		gbc_lblSpace.insets = new Insets(0, 0, 5, 0);
		gbc_lblSpace.gridx = 6;
		gbc_lblSpace.gridy = 0;
		controlPanel.add(space, gbc_lblSpace);		


		//Close Button
		JButton close=new JButton("Close");
		close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				mainFrame.dispose();
			}
		});
		GridBagConstraints gbc_btnClose=new GridBagConstraints();
		gbc_btnClose.fill=GridBagConstraints.HORIZONTAL;
		gbc_btnClose.insets = new Insets(0, 0, 5, 0);
		gbc_btnClose.gridx = 7;
		gbc_btnClose.gridy = 1;
		gbc_btnClose.anchor=GridBagConstraints.PAGE_END;
		controlPanel.add(close, gbc_btnClose);		

		
		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.setVisible(true);
	}
}