package com.java1234.view;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.java1234.model.User;
import com.java1234.dao.UserDao;
import com.java1234.util.DbUtil;

public class ChangeSecurityQuestionPage extends JFrame {

    private String userEmail;
    private JTextField oldSqTxt;
    private JTextField newSqTxt;
    private JTextField newAnswerTxt;
    private JPasswordField passwordTxt;
    
    private DbUtil dbUtil = new DbUtil();
    private UserDao userDao = new UserDao();

    public ChangeSecurityQuestionPage(String userEmail) {
        this.userEmail = userEmail;
        setTitle("Change Security Question");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel titleLbl = new JLabel("Change Security Question");
        titleLbl.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLbl.setBounds(20, 20, 400, 30);
        titleLbl.setIcon(new ImageIcon("src/images/change Security Question.png"));
        getContentPane().add(titleLbl);

        JButton btnClose = new JButton("");
        btnClose.setIcon(new ImageIcon("src/images/close.png"));
        btnClose.setBounds(630, 20, 30, 30);
        btnClose.addActionListener(e -> setVisible(false));
        getContentPane().add(btnClose);

        int startX = 100;
        int startY = 80;
        int gapY = 50;

        JLabel lblOldSq = new JLabel("Old Security Question");
        lblOldSq.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblOldSq.setBounds(startX, startY, 200, 30);
        getContentPane().add(lblOldSq);

        oldSqTxt = new JTextField();
        oldSqTxt.setBounds(startX + 220, startY, 250, 30);
        oldSqTxt.setEditable(false);
        getContentPane().add(oldSqTxt);

        startY += gapY;
        JLabel lblNewSq = new JLabel("New Security Question");
        lblNewSq.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewSq.setBounds(startX, startY, 200, 30);
        getContentPane().add(lblNewSq);

        newSqTxt = new JTextField();
        newSqTxt.setBounds(startX + 220, startY, 250, 30);
        getContentPane().add(newSqTxt);

        startY += gapY;
        JLabel lblNewAnswer = new JLabel("New Answer");
        lblNewAnswer.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewAnswer.setBounds(startX, startY, 200, 30);
        getContentPane().add(lblNewAnswer);

        newAnswerTxt = new JTextField();
        newAnswerTxt.setBounds(startX + 220, startY, 250, 30);
        getContentPane().add(newAnswerTxt);

        startY += gapY;
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPassword.setBounds(startX, startY, 200, 30);
        getContentPane().add(lblPassword);

        passwordTxt = new JPasswordField();
        passwordTxt.setBounds(startX + 220, startY, 250, 30);
        getContentPane().add(passwordTxt);

        startY += gapY;
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

        // Background Image
        JLabel background = new JLabel(new ImageIcon("src/images/small-page-background.png"));
        background.setBounds(0, 0, 700, 400);
        getContentPane().add(background);

        loadOldSq();
    }

    private void loadOldSq() {
        Connection con = null;
        try {
            con = dbUtil.getCon();
            User user = userDao.getSecurityQuestion(con, userEmail);
            if(user != null) {
                oldSqTxt.setText(user.getSecurityQuestion());
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }
    }

    private void updateAction() {
        String newSq = newSqTxt.getText();
        String newAns = newAnswerTxt.getText();
        String pwd = new String(passwordTxt.getPassword());

        if (newSq.isEmpty() || newAns.isEmpty() || pwd.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required!");
            return;
        }

        Connection con = null;
        try {
            con = dbUtil.getCon();
            // verify password
            String sql = "select * from user where email=? and password=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userEmail);
            pstmt.setString(2, pwd);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String updateSql = "update user set securityQuestion=?, answer=? where email=?";
                PreparedStatement pstmt2 = con.prepareStatement(updateSql);
                pstmt2.setString(1, newSq);
                pstmt2.setString(2, newAns);
                pstmt2.setString(3, userEmail);
                pstmt2.executeUpdate();
                JOptionPane.showMessageDialog(null, "Security Question Updated Successfully!");
                clearAction();
                loadOldSq();
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Password!");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }
    }

    private void clearAction() {
        newSqTxt.setText("");
        newAnswerTxt.setText("");
        passwordTxt.setText("");
    }
}
