package com.java1234.view;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import com.java1234.dao.UserDao;
import com.java1234.model.User;
import com.java1234.util.DbUtil;

public class SignupPage extends JFrame {

    private JTextField nameTxt;
    private JTextField emailTxt;
    private JTextField mobileTxt;
    private JTextField addressTxt;
    private JPasswordField passwordTxt;
    private JTextField sqTxt;
    private JTextField answerTxt;

    private DbUtil dbUtil = new DbUtil();
    private UserDao userDao = new UserDao();

    public SignupPage() {
        setTitle("Supermarket Management System - Signup");
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

        JLabel pageTitleLbl = new JLabel("Signup");
        pageTitleLbl.setForeground(new Color(30, 41, 59)); // Premium Slate Gray
        pageTitleLbl.setFont(new Font("Tahoma", Font.BOLD, 32));
        pageTitleLbl.setBounds(0, 210, screenW, 40);
        pageTitleLbl.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(pageTitleLbl);

        // Form Labels and Fields
        int startX = (screenW - 530) / 2;
        int startY = 290;
        int gapY = 40;

        JLabel lblName = new JLabel("Name");
        lblName.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblName.setBounds(startX, startY, 150, 30);
        getContentPane().add(lblName);

        nameTxt = new JTextField();
        nameTxt.setBounds(startX + 180, startY, 350, 30);
        getContentPane().add(nameTxt);

        startY += gapY;
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblEmail.setBounds(startX, startY, 150, 30);
        getContentPane().add(lblEmail);

        emailTxt = new JTextField();
        emailTxt.setBounds(startX + 180, startY, 350, 30);
        getContentPane().add(emailTxt);

        startY += gapY;
        JLabel lblMobile = new JLabel("Mobile Number");
        lblMobile.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblMobile.setBounds(startX, startY, 150, 30);
        getContentPane().add(lblMobile);

        mobileTxt = new JTextField();
        mobileTxt.setBounds(startX + 180, startY, 350, 30);
        getContentPane().add(mobileTxt);

        startY += gapY;
        JLabel lblAddress = new JLabel("Address");
        lblAddress.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblAddress.setBounds(startX, startY, 150, 30);
        getContentPane().add(lblAddress);

        addressTxt = new JTextField();
        addressTxt.setBounds(startX + 180, startY, 350, 30);
        getContentPane().add(addressTxt);

        startY += gapY;
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPassword.setBounds(startX, startY, 150, 30);
        getContentPane().add(lblPassword);

        passwordTxt = new JPasswordField();
        passwordTxt.setBounds(startX + 180, startY, 350, 30);
        getContentPane().add(passwordTxt);

        startY += gapY;
        JLabel lblSq = new JLabel("Security Question");
        lblSq.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblSq.setBounds(startX, startY, 150, 30);
        getContentPane().add(lblSq);

        sqTxt = new JTextField();
        sqTxt.setBounds(startX + 180, startY, 350, 30);
        getContentPane().add(sqTxt);

        startY += gapY;
        JLabel lblAnswer = new JLabel("Answer");
        lblAnswer.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblAnswer.setBounds(startX, startY, 150, 30);
        getContentPane().add(lblAnswer);

        answerTxt = new JTextField();
        answerTxt.setBounds(startX + 180, startY, 350, 30);
        getContentPane().add(answerTxt);

        // Buttons
        startY += gapY + 10;
        JButton btnSave = new JButton("Save");
        btnSave.setIcon(new ImageIcon("src/images/save.png"));
        btnSave.setBounds(startX + 180, startY, 100, 30);
        btnSave.addActionListener(e -> saveAction());
        getContentPane().add(btnSave);

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

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(startX + 430, startY, 100, 30);
        btnLogin.addActionListener(e -> {
            setVisible(false);
            new LoginPage().setVisible(true);
        });
        getContentPane().add(btnLogin);

        // Background Image
        ImageIcon icon = new ImageIcon("src/images/authentication-page-background.png");
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(screenW, screenH, java.awt.Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(newImg));
        background.setBounds(0, 0, screenW, screenH);
        getContentPane().add(background);
    }

    private void saveAction() {
        String name = nameTxt.getText();
        String email = emailTxt.getText();
        String mobile = mobileTxt.getText();
        String address = addressTxt.getText();
        String password = new String(passwordTxt.getPassword());
        String sq = sqTxt.getText();
        String answer = answerTxt.getText();

        if (name.isEmpty() || email.isEmpty() || mobile.isEmpty() || address.isEmpty() || password.isEmpty()
                || sq.isEmpty() || answer.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all fields!");
            return;
        }

        // Email Validation
        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            JOptionPane.showMessageDialog(null, "Please enter a valid email address!");
            return;
        }

        // Mobile Number Validation
        if (!mobile.matches("^\\d{10,15}$")) {
            JOptionPane.showMessageDialog(null, "Mobile number must be digits only and 10-15 digits long!");
            return;
        }

        // Password Validation
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(null, "Password must be at least 6 characters long!");
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setMobileNumber(mobile);
        user.setAddress(address);
        user.setPassword(password);
        user.setSecurityQuestion(sq);
        user.setAnswer(answer);

        Connection con = null;
        try {
            con = dbUtil.getCon();
            userDao.save(con, user);
            JOptionPane.showMessageDialog(null, "Registered Successfully! Wait for Admin Approval.");
            clearAction();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Registration Failed or Email already exists.");
        } finally {
            try {
                dbUtil.closeCon(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearAction() {
        nameTxt.setText("");
        emailTxt.setText("");
        mobileTxt.setText("");
        addressTxt.setText("");
        passwordTxt.setText("");
        sqTxt.setText("");
        answerTxt.setText("");
    }

    public static void main(String[] args) {
        new SignupPage().setVisible(true);
    }
}
