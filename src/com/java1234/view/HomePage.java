package com.java1234.view;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {

    private String email;

    public HomePage(String userEmail) {
        this.email = userEmail;
        setTitle("Supermarket Management System - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen

        // Create responsive background panel
        JPanel bgPanel = new JPanel() {
            Image bgImage = new ImageIcon("src/images/home-background-image.png").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        bgPanel.setLayout(new BorderLayout());
        setContentPane(bgPanel);

        // Header Panel grouping Title and Navigation
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        JLabel mainTitleLbl = new JLabel("Supermarket Management System");
        mainTitleLbl.setFont(new Font("Tahoma", Font.BOLD, 42));
        mainTitleLbl.setForeground(new Color(16, 185, 129)); // Premium Emerald Green
        mainTitleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createVerticalStrut(20));
        headerPanel.add(mainTitleLbl);
        headerPanel.add(Box.createVerticalStrut(10));

        // Top Navigation Buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        topPanel.setOpaque(false);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setIcon(new ImageIcon("src/images/logout.png"));
        btnLogout.setPreferredSize(new Dimension(130, 40));
        btnLogout.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(null, "Do you really want to Logout", "Select", JOptionPane.YES_NO_OPTION);
            if (a == 0) {
                setVisible(false);
                new LoginPage().setVisible(true);
            }
        });
        topPanel.add(btnLogout);

        JButton btnPlaceOrder = new JButton("Place Order");
        btnPlaceOrder.setIcon(new ImageIcon("src/images/place order.png"));
        btnPlaceOrder.setPreferredSize(new Dimension(150, 40));
        btnPlaceOrder.addActionListener(e -> new PlaceOrderPage(email).setVisible(true));
        topPanel.add(btnPlaceOrder);

        JButton btnViewBill = new JButton("View Bill & Order Placed Details");
        btnViewBill.setIcon(new ImageIcon("src/images/View Bills & Order Placed Details.png"));
        btnViewBill.setPreferredSize(new Dimension(300, 40));
        btnViewBill.addActionListener(e -> new ViewBillOrderPlacedDetailsPage().setVisible(true));
        topPanel.add(btnViewBill);

        JButton btnChangePassword = new JButton("Change Password");
        btnChangePassword.setIcon(new ImageIcon("src/images/change Password.png"));
        btnChangePassword.setPreferredSize(new Dimension(200, 40));
        btnChangePassword.addActionListener(e -> new ChangePasswordPage(email).setVisible(true));
        topPanel.add(btnChangePassword);

        JButton btnChangeSq = new JButton("Change Security Question");
        btnChangeSq.setIcon(new ImageIcon("src/images/change Security Question.png"));
        btnChangeSq.setPreferredSize(new Dimension(250, 40));
        btnChangeSq.addActionListener(e -> new ChangeSecurityQuestionPage(email).setVisible(true));
        topPanel.add(btnChangeSq);

        JButton btnExit = new JButton("Exit");
        btnExit.setIcon(new ImageIcon("src/images/exit.png"));
        btnExit.setPreferredSize(new Dimension(100, 40));
        btnExit.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(null, "Do you really want to Close Application", "Select", JOptionPane.YES_NO_OPTION);
            if (a == 0) {
                System.exit(0);
            }
        });
        topPanel.add(btnExit);

        headerPanel.add(topPanel);
        bgPanel.add(headerPanel, BorderLayout.NORTH);

        // Bottom Admin Buttons
        if (email != null && email.equals("admin@gmail.com")) {
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
            bottomPanel.setOpaque(false);

            JButton btnManageCat = new JButton("Manage Category");
            btnManageCat.setIcon(new ImageIcon("src/images/category.png"));
            btnManageCat.setPreferredSize(new Dimension(200, 40));
            btnManageCat.addActionListener(e -> new ManageCategoryPage().setVisible(true));
            bottomPanel.add(btnManageCat);

            JButton btnNewProduct = new JButton("New Product");
            btnNewProduct.setIcon(new ImageIcon("src/images/new product.png"));
            btnNewProduct.setPreferredSize(new Dimension(180, 40));
            btnNewProduct.addActionListener(e -> new NewProductPage().setVisible(true));
            bottomPanel.add(btnNewProduct);

            JButton btnViewEditDelProd = new JButton("View, Edit & Delete Product");
            btnViewEditDelProd.setIcon(new ImageIcon("src/images/view edit delete product.png"));
            btnViewEditDelProd.setPreferredSize(new Dimension(250, 40));
            btnViewEditDelProd.addActionListener(e -> new ViewEditDeleteProductPage().setVisible(true));
            bottomPanel.add(btnViewEditDelProd);

            JButton btnVerifyUsers = new JButton("Verify Users");
            btnVerifyUsers.setIcon(new ImageIcon("src/images/verify users.png"));
            btnVerifyUsers.setPreferredSize(new Dimension(180, 40));
            btnVerifyUsers.addActionListener(e -> new VerifyUsersPage().setVisible(true));
            bottomPanel.add(btnVerifyUsers);

            bgPanel.add(bottomPanel, BorderLayout.SOUTH);
        }
    }
}
