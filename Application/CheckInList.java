import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CheckInList extends JFrame
{
	JFrame mainFrame;
	JPanel controlPanel;
	static Connection conn=null;
	int row=0;
	
	JTable table;
	
	CheckInList(String isbn, String card, String borrower) throws SQLException
	{
		prepareGUI(isbn,card,borrower);
	}
	
	
	void prepareGUI(String isbn, String card, String borrower) throws SQLException
	{
		mainFrame=new JFrame("Library Management System");
		mainFrame.setSize(500,500);
		mainFrame.setLocation(20, 50);
		mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		controlPanel=new JPanel();
		controlPanel.setBorder(new EmptyBorder(10,10,10,10));
		setContentPane(controlPanel);
		GridBagLayout gbl_controlPanel=new GridBagLayout();
		gbl_controlPanel.columnWidths=new int[]{0,0};
		gbl_controlPanel.rowHeights = new int[]{0, 0, 0};
		gbl_controlPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_controlPanel.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		controlPanel.setLayout(gbl_controlPanel);
		
		JButton close= new JButton("Close");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setVisible(false); 
				mainFrame.dispose();
			}
		});
		
		JLayeredPane layeredPane=new JLayeredPane();
		GridBagConstraints gbc_layeredPane = new GridBagConstraints();
		gbc_layeredPane.insets = new Insets(0, 0, 5, 0);
		gbc_layeredPane.fill = GridBagConstraints.BOTH;
		gbc_layeredPane.gridx = 0;
		gbc_layeredPane.gridy = 0;
		controlPanel.add(layeredPane, gbc_layeredPane);
		
		JScrollPane scrollPane=new JScrollPane();
		scrollPane.setBounds(6,6,1200,600);
		layeredPane.add(scrollPane);
		
		table=new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		scrollPane.setViewportView(table);
		
		table.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent evnt)
			{
				System.out.println("ff");
				if(evnt.getClickCount() ==1 || evnt.getClickCount()==2)
				{
					String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
					System.out.println(date);
					try
					{
						conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/LIBRARY", "root", "");
						Statement stmt1=conn.createStatement();
						Statement stmt12=conn.createStatement();
						ResultSet rs2 = stmt12.executeQuery("select max(LOAN_ID) from LIBRARY.BOOK_LOAN;");
						int loan_id=0;
						if(rs2.next())
						{
							loan_id=rs2.getInt(1)+1;
						}
						stmt1.executeUpdate("Update LIBRARY.BOOK_LOAN set DATE_IN='"+date+"' where LOAN_ID="+loan_id+";");
						Statement stmt2=conn.createStatement();
						ResultSet rs= stmt2.executeQuery("Select * from LIBRARY.BOOK_LOAN where LOAN_ID="+loan_id+" and DUE_DATE<DATE_IN;");
						
						if(rs.next())
						{
							JOptionPane.showMessageDialog(null, "Book is overdue. There are fines.");
							mainFrame.setVisible(false);
							new Fines();
						}
						else
	                    {
							JOptionPane.showMessageDialog(null, "Checked in. No fines applied");
	                    	mainFrame.dispose();
	                    	new CheckIn();
	                    }
					}
					catch(SQLException e)
					{
						System.out.println("Sql Error : "+e.getMessage());
					}
				}
			}
		});
			
		//Close Button
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.fill=GridBagConstraints.HORIZONTAL;
		gbc_btnClose.insets = new Insets(0, 0, 5, 0);
		gbc_btnClose.gridx = 0;
		gbc_btnClose.gridy = 2;
		gbc_btnClose.anchor=GridBagConstraints.PAGE_END;
		controlPanel.add(close, gbc_btnClose);		
		try{		
			//jdbc connection to database
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/LIBRARY", "root", "");
			Statement stmt=conn.createStatement();
			ResultSet rs= null;
			

			if(borrower.equals(""))//isbn and card_id
			{
				rs=stmt.executeQuery("Select Loan_id,Isbn,Card_id from LIBRARY.book_loan where Isbn="+isbn+" and Card_id="+"\""+card+"\""+" and Date_in is null;");
				table.setModel(DbUtils.resultSetToTableModel(rs));
				table.setEnabled(false);

				table.getColumnModel().getColumn(1).setPreferredWidth(150);
				table.getColumnModel().getColumn(2).setPreferredWidth(150);

				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			}

			else//all 3 given
			{
				rs=stmt.executeQuery("Select Loan_id,Isbn,Card_id from LIBRARY.book_loan where Isbn="+isbn+" and Card_id="+"\""+card+"\""+" and Date_in is null;");


				table.setModel(DbUtils.resultSetToTableModel(rs));
				table.setEnabled(false);

				table.getColumnModel().getColumn(1).setPreferredWidth(150);
				table.getColumnModel().getColumn(2).setPreferredWidth(150);

				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			}
			String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
			int loanId = rs.getInt(1);
			String sql="UPDATE BOOK_LOAN SET DATE_IN = '"+date+"' WHERE LOAN_ID = '"+loanId+"';";
			Statement stmt7 = conn.createStatement();
			stmt7.executeUpdate(sql);
			}
			catch(SQLException e)
			{
				System.out.println(e.getMessage());
			}
		
		mainFrame.add(controlPanel);
		mainFrame.setVisible(true);
		
	}
	}

