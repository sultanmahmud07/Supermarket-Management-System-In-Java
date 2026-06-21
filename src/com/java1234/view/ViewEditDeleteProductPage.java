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
    private JTextField imagePathTxt;
    private JLabel imgPreviewLbl;
    private java.io.File selectedFile = null;
    private String selectedImagePath = "";
    private ArrayList<Product> productList = new ArrayList<>();
    
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
        int startY = 100;
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

        startY += gapY;
        JLabel lblImage = new JLabel("Image");
        lblImage.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblImage.setForeground(Color.WHITE);
        lblImage.setBounds(startX, startY, 80, 20);
        formPanel.add(lblImage);

        imagePathTxt = new JTextField();
        imagePathTxt.setBounds(startX + 80, startY, 140, 30);
        imagePathTxt.setEditable(false);
        formPanel.add(imagePathTxt);

        JButton btnUpload = new JButton("Choose");
        btnUpload.setBounds(startX + 230, startY, 100, 30);
        btnUpload.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                imagePathTxt.setText(selectedFile.getName());
                ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imgPreviewLbl.setIcon(new ImageIcon(img));
                imgPreviewLbl.setText("");
            }
        });
        formPanel.add(btnUpload);

        startY += gapY;
        JLabel lblPreview = new JLabel("Preview:");
        lblPreview.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPreview.setForeground(Color.WHITE);
        lblPreview.setBounds(startX, startY, 80, 20);
        formPanel.add(lblPreview);

        imgPreviewLbl = new JLabel("No Image");
        imgPreviewLbl.setFont(new Font("Tahoma", Font.ITALIC, 12));
        imgPreviewLbl.setForeground(Color.LIGHT_GRAY);
        imgPreviewLbl.setBounds(startX + 80, startY, 100, 100);
        imgPreviewLbl.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        imgPreviewLbl.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(imgPreviewLbl);

        // Buttons
        startY += gapY + 60;
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
        productTable.setRowHeight(30);
        productTable.setFont(new Font("Tahoma", Font.PLAIN, 14));
        productTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        productTable.getTableHeader().setBackground(new Color(16, 185, 129));
        productTable.getTableHeader().setForeground(Color.WHITE);
        productTable.setSelectionBackground(new Color(16, 185, 129, 80));
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBounds(400, 100, 580, 450);
        formPanel.add(scrollPane);

        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = productTable.getSelectedRow();
                if (row != -1) {
                    Product p = productList.get(row);
                    idValLbl.setText(String.valueOf(p.getId()));
                    nameTxt.setText(p.getName());
                    categoryCombo.setSelectedItem(p.getCategory());
                    priceTxt.setText(p.getPrice());
                    imagePathTxt.setText(p.getImagePath() != null ? new java.io.File(p.getImagePath()).getName() : "");
                    displayImage(p.getImagePath());
                }
            }
        });

        bgPanel.add(formPanel);

        loadCategory();
        loadTableData();
    }

    private void displayImage(String path) {
        if (path == null || path.isEmpty()) {
            imgPreviewLbl.setIcon(null);
            imgPreviewLbl.setText("No Image");
            selectedImagePath = "";
        } else {
            java.io.File file = new java.io.File(path);
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imgPreviewLbl.setIcon(new ImageIcon(img));
                imgPreviewLbl.setText("");
                selectedImagePath = path;
            } else {
                imgPreviewLbl.setIcon(null);
                imgPreviewLbl.setText("Not Found");
                selectedImagePath = "";
            }
        }
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
        productList.clear();
        Connection con = null;
        try {
            con = dbUtil.getCon();
            productList = productDao.getAllRecords(con);
            for (Product p : productList) {
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

        // Handle image upload if selected
        String finalImagePath = selectedImagePath;
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
                finalImagePath = "product_images/" + uniqueName;
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving product image!");
                return;
            }
        }

        Product product = new Product();
        product.setId(Integer.parseInt(idValLbl.getText()));
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setImagePath(finalImagePath);

        Connection con = null;
        try {
            con = dbUtil.getCon();
            productDao.update(con, product);
            JOptionPane.showMessageDialog(null, "Product Updated Successfully!");
            clearAction();
            loadTableData();
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error!");
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
        imagePathTxt.setText("");
        imgPreviewLbl.setIcon(null);
        imgPreviewLbl.setText("No Image");
        selectedFile = null;
        selectedImagePath = "";
    }
}
