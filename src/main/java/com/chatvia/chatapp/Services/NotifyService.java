package com.chatvia.chatapp.Services;

import com.chatvia.chatapp.Entities.Notification;
import com.chatvia.chatapp.Entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotifyService {
    private Connection connection;

    public NotifyService() {
        this.connection = DB.getConnection();
    }

    public String insert(String userId, String type, String payload, String fromId) throws SQLException {
        String sql = "INSERT IGNORE INTO notifications (user_id, `type`, payload, from_id) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userId);
            statement.setString(2, type);
            statement.setString(3, payload);
            statement.setString(4, fromId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting group failed, no rows affected.");
            }
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                throw new SQLException("Inserting group failed, no ID obtained.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return null;
    }

    public String insert(String userId, String type, String fromId) throws SQLException {
        String sql = "INSERT IGNORE INTO notifications (user_id, `type`, from_id) VALUES (?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userId);
            statement.setString(2, type);
            statement.setString(3, fromId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting group failed, no rows affected.");
            }
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                throw new SQLException("Inserting group failed, no ID obtained.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return null;
    }

    public List<Notification> getNotify(String userId, boolean isReaded) throws SQLException {
        String sql = "SELECT notifications.id, notifications.user_id, type, payload, created_at, seen_at, fullname, from_id, avatar\n" +
                "FROM notifications\n" +
                "INNER JOIN users ON notifications.from_id = users.id\n" +
                "WHERE user_id = ? AND is_readed = ?\n" +
                "ORDER BY created_at DESC;";
        List<Notification> notifications = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);

            statement.setString(1, userId);
            statement.setString(2, isReaded ? "1" : "0");

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Notification notification = new Notification();
                notification.setId(resultSet.getString("id"));
                notification.setUserId(resultSet.getString("user_id"));
                notification.setType(resultSet.getString("type"));
                notification.setPayload(resultSet.getString("payload"));
                notification.setCreatedAt(resultSet.getString("created_at"));
                notification.setSeenAt(resultSet.getString("seen_at"));
                notification.setFullname(resultSet.getString("fullname"));
                notification.setFromId(resultSet.getString("from_id"));
                notification.setAvatar(resultSet.getString("avatar"));

                notifications.add(notification);
            }
            return notifications;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return notifications;
    }

    public void updateContent(String notifyId, String type, String payload, String fromId) throws SQLException {
        String sql = "UPDATE notifications\n" +
                "SET `type` = ?, payload = ?, from_id = ?\n" +
                "WHERE id = ?;";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, type);
            statement.setString(2, payload);
            statement.setString(3, fromId);
            statement.setString(4, notifyId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public void updateReaded(String notifyIds) throws SQLException {
        String sql = "UPDATE notifications\n" +
                "SET is_readed = 1\n" +
                "WHERE id IN (" + notifyIds + ")";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public int deleteNotifyWithFromId(String meId, String friendId) throws SQLException {
        String sql = "DELETE FROM notifications WHERE (user_id = ? AND from_id = ?) OR (from_id = ? AND user_id = ?) and type = 'friend_request'";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, meId);
            statement.setString(2, friendId);
            statement.setString(3, meId);
            statement.setString(4, friendId);

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return 0;
    }

    public Notification getNotifyByFromIdAndUserId(String userId, String fromId) throws SQLException {
        String sql = "SELECT notifications.id, notifications.user_id, type, payload, created_at, seen_at, fullname, from_id, avatar\n" +
                "FROM notifications\n" +
                "INNER JOIN users ON notifications.from_id = users.id\n" +
                "WHERE user_id = ? AND from_id = ?\n" +
                "ORDER BY created_at DESC\n" +
                "LIMIT 1;";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);

            statement.setString(1, userId);
            statement.setString(2, fromId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Notification notification = new Notification();
                notification.setId(resultSet.getString("id"));
                notification.setUserId(resultSet.getString("user_id"));
                notification.setType(resultSet.getString("type"));
                notification.setPayload(resultSet.getString("payload"));
                notification.setCreatedAt(resultSet.getString("created_at"));
                notification.setSeenAt(resultSet.getString("seen_at"));
                notification.setFullname(resultSet.getString("fullname"));
                notification.setFromId(resultSet.getString("from_id"));
                notification.setAvatar(resultSet.getString("avatar"));
                return notification;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return null;
    }

}
