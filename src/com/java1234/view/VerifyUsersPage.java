package com.java1234.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;

import com.java1234.dao.UserDao;
import com.java1234.model.User;
import com.java1234.util.DbUtil;

public class VerifyUsersPage extends JFrame {

    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField searchTxt;
    
    private DbUtil dbUtil = new DbUtil();
    private UserDao userDao = new UserDao();

    public VerifyUsersPage() {
        setTitle("Verify Users");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel titleLbl = new JLabel("Verify Users");
        titleLbl.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLbl.setBounds(20, 20, 250, 30);
        titleLbl.setIcon(new ImageIcon("src/images/verify users.png"));
        getContentPane().add(titleLbl);

        JButton btnClose = new JButton("");
        btnClose.setIcon(new ImageIcon("src/images/close.png"));
        btnClose.setBounds(930, 20, 30, 30);
        btnClose.addActionListener(e -> setVisible(false));
        getContentPane().add(btnClose);

        JLabel lblSearch = new JLabel("Search by Email");
        lblSearch.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblSearch.setBounds(350, 80, 150, 20);
        getContentPane().add(lblSearch);

        searchTxt = new JTextField();
        searchTxt.setBounds(500, 75, 200, 30);
        searchTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                loadTableData(searchTxt.getText());
            }
        });
        getContentPane().add(searchTxt);

        JLabel lblInfo = new JLabel("*Click on row to change status");
        lblInfo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblInfo.setBounds(400, 420, 250, 20);
        getContentPane().add(lblInfo);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Email", "Mobile Number", "Address", "Security Question", "Status"}, 0);
        userTable = new JTable(tableModel);
        userTable.setRowHeight(30);
        userTable.setFont(new Font("Tahoma", Font.PLAIN, 14));
        userTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        userTable.getTableHeader().setBackground(new Color(16, 185, 129));
        userTable.getTableHeader().setForeground(Color.WHITE);
        userTable.setSelectionBackground(new Color(16, 185, 129, 80));
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBounds(20, 130, 940, 270);
        getContentPane().add(scrollPane);

        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = userTable.getSelectedRow();
                if (row != -1) {
                    String email = tableModel.getValueAt(row, 2).toString();
                    String status = tableModel.getValueAt(row, 6).toString();
                    
                    if(status.equals("true")) {
                        status = "false";
                    } else {
                        status = "true";
                    }
                    
                    int a = JOptionPane.showConfirmDialog(null, "Do you want to change status of " + email + "?", "Select", JOptionPane.YES_NO_OPTION);
                    if (a == 0) {
                        Connection con = null;
                        try {
                            con = dbUtil.getCon();
                            userDao.changeStatus(con, email, status);
                            JOptionPane.showMessageDialog(null, "Status Changed Successfully!");
                            loadTableData(searchTxt.getText());
                        } catch(Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            try { dbUtil.closeCon(con); } catch(Exception ex){}
                        }
                    }
                }
            }
        });

        // Background Image
        JLabel background = new JLabel(new ImageIcon("src/images/full-page-background.PNG"));
        background.setBounds(0, 0, 1000, 500);
        getContentPane().add(background);

        loadTableData("");
    }

    private void loadTableData(String email) {
        tableModel.setRowCount(0);
        Connection con = null;
        try {
            con = dbUtil.getCon();
            ArrayList<User> list = userDao.getAllUsers(con, email);
            for (User u : list) {
                if(!u.getEmail().equals("admin@gmail.com")) {
                    tableModel.addRow(new Object[]{u.getId(), u.getName(), u.getEmail(), u.getMobileNumber(), u.getAddress(), u.getSecurityQuestion(), u.getStatus()});
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }
    }
}
