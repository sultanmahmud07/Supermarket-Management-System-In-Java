package com.java1234.view;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import com.java1234.dao.UserDao;
import com.java1234.model.User;
import com.java1234.util.DbUtil;

public class LoginPage extends JFrame {

    private JTextField emailTxt;
    private JPasswordField passwordTxt;

    private DbUtil dbUtil = new DbUtil();
    private UserDao userDao = new UserDao();

    public LoginPage() {
        setTitle("Supermarket Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        getContentPane().setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenW = screenSize.width;
        int screenH = screenSize.height;

        // Programmatic Header Title
        JLabel mainTitleLbl = new JLabel("Supermarket Management System");
        mainTitleLbl.setForeground(new Color(16, 185, 129)); // Premium Emerald Green
        mainTitleLbl.setFont(new Font("Tahoma", Font.BOLD, 40));
        mainTitleLbl.setBounds(0, 140, screenW, 50);
        mainTitleLbl.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(mainTitleLbl);

        JLabel pageTitleLbl = new JLabel("Login");
        pageTitleLbl.setForeground(new Color(30, 41, 59)); // Premium Slate Gray
        pageTitleLbl.setFont(new Font("Tahoma", Font.BOLD, 32));
        pageTitleLbl.setBounds(0, 220, screenW, 40);
        pageTitleLbl.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(pageTitleLbl);

        int startX = (screenW - 530) / 2;
        int startY = 290;
        int gapY = 40;

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblEmail.setBounds(startX, startY, 150, 30);
        getContentPane().add(lblEmail);

        emailTxt = new JTextField();
        emailTxt.setBounds(startX + 180, startY, 350, 30);
        getContentPane().add(emailTxt);

        startY += gapY;
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPassword.setBounds(startX, startY, 150, 30);
        getContentPane().add(lblPassword);

        passwordTxt = new JPasswordField();
        passwordTxt.setBounds(startX + 180, startY, 350, 30);
        getContentPane().add(passwordTxt);

        // Buttons
        startY += gapY + 20;
        JButton btnLogin = new JButton("Login");
        btnLogin.setIcon(new ImageIcon("src/images/login.png"));
        btnLogin.setBounds(startX + 180, startY, 100, 30);
        btnLogin.addActionListener(e -> loginAction());
        getContentPane().add(btnLogin);

        JButton btnClear = new JButton("Clear");
        btnClear.setIcon(new ImageIcon("src/images/clear.png"));
        btnClear.setBounds(startX + 300, startY, 100, 30);
        btnClear.addActionListener(e -> clearAction());
        getContentPane().add(btnClear);

        JButton btnExit = new JButton("Exit");
        btnExit.setIcon(new ImageIcon("src/images/exit small.png"));
        btnExit.setBounds(startX + 430, startY, 100, 30);
        btnExit.addActionListener(e -> System.exit(0));
        getContentPane().add(btnExit);

        startY += 40;
        JButton btnForgot = new JButton("Forgot Password ?");
        btnForgot.setBounds(startX + 180, startY, 180, 30);
        btnForgot.addActionListener(e -> {
            setVisible(false);
            new ForgotPasswordPage().setVisible(true);
        });
        getContentPane().add(btnForgot);

        JButton btnSignup = new JButton("Signup");
        btnSignup.setBounds(startX + 430, startY, 100, 30);
        btnSignup.addActionListener(e -> {
            setVisible(false);
            new SignupPage().setVisible(true);
        });
        getContentPane().add(btnSignup);

        // Background Image
        ImageIcon icon = new ImageIcon("src/images/authentication-page-background.png");
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(screenW, screenH, java.awt.Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(newImg));
        background.setBounds(0, 0, screenW, screenH);
        getContentPane().add(background);
    }

    private void loginAction() {
        String email = emailTxt.getText();
        String password = new String(passwordTxt.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Email or Password cannot be empty!");
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        Connection con = null;
        try {
            con = dbUtil.getCon();
            User loggedInUser = userDao.login(con, user);
            if (loggedInUser != null) {
                if (loggedInUser.getStatus().equals("true") || loggedInUser.getEmail().equals("admin@gmail.com")) {
                    setVisible(false);
                    new HomePage(email).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Wait for Admin Approval!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Email or Password!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error!");
        } finally {
            try {
                dbUtil.closeCon(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearAction() {
        emailTxt.setText("");
        passwordTxt.setText("");
    }

    public static void main(String[] args) {
        new LoginPage().setVisible(true);
    }
}
