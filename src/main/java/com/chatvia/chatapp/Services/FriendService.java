package com.chatvia.chatapp.Services;


import java.sql.*;

public class FriendService {
    private Connection connection;

    public FriendService() {
        this.connection = DB.getConnection();
    }

    public String insertAddFriendRequest(String userId, String friendId, String status) throws SQLException {
        String sql = "INSERT IGNORE INTO friends (user_id, `friend_id`, status) VALUES (?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userId);
            statement.setString(2, friendId);
            statement.setString(3, status);
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

    public void updateStatus(String userId, String friendId, String status) throws SQLException {
        String sql = "UPDATE friends SET status = ? WHERE user_id = ? AND friend_id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, status);
            statement.setString(2, userId);
            statement.setString(3, friendId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public int deleteFriendShip(String meId, String friendId) throws SQLException {
        String sql = "DELETE FROM friends WHERE (user_id = ? AND friend_id = ?) OR (friend_id = ? AND user_id = ?)";
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

    public int blockUser(String meId, String friendId) throws SQLException {
        String sql = "INSERT INTO blocked_users (user_id, blocked_user_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE blocked_user_id = blocked_user_id;";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, meId);
            statement.setString(2, friendId);

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

    public int unblockUser(String meId, String friendId) throws SQLException {
        String sql = "DELETE FROM blocked_users WHERE user_id = ? AND blocked_user_id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, meId);
            statement.setString(2, friendId);

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
}