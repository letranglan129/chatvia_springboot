package com.chatvia.chatapp.Services;

import com.chatvia.chatapp.Entities.File;
import com.chatvia.chatapp.Entities.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    private Connection connection;

    public FileService() {
        this.connection = DB.getConnection();
    }

    public void insertFiles(String messageId, List<File> files) throws SQLException {
        String sql = "INSERT INTO file_messages (`message_id`, `href`, `name`, `size`) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < files.size(); i++) {
                statement.setString(1, messageId);
                statement.setString(2, files.get(i).getHref());
                statement.setString(3, files.get(i).getName());
                statement.setString(4, files.get(i).getSize());
                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public void insertFile(String messageId, File file) throws SQLException {
        String sql = "INSERT INTO file_messages (`message_id`, `href`, `name`, `size`) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, messageId);
            statement.setString(2, file.getHref());
            statement.setString(3, file.getName());
            statement.setString(4, file.getSize());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public List<File> findByMessageId(String messageId) throws SQLException {
        String sql = "SELECT * FROM file_messages WHERE message_id = ?";

        List<File> files = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, messageId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                File file = new File();

                file.setHref(resultSet.getString("href"));
                file.setName(resultSet.getString("name"));
                file.setSize(resultSet.getString("size"));

                files.add(file);
            }
            return files;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement != null) {
                statement.close();
            }
        }

        return files;

    }

}
