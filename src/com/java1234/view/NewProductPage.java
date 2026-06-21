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
    private JTextField imagePathTxt;
    private java.io.File selectedFile = null;
    
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
        JLabel lblImage = new JLabel("Image");
        lblImage.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblImage.setBounds(startX, startY, 100, 30);
        getContentPane().add(lblImage);

        imagePathTxt = new JTextField();
        imagePathTxt.setBounds(startX + 100, startY, 140, 30);
        imagePathTxt.setEditable(false);
        getContentPane().add(imagePathTxt);

        JButton btnUpload = new JButton("Choose");
        btnUpload.setBounds(startX + 250, startY, 100, 30);
        btnUpload.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                imagePathTxt.setText(selectedFile.getName());
            }
        });
        getContentPane().add(btnUpload);

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

        // Validate price
        try {
            double p = Double.parseDouble(price);
            if (p <= 0) {
                JOptionPane.showMessageDialog(null, "Price must be a positive number!");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Price must be a valid number!");
            return;
        }

        // Handle image upload
        String copiedPath = "";
        if (selectedFile != null) {
            try {
                java.io.File destDir = new java.io.File("product_images");
                if (!destDir.exists()) {
                    destDir.mkdir();
                }
                String ext = "";
                String nameOnly = selectedFile.getName();
                int idx = nameOnly.lastIndexOf('.');
                if (idx > 0) {
                    ext = nameOnly.substring(idx);
                }
                String uniqueName = java.util.UUID.randomUUID().toString() + ext;
                java.io.File destFile = new java.io.File(destDir, uniqueName);
                java.nio.file.Files.copy(selectedFile.toPath(), destFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                copiedPath = "product_images/" + uniqueName;
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving product image!");
                return;
            }
        }

        Product product = new Product();
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setImagePath(copiedPath);

        Connection con = null;
        try {
            con = dbUtil.getCon();
            productDao.save(con, product);
            JOptionPane.showMessageDialog(null, "Product Added Successfully!");
            clearAction();
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error!");
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }
    }

    private void clearAction() {
        nameTxt.setText("");
        priceTxt.setText("");
        imagePathTxt.setText("");
        selectedFile = null;
    }
}
