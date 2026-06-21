package com.java1234.view;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;

import com.java1234.dao.CategoryDao;
import com.java1234.dao.ProductDao;
import com.java1234.model.Category;
import com.java1234.model.Product;
import com.java1234.util.DbUtil;

public class NewProductPage extends JFrame {

    private JTextField nameTxt;
    private JComboBox<String> categoryCombo;
    private JTextField priceTxt;
    
    private DbUtil dbUtil = new DbUtil();
    private CategoryDao categoryDao = new CategoryDao();
    private ProductDao productDao = new ProductDao();

    public NewProductPage() {
        setTitle("New Product");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel titleLbl = new JLabel("New Product");
        titleLbl.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLbl.setBounds(20, 20, 300, 30);
        titleLbl.setIcon(new ImageIcon("src/images/new product.png"));
        getContentPane().add(titleLbl);

        JButton btnClose = new JButton("");
        btnClose.setIcon(new ImageIcon("src/images/close.png"));
        btnClose.setBounds(630, 20, 30, 30);
        btnClose.addActionListener(e -> setVisible(false));
        getContentPane().add(btnClose);

        int startX = 200;
        int startY = 80;
        int gapY = 50;

        JLabel lblName = new JLabel("Name");
        lblName.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblName.setBounds(startX, startY, 100, 30);
        getContentPane().add(lblName);

        nameTxt = new JTextField();
        nameTxt.setBounds(startX + 100, startY, 250, 30);
        getContentPane().add(nameTxt);

        startY += gapY;
        JLabel lblCategory = new JLabel("Category");
        lblCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblCategory.setBounds(startX, startY, 100, 30);
        getContentPane().add(lblCategory);

        categoryCombo = new JComboBox<>();
        categoryCombo.setBounds(startX + 100, startY, 250, 30);
        getContentPane().add(categoryCombo);

        startY += gapY;
        JLabel lblPrice = new JLabel("Price");
        lblPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPrice.setBounds(startX, startY, 100, 30);
        getContentPane().add(lblPrice);

        priceTxt = new JTextField();
        priceTxt.setBounds(startX + 100, startY, 250, 30);
        getContentPane().add(priceTxt);

        startY += gapY;
        JButton btnSave = new JButton("Save");
        btnSave.setIcon(new ImageIcon("src/images/save.png"));
        btnSave.setBounds(startX + 100, startY, 100, 30);
        btnSave.addActionListener(e -> saveAction());
        getContentPane().add(btnSave);

        JButton btnClear = new JButton("Clear");
        btnClear.setIcon(new ImageIcon("src/images/clear.png"));
        btnClear.setBounds(startX + 220, startY, 100, 30);
        btnClear.addActionListener(e -> clearAction());
        getContentPane().add(btnClear);

        // Background Image
        JLabel background = new JLabel(new ImageIcon("src/images/small-page-background.png"));
        background.setBounds(0, 0, 700, 400);
        getContentPane().add(background);

        loadCategory();
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

    private void saveAction() {
        String name = nameTxt.getText();
        String category = (String) categoryCombo.getSelectedItem();
        String price = priceTxt.getText();

        if (name.isEmpty() || category == null || price.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required!");
            return;
        }

        Product product = new Product();
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);

        Connection con = null;
        try {
            con = dbUtil.getCon();
            productDao.save(con, product);
            JOptionPane.showMessageDialog(null, "Product Added Successfully!");
            clearAction();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }
    }

    private void clearAction() {
        nameTxt.setText("");
        priceTxt.setText("");
    }
}
