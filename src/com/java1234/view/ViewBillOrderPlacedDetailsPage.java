package com.java1234.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;

import com.java1234.dao.BillDao;
import com.java1234.model.Bill;
import com.java1234.util.DbUtil;

public class ViewBillOrderPlacedDetailsPage extends JFrame {

    private JTable billTable;
    private DefaultTableModel tableModel;
    private JTextField searchTxt;
    
    private DbUtil dbUtil = new DbUtil();
    private BillDao billDao = new BillDao();

    public ViewBillOrderPlacedDetailsPage() {
        setTitle("View Bill & Order Placed Details");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel titleLbl = new JLabel("View Bills & Order Placed Details");
        titleLbl.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLbl.setBounds(20, 20, 500, 30);
        titleLbl.setIcon(new ImageIcon("src/images/View Bills & Order Placed Details.png"));
        getContentPane().add(titleLbl);

        JButton btnClose = new JButton("");
        btnClose.setIcon(new ImageIcon("src/images/close.png"));
        btnClose.setBounds(930, 20, 30, 30);
        btnClose.addActionListener(e -> setVisible(false));
        getContentPane().add(btnClose);

        JLabel lblSearch = new JLabel("Filter by Date");
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

        JLabel lblInfo = new JLabel("*Click on row to open Bill");
        lblInfo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblInfo.setBounds(400, 420, 250, 20);
        getContentPane().add(lblInfo);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Bill ID", "Name", "Email", "Mobile Number", "Date", "Total", "Created By"}, 0);
        billTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(billTable);
        scrollPane.setBounds(20, 130, 940, 270);
        getContentPane().add(scrollPane);

        billTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = billTable.getSelectedRow();
                if (row != -1) {
                    String uuid = tableModel.getValueAt(row, 1).toString();
                    try {
                        String fullPath = "bills/" + uuid + ".pdf";
                        if((new File(fullPath)).exists()) {
                            Desktop.getDesktop().open(new File(fullPath));
                        } else {
                            JOptionPane.showMessageDialog(null, "File is not exists");
                        }
                    } catch(Exception ex) {
                        ex.printStackTrace();
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

    private void loadTableData(String date) {
        tableModel.setRowCount(0);
        Connection con = null;
        try {
            con = dbUtil.getCon();
            ArrayList<Bill> list = billDao.getAllRecordsByInc(con, date);
            for (Bill b : list) {
                tableModel.addRow(new Object[]{b.getId(), b.getUuid(), b.getName(), b.getEmail(), b.getMobileNumber(), b.getDate(), b.getTotal(), b.getCreatedBy()});
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }
    }
}
