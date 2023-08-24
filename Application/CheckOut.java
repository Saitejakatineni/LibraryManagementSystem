import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CheckOut extends JFrame
{
	JFrame mainFrame;
	JPanel controlPanel;
	static Connection conn;
	
	CheckOut()
	{
		prepareGUI();
	}
	
	public static void main(String[] args)
	{
		CheckOut checkOut=new CheckOut();
		//mainPage.show();
	}
	
	void prepareGUI()
	{
		mainFrame=new JFrame("McDermott Library System");
		mainFrame.setSize(700,500);
		mainFrame.setLocation(50, 50);
		//mainFrame.setLayout(new GridLayout(2,1));
		mainFrame.setMinimumSize(mainFrame.getSize());
		mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		controlPanel=new JPanel();
		controlPanel.setBorder(new EmptyBorder(10,10,10,10));
		setContentPane(controlPanel);
		GridBagLayout gbl_controlPanel=new GridBagLayout();
		gbl_controlPanel.columnWidths=new int[]{0,0};
		gbl_controlPanel.rowHeights = new int[]{0, 0, 0,0,0,0};
		gbl_controlPanel.columnWeights = new double[]{Double.MIN_VALUE, Double.MIN_VALUE};
		gbl_controlPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		controlPanel.setLayout(gbl_controlPanel);
		controlPanel.setBackground(Color.orange);
		
		JLabel space1=new JLabel("  ",JLabel.CENTER);
		//searchLabel.setFont(new F);
		GridBagConstraints gbc_space1=new GridBagConstraints();
		gbc_space1.insets=new Insets(0,0,5,0);
		gbc_space1.gridx=0;
		gbc_space1.gridy=1;
		gbc_space1.gridwidth=2;
		controlPanel.add(space1, gbc_space1);

		//Book ISBN
		JLabel lblIsbn=new JLabel("     ISBN* :",JLabel.LEFT);
		lblIsbn.setFont(new Font("Times New Roman",Font.BOLD,14));
		GridBagConstraints gbc_lblIsbn=new GridBagConstraints();
		gbc_lblIsbn.insets=new Insets(0,0,5,0);
		gbc_lblIsbn.gridx=0;
		gbc_lblIsbn.gridy=2;
		controlPanel.add(lblIsbn, gbc_lblIsbn);
		
		//Isbn Text Box
		JTextField isbnText = new JTextField();
		
		isbnText.setFont(new Font("roboto",Font.PLAIN,14));
		isbnText.setForeground(Color.black);
		GridBagConstraints gbc_isbnText=new GridBagConstraints();
		gbc_isbnText.fill=GridBagConstraints.HORIZONTAL;
		gbc_isbnText.insets=new Insets(0,0,5,0);
		gbc_isbnText.gridx=1;
		gbc_isbnText.gridy=2;
		controlPanel.add(isbnText, gbc_isbnText);
		isbnText.setColumns(15);
		
		//Borrower Card No.
		JLabel lblcard=new JLabel("Card No.* :",JLabel.LEFT);
		lblcard.setFont(new Font("roboto",Font.BOLD,14));
		GridBagConstraints gbc_lblcard=new GridBagConstraints();
		gbc_lblcard.insets=new Insets(0,0,5,0);
		gbc_lblcard.gridx=0;
		gbc_lblcard.gridy=3;
		//gbc_lblIsbn.gridwidth=2;
		controlPanel.add(lblcard, gbc_lblcard);
		
		//Card No.Text Box
		JTextField cardText = new JTextField();
	
		cardText.setFont(new Font("roboto",Font.PLAIN,14));
		cardText.setForeground(Color.black);
		GridBagConstraints gbc_cardText =new GridBagConstraints();
		gbc_cardText.fill=GridBagConstraints.HORIZONTAL;
		gbc_cardText.insets=new Insets(0,0,5,0);
		gbc_cardText.gridx=1;
		gbc_cardText.gridy=3;
		controlPanel.add(cardText, gbc_cardText);
		cardText.setColumns(15);
		
		JButton checkOut=new JButton("Check Out");
		checkOut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{if(isbnText.getText().equals("") || cardText.getText().equals("") )
			{
				JOptionPane.showMessageDialog(null, "Please fill in the required fields");
			}
			else{
				try{
				String isbn=isbnText.getText();
				String cardId=  cardText.getText() ;
				String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DATE, 14);
				String dueDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
				

				conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/LIBRARY", "root", "");
				Statement stmt1=conn.createStatement();
				ResultSet rs1 = stmt1.executeQuery("select * from LIBRARY.BOOK where ISBN="+isbn+";");
				Statement stmt2 = conn.createStatement();
				ResultSet rs2 = stmt2.executeQuery("select * from LIBRARY.BORROWER where CARD_ID='"+cardId+"';");
					System.out.println(cardId);
					System.out.println(isbn);
					System.out.println(rs2.next());
					System.out.println(rs1.next());
				if(rs1.wasNull() || rs2.wasNull())
					{
						System.out.println("lawda11");
						JOptionPane.showMessageDialog(null, "Invalid Isbn or Card Id");
						isbnText.setText("");
						cardText.setText("");
					}
				else
				{
					System.out.println("lawda");
					Statement stmt3 = conn.createStatement();
					ResultSet rs3 = stmt3.executeQuery("select * from LIBRARY.book_loan where Isbn='"+isbn+"' and Date_in is null;");
					System.out.println(rs3.next());
					if(rs3.next())
					{
						JOptionPane.showMessageDialog(null, "This book has already been issued");	
						isbnText.setText(""); 
					}
					else
					{
						Statement stmt4 = conn.createStatement();
						ResultSet rs4 = stmt4.executeQuery("select * from LIBRARY.book_loan where Card_id='"+cardId+"' and Date_in is null and Due_date<CAST(CURRENT_TIMESTAMP AS DATE);");
						if(rs4.next())
						{
							JOptionPane.showMessageDialog(null, "The borrower already has an overdue book");	
							isbnText.setText(""); 
							cardText.setText("");
						}
						else
						{
							Statement stmt5 = conn.createStatement();
							ResultSet rs5 = stmt5.executeQuery("select count(*) from LIBRARY.book_loan where Card_id='"+cardId+"' and Date_in is null;");
							rs5.next();
							if(rs5.getInt(1)>=3)
							{
								JOptionPane.showMessageDialog(null, "The borrower already has 3 active book loans");
								isbnText.setText(""); 
								cardText.setText("");
							}
							else
							{
								Statement stmt6 = conn.createStatement();
								System.out.println("rs6");
								ResultSet rs6 = stmt6.executeQuery("select * from LIBRARY.book_loan b,fines f where Card_id='"+cardId+"' and b.Loan_id=f.Loan_id and paid=0;");
								if(rs6.next())
								{
									JOptionPane.showMessageDialog(null, "The borrower has to pay some fine");
									isbnText.setText(""); 
									cardText.setText("");
								}
								else{
									System.out.println("last");
									Statement stmt = conn.createStatement();
									ResultSet rs= stmt.executeQuery("select max(Loan_id) from book_loan");
									int id=0;
									if(rs.next())
									{				 	
									 id=rs.getInt(1)+1;
									}
									
									String sql="INSERT INTO Book_loan (Loan_id, Isbn, Card_id, Date_out, Due_date) VALUES ("+id+", '"+isbn+"', "+"\""+cardId+"\""+" , '"+date+"', '"+dueDate+"');";
									Statement stmt7 = conn.createStatement();
									stmt7.executeUpdate(sql);
									JOptionPane.showMessageDialog(null, "Done");
									isbnText.setText(""); 
									cardText.setText("");
								}
							}
						}
					}
				}

				
				
		    }
			catch(SQLException ex) {
				System.out.println("Error in connection: " + ex.getMessage());
			}
			
			}
			}
		});
		GridBagConstraints gbc_btnCheckOut=new GridBagConstraints();
		gbc_btnCheckOut.fill=GridBagConstraints.VERTICAL;
		gbc_btnCheckOut.insets = new Insets(0, 0, 5, 0);
		gbc_btnCheckOut.gridx = 1;
		gbc_btnCheckOut.gridy = 5;
		controlPanel.add(checkOut,gbc_btnCheckOut);
		
		JLabel space2=new JLabel("  ",JLabel.CENTER);
		//searchLabel.setFont(new F);
		GridBagConstraints gbc_space2=new GridBagConstraints();
		gbc_space2.insets=new Insets(0,0,5,0);
		gbc_space2.gridx=1;
		gbc_space2.gridy=0;
		gbc_space2.gridwidth=2;
		controlPanel.add(space2, gbc_space2);
		
		//Close Button
		JButton close=new JButton("Back");
		close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				mainFrame.setVisible(false);
				new MainPage();
			}
		});
		GridBagConstraints gbc_btnClose=new GridBagConstraints();
		gbc_btnClose.fill=GridBagConstraints.VERTICAL;
		gbc_btnClose.insets = new Insets(0, 0, 5, 0);
		gbc_btnClose.gridx = 0;
		gbc_btnClose.gridy = 5;
		gbc_btnClose.anchor=GridBagConstraints.PAGE_END;
		gbc_btnClose.gridwidth=2;
		controlPanel.add(close, gbc_btnClose);		

		
		mainFrame.add(controlPanel);
		mainFrame.setVisible(true);
	}
}