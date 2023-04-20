package com.chatvia.chatapp.Services;

import com.chatvia.chatapp.Entities.Message;
import com.chatvia.chatapp.Entities.User;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private Connection connection;

    public UserService() {
        this.connection = DB.getConnection();
    }

    public User findUserById(int id) throws SQLException {
        User user = null;
        String sql = "SELECT * FROM users WHERE id = ?";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFullname(resultSet.getString("fullname"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setPhone(resultSet.getString("phone"));
                user.setConnectId(resultSet.getInt("connectid"));
                user.setAvatar(resultSet.getString("avatar"));
                user.setDescribe(resultSet.getString("describe"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return user;
    }

    public User findUserByEmail(String email) throws SQLException {
        User user = null;
        String sql = "SELECT * FROM users WHERE email = ?";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFullname(resultSet.getString("fullname"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setPhone(resultSet.getString("phone"));
                user.setConnectId(resultSet.getInt("connectid"));
                user.setAvatar(resultSet.getString("avatar"));
                user.setDescribe(resultSet.getString("describe"));
            }
        } catch (SQLException e) {
//            throw new SQLException(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return user;
    }

    public void saveUser(User user) throws SQLException {
        String sql = "INSERT INTO users (fullname, email, password, phone, connectid, avatar) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, user.getFullname());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getPhone());
            statement.setInt(5, user.getConnectId());
            statement.setString(6, user.getAvatar());
            statement.executeUpdate();
        } catch (SQLException e) {
//            throw new SQLException(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public int update(String name, String email, String describe, String phone) throws SQLException {
        String sql = "UPDATE users SET fullname = ?, phone = ?, `describe` = ? WHERE email = ?";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, phone);
            statement.setString(3, describe);
            statement.setString(4, email);

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public void saveUser(String name, String email, String password) throws SQLException {
        String sql = "INSERT INTO users (fullname, email, password) VALUES (?, ?, ?)";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.executeUpdate();
        } catch (SQLException e) {
//            throw new SQLException(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public List<User> getFriends(String id) throws SQLException {
        String sql = "SELECT users.* FROM friends JOIN users ON (friends.user_id = users.id and friends.user_id != ?) or (friends.friend_id = users.id and friends.friend_id != ?) WHERE (friend_id = ? or user_id = ?) and `status` = 'accepted' and id not in (SELECT blocked_user_id FROM blocked_users WHERE user_id = ?) and id not in (SELECT user_id FROM blocked_users WHERE blocked_user_id = ?)";
        List<User> users = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            for (int i = 1; i <= 6; i++) {
                statement.setString(i, id);
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setFullname(resultSet.getString("fullname"));
                user.setPhone(resultSet.getString("phone"));
                user.setConnectId(resultSet.getInt("connectid"));
                user.setAvatar(resultSet.getString("avatar"));

                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return users;
    }

    public List<User> getUserByIds(String ids) throws SQLException {
        String sql = "SELECT * FROM users WHERE id IN (" + ids + ")";
        List<User> users = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setFullname(resultSet.getString("fullname"));
                user.setPhone(resultSet.getString("phone"));
                user.setConnectId(resultSet.getInt("connectid"));
                user.setAvatar(resultSet.getString("avatar"));

                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return users;
    }

    public int unfriend(String userId, String friendId) throws SQLException {
        String sql = "DELETE FROM friends WHERE (user_id = ? and friend_id = ?) OR (friend_id = ? AND user_id = ?);";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, userId);
            statement.setString(2, friendId);
            statement.setString(3, userId);
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

    public List<User> searchUsers(String meId, String q) throws SQLException {
        String sql = "SELECT id, email, fullname, phone, connectId, avatar,\n" +
                "(SELECT status FROM friends\n" +
                "WHERE (user_id = ? OR friend_id = ?) AND (users.id = friend_id OR user_id = users.id) LIMIT 1) AS status,\n" +
                "(SELECT user_id FROM friends\n" +
                "WHERE (user_id = ? OR friend_id = ?) AND (users.id = friend_id OR user_id = users.id) LIMIT 1) AS user_id,\n" +
                "(SELECT friend_id FROM friends\n" +
                "WHERE (user_id = ? OR friend_id = ?) AND (users.id = friend_id OR user_id = users.id) LIMIT 1) AS friend_id,\n" +
                "(SELECT user_id FROM blocked_users\n" +
                "WHERE (user_id = ? and blocked_user_id = id) or (user_id = id AND blocked_user_id = ?)) AS blockBy,\n" +
                "(SELECT blocked_user_id FROM blocked_users\n" +
                "WHERE (user_id = ? and blocked_user_id = id) or (user_id = id AND blocked_user_id = ?)) AS blocked_user_id\n" +
                "FROM users\n" +
                "WHERE id NOT IN (?) AND (fullname LIKE ? ESCAPE '!' OR email LIKE ? ESCAPE '!' OR phone LIKE ? ESCAPE '!')";
        List<User> users = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);

            for (int i = 1; i <= 11; i++) {
                statement.setString(i, meId);
            }
            statement.setString(12, "%" + q + "%");
            statement.setString(13, "%" + q + "%");
            statement.setString(14, "%" + q + "%");

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setFullname(resultSet.getString("fullname"));
                user.setPhone(resultSet.getString("phone"));
                user.setAvatar(resultSet.getString("avatar"));
                user.setStatus(resultSet.getString("status"));
                user.setBlockBy(resultSet.getString("blockBy"));
                user.setBlockedUserId(resultSet.getString("blocked_user_id"));;
                user.setReqUserId(resultSet.getString("user_id"));
                user.setReqFriendId(resultSet.getString("friend_id"));

                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return users;
    }

    public int setPassword(String password, String id) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE id = ?";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, password);
            statement.setString(2, id);

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

    public int setAvatar(String avatar, String id) throws SQLException {
        String sql = "UPDATE users SET avatar = ? WHERE id = ?";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, avatar);
            statement.setString(2, id);

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