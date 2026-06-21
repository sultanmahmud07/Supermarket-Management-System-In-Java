package com.java1234.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.java1234.model.Product;

public class ProductDao {

    public void save(Connection con, Product product) throws Exception {
        String sql = "insert into product (name,category,price) values(?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getCategory());
        pstmt.setString(3, product.getPrice());
        pstmt.executeUpdate();
    }

    public ArrayList<Product> getAllRecords(Connection con) throws Exception {
        ArrayList<Product> list = new ArrayList<>();
        String sql = "select * from product";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setCategory(rs.getString("category"));
            product.setPrice(rs.getString("price"));
            list.add(product);
        }
        return list;
    }

    public void update(Connection con, Product product) throws Exception {
        String sql = "update product set name=?, category=?, price=? where id=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getCategory());
        pstmt.setString(3, product.getPrice());
        pstmt.setInt(4, product.getId());
        pstmt.executeUpdate();
    }

    public void delete(Connection con, String id) throws Exception {
        String sql = "delete from product where id=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, id);
        pstmt.executeUpdate();
    }

    public ArrayList<Product> getByCategory(Connection con, String category) throws Exception {
        ArrayList<Product> list = new ArrayList<>();
        String sql = "select * from product where category=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, category);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setCategory(rs.getString("category"));
            product.setPrice(rs.getString("price"));
            list.add(product);
        }
        return list;
    }
}
