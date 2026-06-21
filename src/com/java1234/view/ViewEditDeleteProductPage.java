package com.java1234.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;

import com.java1234.dao.CategoryDao;
import com.java1234.dao.ProductDao;
import com.java1234.model.Category;
import com.java1234.model.Product;
import com.java1234.util.DbUtil;

public class ViewEditDeleteProductPage extends JFrame {

    private JTable productTable;
    private DefaultTableModel tableModel;
    private JLabel idValLbl;
    private JTextField nameTxt;
    private JComboBox<String> categoryCombo;
    private JTextField priceTxt;
    
    private DbUtil dbUtil = new DbUtil();
    private CategoryDao categoryDao = new CategoryDao();
    private ProductDao productDao = new ProductDao();

    public ViewEditDeleteProductPage() {
        setTitle("View, Edit & Delete Product");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen

        // Create responsive background panel
        JPanel bgPanel = new JPanel() {
            Image bgImage = new ImageIcon("src/images/full-page-background.PNG").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        bgPanel.setLayout(new GridBagLayout());
        setContentPane(bgPanel);

        // Form Panel (Centered)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setPreferredSize(new Dimension(1000, 600));
        formPanel.setOpaque(false);

        JLabel titleLbl = new JLabel("View, Edit & Delete Product");
        titleLbl.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setBounds(20, 20, 400, 30);
        titleLbl.setIcon(new ImageIcon("src/images/view edit delete product.png"));
        formPanel.add(titleLbl);

        JButton btnClose = new JButton("");
        btnClose.setIcon(new ImageIcon("src/images/close.png"));
        btnClose.setBounds(950, 20, 30, 30);
        btnClose.addActionListener(e -> setVisible(false));
        formPanel.add(btnClose);

        // Edit Form (Left Side)
        int startX = 20;
        int startY = 150;
        int gapY = 50;

        JLabel lblId = new JLabel("ID:");
        lblId.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblId.setForeground(Color.WHITE);
        lblId.setBounds(startX, startY, 80, 20);
        formPanel.add(lblId);

        idValLbl = new JLabel("00");
        idValLbl.setFont(new Font("Tahoma", Font.BOLD, 14));
        idValLbl.setForeground(Color.WHITE);
        idValLbl.setBounds(startX + 80, startY, 200, 30);
        formPanel.add(idValLbl);

        startY += gapY;
        JLabel lblName = new JLabel("Name");
        lblName.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblName.setForeground(Color.WHITE);
        lblName.setBounds(startX, startY, 80, 20);
        formPanel.add(lblName);

        nameTxt = new JTextField();
        nameTxt.setBounds(startX + 80, startY, 250, 30);
        formPanel.add(nameTxt);

        startY += gapY;
        JLabel lblCategory = new JLabel("Category");
        lblCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblCategory.setForeground(Color.WHITE);
        lblCategory.setBounds(startX, startY, 80, 20);
        formPanel.add(lblCategory);

        categoryCombo = new JComboBox<>();
        categoryCombo.setBounds(startX + 80, startY, 250, 30);
        formPanel.add(categoryCombo);

        startY += gapY;
        JLabel lblPrice = new JLabel("Price");
        lblPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPrice.setForeground(Color.WHITE);
        lblPrice.setBounds(startX, startY, 80, 20);
        formPanel.add(lblPrice);

        priceTxt = new JTextField();
        priceTxt.setBounds(startX + 80, startY, 250, 30);
        formPanel.add(priceTxt);

        // Buttons
        startY += gapY + 10;
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setIcon(new ImageIcon("src/images/save.png"));
        btnUpdate.setBounds(startX + 20, startY, 100, 30);
        btnUpdate.addActionListener(e -> updateAction());
        formPanel.add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setIcon(new ImageIcon("src/images/delete.png"));
        btnDelete.setBounds(startX + 130, startY, 100, 30);
        btnDelete.addActionListener(e -> deleteAction());
        formPanel.add(btnDelete);

        JButton btnClear = new JButton("Clear");
        btnClear.setIcon(new ImageIcon("src/images/clear.png"));
        btnClear.setBounds(startX + 240, startY, 90, 30);
        btnClear.addActionListener(e -> clearAction());
        formPanel.add(btnClear);

        // Table (Right Side)
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Category", "Price"}, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBounds(400, 150, 580, 400);
        formPanel.add(scrollPane);

        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = productTable.getSelectedRow();
                if (row != -1) {
                    idValLbl.setText(tableModel.getValueAt(row, 0).toString());
                    nameTxt.setText(tableModel.getValueAt(row, 1).toString());
                    categoryCombo.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    priceTxt.setText(tableModel.getValueAt(row, 3).toString());
                }
            }
        });

        bgPanel.add(formPanel);

        loadCategory();
        loadTableData();
    }

    private void loadCategory() {
        Connection con = null;
        try {
            con = dbUtil.getCon();
            ArrayList<Category> list = categoryDao.getAllRecords(con);
            for (Category c : list) {
                categoryCombo.addItem(c.getName());
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        Connection con = null;
        try {
            con = dbUtil.getCon();
            ArrayList<Product> list = productDao.getAllRecords(con);
            for (Product p : list) {
                tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getCategory(), p.getPrice()});
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }
    }

    private void updateAction() {
        if(idValLbl.getText().equals("00")) {
            JOptionPane.showMessageDialog(null, "Select a product to update!");
            return;
        }
        Product product = new Product();
        product.setId(Integer.parseInt(idValLbl.getText()));
        product.setName(nameTxt.getText());
        product.setCategory((String)categoryCombo.getSelectedItem());
        product.setPrice(priceTxt.getText());

        Connection con = null;
        try {
            con = dbUtil.getCon();
            productDao.update(con, product);
            JOptionPane.showMessageDialog(null, "Product Updated Successfully!");
            clearAction();
            loadTableData();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }
    }

    private void deleteAction() {
        if(idValLbl.getText().equals("00")) {
            JOptionPane.showMessageDialog(null, "Select a product to delete!");
            return;
        }
        int a = JOptionPane.showConfirmDialog(null, "Do you really want to delete product?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            Connection con = null;
            try {
                con = dbUtil.getCon();
                productDao.delete(con, idValLbl.getText());
                JOptionPane.showMessageDialog(null, "Product Deleted Successfully!");
                clearAction();
                loadTableData();
            } catch(Exception ex) {
                ex.printStackTrace();
            } finally {
                try { dbUtil.closeCon(con); } catch(Exception e){}
            }
        }
    }

    private void clearAction() {
        idValLbl.setText("00");
        nameTxt.setText("");
        priceTxt.setText("");
    }
}
