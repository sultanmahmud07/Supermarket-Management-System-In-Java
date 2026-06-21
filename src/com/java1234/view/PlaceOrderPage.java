package com.java1234.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.java1234.dao.BillDao;
import com.java1234.dao.CategoryDao;
import com.java1234.dao.ProductDao;
import com.java1234.model.Bill;
import com.java1234.model.Category;
import com.java1234.model.Product;
import com.java1234.util.DbUtil;

public class PlaceOrderPage extends JFrame {

    private String userEmail;
    private String uuid;
    private int grandTotal = 0;
    private int productPrice = 0;
    private int productTotal = 0;

    private JTextField nameTxt;
    private JTextField mobileTxt;
    private JTextField emailTxt;
    private JComboBox<String> categoryCombo;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;

    private JTextField pNameTxt;
    private JTextField pPriceTxt;
    private JTextField pQuantityTxt;
    private JTextField pTotalTxt;
    private JLabel lblGrandTotal;

    private DbUtil dbUtil = new DbUtil();
    private CategoryDao categoryDao = new CategoryDao();
    private ProductDao productDao = new ProductDao();
    private BillDao billDao = new BillDao();

    public PlaceOrderPage(String userEmail) {
        this.userEmail = userEmail;
        uuid = UUID.randomUUID().toString().substring(0, 8); // short uuid
        
        setTitle("Place Order");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
        formPanel.setPreferredSize(new Dimension(1300, 700));
        formPanel.setOpaque(false);

        JLabel titleLbl = new JLabel("Place Order");
        titleLbl.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setBounds(20, 20, 250, 30);
        titleLbl.setIcon(new ImageIcon("src/images/place order.png"));
        formPanel.add(titleLbl);

        JButton btnClose = new JButton("");
        btnClose.setIcon(new ImageIcon("src/images/close.png"));
        btnClose.setBounds(1250, 20, 30, 30);
        btnClose.addActionListener(e -> setVisible(false));
        formPanel.add(btnClose);

        JLabel lblBillId = new JLabel("Bill ID: " + uuid);
        lblBillId.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblBillId.setForeground(Color.WHITE);
        lblBillId.setBounds(50, 80, 200, 20);
        formPanel.add(lblBillId);

        // Customer Details
        JLabel lblCD = new JLabel("Customer Details:");
        lblCD.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblCD.setForeground(Color.WHITE);
        lblCD.setBounds(50, 120, 200, 20);
        formPanel.add(lblCD);

        JLabel lblName = new JLabel("Name");
        lblName.setForeground(Color.WHITE);
        lblName.setBounds(50, 150, 200, 20);
        formPanel.add(lblName);
        nameTxt = new JTextField();
        nameTxt.setBounds(50, 170, 250, 30);
        formPanel.add(nameTxt);

        JLabel lblMobile = new JLabel("Mobile Number");
        lblMobile.setForeground(Color.WHITE);
        lblMobile.setBounds(50, 210, 200, 20);
        formPanel.add(lblMobile);
        mobileTxt = new JTextField();
        mobileTxt.setBounds(50, 230, 250, 30);
        formPanel.add(mobileTxt);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setBounds(50, 270, 200, 20);
        formPanel.add(lblEmail);
        emailTxt = new JTextField();
        emailTxt.setBounds(50, 290, 250, 30);
        formPanel.add(emailTxt);

        // Category & Product List
        JLabel lblCategory = new JLabel("Category");
        lblCategory.setForeground(Color.WHITE);
        lblCategory.setBounds(350, 80, 200, 20);
        formPanel.add(lblCategory);
        
        categoryCombo = new JComboBox<>();
        categoryCombo.setBounds(350, 100, 250, 30);
        categoryCombo.addActionListener(e -> productByCategory((String)categoryCombo.getSelectedItem()));
        formPanel.add(categoryCombo);

        productTableModel = new DefaultTableModel(new Object[]{"Name"}, 0);
        productTable = new JTable(productTableModel);
        JScrollPane scrollPane1 = new JScrollPane(productTable);
        scrollPane1.setBounds(350, 150, 250, 350);
        formPanel.add(scrollPane1);

        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = productTable.getSelectedRow();
                if (row != -1) {
                    String pName = productTableModel.getValueAt(row, 0).toString();
                    productNameSelected(pName);
                }
            }
        });

        // Product Details
        JLabel lblPName = new JLabel("Name");
        lblPName.setForeground(Color.WHITE);
        lblPName.setBounds(650, 80, 100, 20);
        formPanel.add(lblPName);
        pNameTxt = new JTextField();
        pNameTxt.setBounds(650, 100, 200, 30);
        pNameTxt.setEditable(false);
        formPanel.add(pNameTxt);

        JLabel lblPPrice = new JLabel("Price");
        lblPPrice.setForeground(Color.WHITE);
        lblPPrice.setBounds(880, 80, 100, 20);
        formPanel.add(lblPPrice);
        pPriceTxt = new JTextField();
        pPriceTxt.setBounds(880, 100, 200, 30);
        pPriceTxt.setEditable(false);
        formPanel.add(pPriceTxt);

        JLabel lblPQuantity = new JLabel("Quantity");
        lblPQuantity.setForeground(Color.WHITE);
        lblPQuantity.setBounds(650, 140, 100, 20);
        formPanel.add(lblPQuantity);
        pQuantityTxt = new JTextField();
        pQuantityTxt.setBounds(650, 160, 200, 30);
        pQuantityTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateTotal();
            }
        });
        formPanel.add(pQuantityTxt);

        JLabel lblPTotal = new JLabel("Total");
        lblPTotal.setForeground(Color.WHITE);
        lblPTotal.setBounds(880, 140, 100, 20);
        formPanel.add(lblPTotal);
        pTotalTxt = new JTextField();
        pTotalTxt.setBounds(880, 160, 200, 30);
        pTotalTxt.setEditable(false);
        formPanel.add(pTotalTxt);

        JButton btnClear = new JButton("Clear");
        btnClear.setIcon(new ImageIcon("src/images/clear.png"));
        btnClear.setBounds(650, 200, 100, 30);
        btnClear.addActionListener(e -> clearProductFields());
        formPanel.add(btnClear);

        JButton btnAddToCart = new JButton("Add to Cart");
        btnAddToCart.setIcon(new ImageIcon("src/images/add to cart.png"));
        btnAddToCart.setBounds(930, 200, 150, 30);
        btnAddToCart.addActionListener(e -> addToCart());
        formPanel.add(btnAddToCart);

        // Cart Table
        cartTableModel = new DefaultTableModel(new Object[]{"Name", "Price", "Quantity", "Total"}, 0);
        cartTable = new JTable(cartTableModel);
        JScrollPane scrollPane2 = new JScrollPane(cartTable);
        scrollPane2.setBounds(650, 250, 430, 250);
        formPanel.add(scrollPane2);
        
        cartTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = cartTable.getSelectedRow();
                if (row != -1) {
                    int a = JOptionPane.showConfirmDialog(null, "Do you want to remove this product?", "Select", JOptionPane.YES_NO_OPTION);
                    if (a == 0) {
                        int rowTotal = Integer.parseInt(cartTableModel.getValueAt(row, 3).toString());
                        grandTotal -= rowTotal;
                        lblGrandTotal.setText(String.valueOf(grandTotal));
                        cartTableModel.removeRow(row);
                    }
                }
            }
        });

        // Grand Total & Generate Bill
        JLabel gtLbl = new JLabel("Grand Total: $");
        gtLbl.setFont(new Font("Tahoma", Font.BOLD, 18));
        gtLbl.setForeground(Color.WHITE);
        gtLbl.setBounds(650, 520, 150, 30);
        formPanel.add(gtLbl);

        lblGrandTotal = new JLabel("0");
        lblGrandTotal.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblGrandTotal.setForeground(Color.WHITE);
        lblGrandTotal.setBounds(800, 520, 100, 30);
        formPanel.add(lblGrandTotal);

        JButton btnGenerateBill = new JButton("Generate Bill & Print");
        btnGenerateBill.setIcon(new ImageIcon("src/images/generate bill & print.png"));
        btnGenerateBill.setBounds(900, 520, 180, 30);
        btnGenerateBill.addActionListener(e -> generateBill());
        formPanel.add(btnGenerateBill);

        bgPanel.add(formPanel);

        loadCategory();
    }

    private void loadCategory() {
        Connection con = null;
        try {
            con = dbUtil.getCon();
            ArrayList<Category> list = categoryDao.getAllRecords(con);
            categoryCombo.removeAllItems();
            for (Category c : list) {
                categoryCombo.addItem(c.getName());
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }
    }

    private void productByCategory(String category) {
        if(category == null) return;
        productTableModel.setRowCount(0);
        Connection con = null;
        try {
            con = dbUtil.getCon();
            ArrayList<Product> list = productDao.getByCategory(con, category);
            for (Product p : list) {
                productTableModel.addRow(new Object[]{p.getName()});
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }
    }

    private void productNameSelected(String name) {
        Connection con = null;
        try {
            con = dbUtil.getCon();
            ArrayList<Product> list = productDao.getAllRecords(con);
            for(Product p : list) {
                if(p.getName().equals(name)) {
                    pNameTxt.setText(p.getName());
                    pPriceTxt.setText(p.getPrice());
                    productPrice = Integer.parseInt(p.getPrice());
                    pQuantityTxt.setText("1");
                    pTotalTxt.setText(p.getPrice());
                    productTotal = productPrice;
                    break;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }
    }

    private void calculateTotal() {
        String qty = pQuantityTxt.getText();
        if(!qty.isEmpty()) {
            try {
                int q = Integer.parseInt(qty);
                productTotal = productPrice * q;
                pTotalTxt.setText(String.valueOf(productTotal));
            } catch(Exception e) {
                pTotalTxt.setText("");
            }
        } else {
            pTotalTxt.setText("");
        }
    }

    private void clearProductFields() {
        pNameTxt.setText("");
        pPriceTxt.setText("");
        pQuantityTxt.setText("");
        pTotalTxt.setText("");
    }

    private void addToCart() {
        if(pNameTxt.getText().isEmpty() || pQuantityTxt.getText().isEmpty() || pTotalTxt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select product and quantity");
            return;
        }
        
        String name = pNameTxt.getText();
        String price = pPriceTxt.getText();
        String qty = pQuantityTxt.getText();
        String total = pTotalTxt.getText();
        
        cartTableModel.addRow(new Object[]{name, price, qty, total});
        grandTotal += Integer.parseInt(total);
        lblGrandTotal.setText(String.valueOf(grandTotal));
        clearProductFields();
    }

    private void generateBill() {
        String name = nameTxt.getText();
        String mobile = mobileTxt.getText();
        String email = emailTxt.getText();
        
        if(name.isEmpty() || mobile.isEmpty() || email.isEmpty() || grandTotal == 0) {
            JOptionPane.showMessageDialog(null, "Please fill customer details and add products to cart!");
            return;
        }

        SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String todayDate = dFormat.format(date);
        String createdBy = userEmail;
        
        Bill bill = new Bill();
        bill.setUuid(uuid);
        bill.setName(name);
        bill.setEmail(email);
        bill.setMobileNumber(mobile);
        bill.setDate(todayDate);
        bill.setTotal(String.valueOf(grandTotal));
        bill.setCreatedBy(createdBy);
        
        Connection con = null;
        try {
            con = dbUtil.getCon();
            billDao.save(con, bill);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }

        // Generate PDF
        String path = "bills/";
        File dir = new File(path);
        if(!dir.exists()) dir.mkdir();
        
        String fullPath = path + uuid + ".pdf";
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(fullPath));
            doc.open();
            Paragraph p1 = new Paragraph("                                       Supermarket Management System\n");
            doc.add(p1);
            Paragraph p2 = new Paragraph("*************************************************************************************************\n");
            doc.add(p2);
            Paragraph p3 = new Paragraph("Bill ID: " + uuid + "\nCustomer Name: " + name + "\nTotal Paid: " + grandTotal + "\n");
            doc.add(p3);
            doc.add(p2);
            
            PdfPTable pdfTable = new PdfPTable(4);
            pdfTable.addCell("Name");
            pdfTable.addCell("Price");
            pdfTable.addCell("Quantity");
            pdfTable.addCell("Total");
            
            for(int i=0; i<cartTable.getRowCount(); i++) {
                String n = cartTable.getValueAt(i, 0).toString();
                String p = cartTable.getValueAt(i, 1).toString();
                String q = cartTable.getValueAt(i, 2).toString();
                String t = cartTable.getValueAt(i, 3).toString();
                pdfTable.addCell(n);
                pdfTable.addCell(p);
                pdfTable.addCell(q);
                pdfTable.addCell(t);
            }
            doc.add(pdfTable);
            doc.add(p2);
            Paragraph p4 = new Paragraph("Thank You, Please Visit Again!");
            doc.add(p4);
            
            doc.close();
            
            JOptionPane.showMessageDialog(null, "Bill Generated Successfully!");
            
            int a = JOptionPane.showConfirmDialog(null, "Do you want to Print Bill?", "Select", JOptionPane.YES_NO_OPTION);
            if(a == 0) {
                if((new File(fullPath)).exists()) {
                    Desktop.getDesktop().open(new File(fullPath));
                } else {
                    JOptionPane.showMessageDialog(null, "File is not exists");
                }
            }
            setVisible(false);
            new PlaceOrderPage(createdBy).setVisible(true); // reload
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
