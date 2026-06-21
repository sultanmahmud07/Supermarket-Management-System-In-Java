package com.java1234.view;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import com.java1234.dao.UserDao;
import com.java1234.model.User;
import com.java1234.util.DbUtil;

public class ForgotPasswordPage extends JFrame {

    private JTextField emailTxt;
    private JTextField sqTxt;
    private JTextField answerTxt;
    private JPasswordField newPasswordTxt;
    private String dbAnswer = null;

    private DbUtil dbUtil = new DbUtil();
    private UserDao userDao = new UserDao();

    public ForgotPasswordPage() {
        setTitle("Supermarket Management System - Forgot Password");
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

        JLabel pageTitleLbl = new JLabel("Forgot Password ?");
        pageTitleLbl.setForeground(new Color(30, 41, 59)); // Premium Slate Gray
        pageTitleLbl.setFont(new Font("Tahoma", Font.BOLD, 32));
        pageTitleLbl.setBounds(0, 210, screenW, 40);
        pageTitleLbl.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(pageTitleLbl);

        int startX = (screenW - 570) / 2;
        int startY = 290;
        int gapY = 40;

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblEmail.setBounds(startX, startY, 200, 30);
        getContentPane().add(lblEmail);

        emailTxt = new JTextField();
        emailTxt.setBounds(startX + 220, startY, 350, 30);
        getContentPane().add(emailTxt);

        JButton btnSearch = new JButton("Search");
        btnSearch.setIcon(new ImageIcon("src/images/search.png"));
        btnSearch.setBounds(startX + 590, startY, 100, 30);
        btnSearch.addActionListener(e -> searchAction());
        getContentPane().add(btnSearch);

        startY += gapY;
        JLabel lblSq = new JLabel("Your Security Question");
        lblSq.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblSq.setBounds(startX, startY, 200, 30);
        getContentPane().add(lblSq);

        sqTxt = new JTextField();
        sqTxt.setBounds(startX + 220, startY, 350, 30);
        sqTxt.setEditable(false);
        getContentPane().add(sqTxt);

        startY += gapY;
        JLabel lblAnswer = new JLabel("Your Answer");
        lblAnswer.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblAnswer.setBounds(startX, startY, 200, 30);
        getContentPane().add(lblAnswer);

        answerTxt = new JTextField();
        answerTxt.setBounds(startX + 220, startY, 350, 30);
        getContentPane().add(answerTxt);

        startY += gapY;
        JLabel lblNewPassword = new JLabel("Enter New Password");
        lblNewPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewPassword.setBounds(startX, startY, 200, 30);
        getContentPane().add(lblNewPassword);

        newPasswordTxt = new JPasswordField();
        newPasswordTxt.setBounds(startX + 220, startY, 350, 30);
        getContentPane().add(newPasswordTxt);

        // Buttons
        startY += gapY + 20;
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setIcon(new ImageIcon("src/images/save.png"));
        btnUpdate.setBounds(startX + 220, startY, 100, 30);
        btnUpdate.addActionListener(e -> updateAction());
        getContentPane().add(btnUpdate);

        JButton btnClear = new JButton("Clear");
        btnClear.setIcon(new ImageIcon("src/images/clear.png"));
        btnClear.setBounds(startX + 340, startY, 100, 30);
        btnClear.addActionListener(e -> clearAction());
        getContentPane().add(btnClear);

        JButton btnExit = new JButton("Exit");
        btnExit.setIcon(new ImageIcon("src/images/exit small.png"));
        btnExit.setBounds(startX + 470, startY, 100, 30);
        btnExit.addActionListener(e -> System.exit(0));
        getContentPane().add(btnExit);

        startY += 40;
        JButton btnSignup = new JButton("Signup");
        btnSignup.setBounds(startX + 220, startY, 100, 30);
        btnSignup.addActionListener(e -> {
            setVisible(false);
            new SignupPage().setVisible(true);
        });
        getContentPane().add(btnSignup);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(startX + 470, startY, 100, 30);
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

    private void searchAction() {
        String email = emailTxt.getText();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter email!");
            return;
        }

        Connection con = null;
        try {
            con = dbUtil.getCon();
            User user = userDao.getSecurityQuestion(con, email);
            if (user != null) {
                sqTxt.setText(user.getSecurityQuestion());
                dbAnswer = user.getAnswer();
            } else {
                JOptionPane.showMessageDialog(null, "Email not found!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                dbUtil.closeCon(con);
            } catch (Exception e) {
            }
        }
    }

    private void updateAction() {
        String email = emailTxt.getText();
        String answer = answerTxt.getText();
        String newPassword = new String(newPasswordTxt.getPassword());

        if (email.isEmpty() || answer.isEmpty() || newPassword.isEmpty() || sqTxt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required!");
            return;
        }

        if (!answer.equals(dbAnswer)) {
            JOptionPane.showMessageDialog(null, "Incorrect Answer!");
            return;
        }

        Connection con = null;
        try {
            con = dbUtil.getCon();
            userDao.updatePassword(con, email, newPassword);
            JOptionPane.showMessageDialog(null, "Password Updated Successfully!");
            clearAction();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                dbUtil.closeCon(con);
            } catch (Exception e) {
            }
        }
    }

    private void clearAction() {
        emailTxt.setText("");
        sqTxt.setText("");
        answerTxt.setText("");
        newPasswordTxt.setText("");
        dbAnswer = null;
    }

    public static void main(String[] args) {
        new ForgotPasswordPage().setVisible(true);
    }
}
