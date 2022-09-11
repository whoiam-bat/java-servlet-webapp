package com.my.store.dao;

import com.my.store.JdbcConfig;
import com.my.store.model.Book;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class BookStockDao {
    private final Connection connection;

    public BookStockDao() {
        connection = JdbcConfig.getInstance().getConnection();
    }


    public List<Book> listBooks() {
        List<Book> bookList = new ArrayList<>();

        try(
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM book;");
                ) {

            while(rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                float price = rs.getFloat("price");
                int amount = rs.getInt("amount");
                String imagePATH = rs.getString("image");

                Book temp = new Book(id, title, author, genre, price, amount, imagePATH);

                bookList.add(temp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public Book getBook(String bookId) {
        Book res = null;
        try(
                PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM book WHERE id=?;");
        ) {
            pstmt.setInt(1, Integer.parseInt(bookId));

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                res = new Book(rs.getInt("id"),
                        rs.getString("title"), rs.getString("author"),
                        rs.getString("genre"), rs.getFloat("price"),
                        rs.getInt("amount"), rs.getString("image"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    public Book searchBookByTitle(String nameBook) {
        Book res = null;
        try(
                PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM book WHERE title=?;");
        ) {
            pstmt.setString(1, nameBook);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                res = new Book(rs.getInt("id"),
                        rs.getString("title"), rs.getString("author"),
                        rs.getString("genre"), rs.getFloat("price"),
                        rs.getInt("amount"), rs.getString("image"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    public void addBook(Book book) {
        try (
                PreparedStatement pstmt = connection.prepareStatement(
                        "INSERT INTO book (title, author, genre, price, amount, image) " +
                                "VALUE(?, ?, ?, ?, ?, ?)");
                ) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getGenre());
            pstmt.setFloat(4, book.getPrice());
            pstmt.setInt(5, book.getAmount());
            pstmt.setString(6, book.getImagePATH());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(String id) {
        try (
             PreparedStatement pstmt = connection.prepareStatement("DELETE FROM book WHERE id=?;");) {

            pstmt.setInt(1, Integer.parseInt(id));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(Book book) {
        try(
                PreparedStatement pstmt = connection.prepareStatement(
                        "UPDATE book SET title=?, author=?, genre=?, price=?, amount=?, image=?" +
                                " WHERE id=?;");) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getGenre());
            pstmt.setFloat(4, book.getPrice());
            pstmt.setInt(5, book.getAmount());
            pstmt.setString(6, book.getImagePATH());
            pstmt.setInt(7, book.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
