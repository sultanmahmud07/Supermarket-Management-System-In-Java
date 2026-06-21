package com.java1234.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;

import com.java1234.dao.CategoryDao;
import com.java1234.model.Category;
import com.java1234.util.DbUtil;

public class ManageCategoryPage extends JFrame {

    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTextField nameTxt;
    
    private DbUtil dbUtil = new DbUtil();
    private CategoryDao categoryDao = new CategoryDao();

    public ManageCategoryPage() {
        setTitle("Manage Category");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel titleLbl = new JLabel("Manage Category");
        titleLbl.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLbl.setBounds(20, 20, 300, 30);
        titleLbl.setIcon(new ImageIcon("src/images/category.png"));
        getContentPane().add(titleLbl);

        JButton btnClose = new JButton("");
        btnClose.setIcon(new ImageIcon("src/images/close.png"));
        btnClose.setBounds(630, 20, 30, 30);
        btnClose.addActionListener(e -> setVisible(false));
        getContentPane().add(btnClose);

        JLabel lblView = new JLabel("View Category");
        lblView.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblView.setBounds(400, 60, 200, 20);
        getContentPane().add(lblView);

        JLabel lblInfo = new JLabel("*Click on row to Delete Category");
        lblInfo.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblInfo.setBounds(400, 320, 250, 20);
        getContentPane().add(lblInfo);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Category Name"}, 0);
        categoryTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setBounds(350, 90, 300, 220);
        getContentPane().add(scrollPane);

        categoryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = categoryTable.getSelectedRow();
                if (row != -1) {
                    String id = tableModel.getValueAt(row, 0).toString();
                    String name = tableModel.getValueAt(row, 1).toString();
                    int a = JOptionPane.showConfirmDialog(null, "Do you want to Delete Category: " + name + "?", "Select", JOptionPane.YES_NO_OPTION);
                    if (a == 0) {
                        Connection con = null;
                        try {
                            con = dbUtil.getCon();
                            categoryDao.delete(con, id);
                            JOptionPane.showMessageDialog(null, "Category Deleted Successfully!");
                            loadTableData();
                        } catch(Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            try { dbUtil.closeCon(con); } catch(Exception ex){}
                        }
                    }
                }
            }
        });

        // Add Category Form
        JLabel lblAdd = new JLabel("Add New Category");
        lblAdd.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblAdd.setBounds(50, 100, 200, 30);
        getContentPane().add(lblAdd);

        nameTxt = new JTextField();
        nameTxt.setBounds(50, 140, 250, 30);
        getContentPane().add(nameTxt);

        JButton btnSave = new JButton("Save");
        btnSave.setIcon(new ImageIcon("src/images/save.png"));
        btnSave.setBounds(50, 190, 100, 30);
        btnSave.addActionListener(e -> saveAction());
        getContentPane().add(btnSave);

        JButton btnClear = new JButton("Clear");
        btnClear.setIcon(new ImageIcon("src/images/clear.png"));
        btnClear.setBounds(170, 190, 100, 30);
        btnClear.addActionListener(e -> nameTxt.setText(""));
        getContentPane().add(btnClear);

        // Background Image
        JLabel background = new JLabel(new ImageIcon("src/images/small-page-background.png"));
        background.setBounds(0, 0, 700, 400);
        getContentPane().add(background);

        loadTableData();
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        Connection con = null;
        try {
            con = dbUtil.getCon();
            ArrayList<Category> list = categoryDao.getAllRecords(con);
            for (Category c : list) {
                tableModel.addRow(new Object[]{c.getId(), c.getName()});
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }
    }

    private void saveAction() {
        String name = nameTxt.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter Category Name!");
            return;
        }

        Category category = new Category();
        category.setName(name);

        Connection con = null;
        try {
            con = dbUtil.getCon();
            categoryDao.save(con, category);
            JOptionPane.showMessageDialog(null, "Category Added Successfully!");
            nameTxt.setText("");
            loadTableData();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }
    }
}
