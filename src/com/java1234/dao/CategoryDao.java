package com.java1234.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.java1234.model.Category;

public class CategoryDao {

    public void save(Connection con, Category category) throws Exception {
        String sql = "insert into category (name) values(?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, category.getName());
        pstmt.executeUpdate();
    }

    public ArrayList<Category> getAllRecords(Connection con) throws Exception {
        ArrayList<Category> list = new ArrayList<>();
        String sql = "select * from category";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Category category = new Category();
            category.setId(rs.getInt("id"));
            category.setName(rs.getString("name"));
            list.add(category);
        }
        return list;
    }

    public void delete(Connection con, String id) throws Exception {
        String sql = "delete from category where id=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, id);
        pstmt.executeUpdate();
    }
}
