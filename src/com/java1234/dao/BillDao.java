package com.java1234.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.java1234.model.Bill;

public class BillDao {

    public void save(Connection con, Bill bill) throws Exception {
        String sql = "insert into bill (uuid,name,email,mobileNumber,date,total,createdBy) values(?,?,?,?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, bill.getUuid());
        pstmt.setString(2, bill.getName());
        pstmt.setString(3, bill.getEmail());
        pstmt.setString(4, bill.getMobileNumber());
        pstmt.setString(5, bill.getDate());
        pstmt.setString(6, bill.getTotal());
        pstmt.setString(7, bill.getCreatedBy());
        pstmt.executeUpdate();
    }

    public ArrayList<Bill> getAllRecordsByInc(Connection con, String date) throws Exception {
        ArrayList<Bill> list = new ArrayList<>();
        String sql = "select * from bill where date like ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, "%" + date + "%");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Bill bill = new Bill();
            bill.setId(rs.getInt("id"));
            bill.setUuid(rs.getString("uuid"));
            bill.setName(rs.getString("name"));
            bill.setEmail(rs.getString("email"));
            bill.setMobileNumber(rs.getString("mobileNumber"));
            bill.setDate(rs.getString("date"));
            bill.setTotal(rs.getString("total"));
            bill.setCreatedBy(rs.getString("createdBy"));
            list.add(bill);
        }
        return list;
    }
}
