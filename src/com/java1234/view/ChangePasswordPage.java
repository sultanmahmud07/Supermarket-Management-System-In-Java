package com.java1234.view;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.java1234.dao.UserDao;
import com.java1234.util.DbUtil;

public class ChangePasswordPage extends JFrame {

    private String userEmail;
    private JPasswordField oldPasswordTxt;
    private JPasswordField newPasswordTxt;
    private JPasswordField confirmPasswordTxt;
    
    private DbUtil dbUtil = new DbUtil();
    private UserDao userDao = new UserDao();

    public ChangePasswordPage(String userEmail) {
        this.userEmail = userEmail;
        setTitle("Change Password");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel titleLbl = new JLabel("Change Password");
        titleLbl.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLbl.setBounds(20, 20, 300, 30);
        titleLbl.setIcon(new ImageIcon("src/images/change Password.png"));
        getContentPane().add(titleLbl);

        JButton btnClose = new JButton("");
        btnClose.setIcon(new ImageIcon("src/images/close.png"));
        btnClose.setBounds(630, 20, 30, 30);
        btnClose.addActionListener(e -> setVisible(false));
        getContentPane().add(btnClose);

        int startX = 150;
        int startY = 100;
        int gapY = 50;

        JLabel lblOld = new JLabel("Old Password");
        lblOld.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblOld.setBounds(startX, startY, 150, 30);
        getContentPane().add(lblOld);

        oldPasswordTxt = new JPasswordField();
        oldPasswordTxt.setBounds(startX + 160, startY, 200, 30);
        getContentPane().add(oldPasswordTxt);

        startY += gapY;
        JLabel lblNew = new JLabel("New Password");
        lblNew.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNew.setBounds(startX, startY, 150, 30);
        getContentPane().add(lblNew);

        newPasswordTxt = new JPasswordField();
        newPasswordTxt.setBounds(startX + 160, startY, 200, 30);
        getContentPane().add(newPasswordTxt);

        startY += gapY;
        JLabel lblConfirm = new JLabel("Confirm Password");
        lblConfirm.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblConfirm.setBounds(startX, startY, 150, 30);
        getContentPane().add(lblConfirm);

        confirmPasswordTxt = new JPasswordField();
        confirmPasswordTxt.setBounds(startX + 160, startY, 200, 30);
        getContentPane().add(confirmPasswordTxt);

        startY += gapY;
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setIcon(new ImageIcon("src/images/save.png"));
        btnUpdate.setBounds(startX + 160, startY, 90, 30);
        btnUpdate.addActionListener(e -> updateAction());
        getContentPane().add(btnUpdate);

        JButton btnClear = new JButton("Clear");
        btnClear.setIcon(new ImageIcon("src/images/clear.png"));
        btnClear.setBounds(startX + 270, startY, 90, 30);
        btnClear.addActionListener(e -> clearAction());
        getContentPane().add(btnClear);

        // Background Image
        JLabel background = new JLabel(new ImageIcon("src/images/small-page-background.png"));
        background.setBounds(0, 0, 700, 400);
        getContentPane().add(background);
    }

    private void updateAction() {
        String oldPwd = new String(oldPasswordTxt.getPassword());
        String newPwd = new String(newPasswordTxt.getPassword());
        String confirmPwd = new String(confirmPasswordTxt.getPassword());

        if (oldPwd.isEmpty() || newPwd.isEmpty() || confirmPwd.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required!");
            return;
        }

        if (!newPwd.equals(confirmPwd)) {
            JOptionPane.showMessageDialog(null, "New Password and Confirm Password do not match!");
            return;
        }

        Connection con = null;
        try {
            con = dbUtil.getCon();
            // verify old password
            String sql = "select * from user where email=? and password=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userEmail);
            pstmt.setString(2, oldPwd);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userDao.updatePassword(con, userEmail, newPwd);
                JOptionPane.showMessageDialog(null, "Password Updated Successfully!");
                clearAction();
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Old Password!");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            try { dbUtil.closeCon(con); } catch(Exception e){}
        }
    }

    private void clearAction() {
        oldPasswordTxt.setText("");
        newPasswordTxt.setText("");
        confirmPasswordTxt.setText("");
    }
}
