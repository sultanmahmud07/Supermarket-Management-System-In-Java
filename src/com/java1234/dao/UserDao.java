package com.java1234.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.java1234.model.User;

public class UserDao {

    public void save(Connection con, User user) throws Exception {
        String sql = "insert into user (name,email,mobileNumber,address,password,securityQuestion,answer,status) values(?,?,?,?,?,?,?,'false')";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, user.getName());
        pstmt.setString(2, user.getEmail());
        pstmt.setString(3, user.getMobileNumber());
        pstmt.setString(4, user.getAddress());
        pstmt.setString(5, user.getPassword());
        pstmt.setString(6, user.getSecurityQuestion());
        pstmt.setString(7, user.getAnswer());
        pstmt.executeUpdate();
    }

    public User login(Connection con, User user) throws Exception {
        User resultUser = null;
        String sql = "select * from user where email=? and password=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, user.getEmail());
        pstmt.setString(2, user.getPassword());
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            resultUser = new User();
            resultUser.setId(rs.getInt("id"));
            resultUser.setName(rs.getString("name"));
            resultUser.setEmail(rs.getString("email"));
            resultUser.setMobileNumber(rs.getString("mobileNumber"));
            resultUser.setAddress(rs.getString("address"));
            resultUser.setPassword(rs.getString("password"));
            resultUser.setSecurityQuestion(rs.getString("securityQuestion"));
            resultUser.setAnswer(rs.getString("answer"));
            resultUser.setStatus(rs.getString("status"));
        }
        return resultUser;
    }

    public User getSecurityQuestion(Connection con, String email) throws Exception {
        User resultUser = null;
        String sql = "select * from user where email=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            resultUser = new User();
            resultUser.setSecurityQuestion(rs.getString("securityQuestion"));
            resultUser.setAnswer(rs.getString("answer"));
        }
        return resultUser;
    }

    public void updatePassword(Connection con, String email, String newPassword) throws Exception {
        String sql = "update user set password=? where email=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, newPassword);
        pstmt.setString(2, email);
        pstmt.executeUpdate();
    }

    public void changeStatus(Connection con, String email, String status) throws Exception {
        String sql = "update user set status=? where email=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, status);
        pstmt.setString(2, email);
        pstmt.executeUpdate();
    }

    public java.util.ArrayList<User> getAllUsers(Connection con, String emailSearch) throws Exception {
        java.util.ArrayList<User> list = new java.util.ArrayList<>();
        String sql = "select * from user where email like ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, "%" + emailSearch + "%");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setMobileNumber(rs.getString("mobileNumber"));
            user.setAddress(rs.getString("address"));
            user.setSecurityQuestion(rs.getString("securityQuestion"));
            user.setStatus(rs.getString("status"));
            list.add(user);
        }
        return list;
    }
}
