package com.chatvia.chatapp.Services;

import com.chatvia.chatapp.Entities.Message;
import com.chatvia.chatapp.Entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageService {
    private Connection connection;

    public MessageService() {
        this.connection = DB.getConnection();
    }

    public List<Message> getMessageText(String groupId, String senderId) throws SQLException {
        String sql = "SELECT users.*, messages.*, messages.id as message_id, (SELECT viewed_at FROM viewed_messages WHERE viewed_messages.message_id = messages.id LIMIT 1) as viewed_at\n" +
                "FROM messages\n" +
                "INNER JOIN users ON messages.sender_id = users.id\n" +
                "WHERE group_id = ? and format = 'text'\n" +
                "AND messages.id NOT IN (SELECT message_id FROM deleted_messages WHERE user_id = ?)";

        List<Message> messages = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, groupId);
            statement.setString(2, senderId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Message message = new Message();
                message.setAvatar(resultSet.getString("avatar"));
                message.setFormat(resultSet.getString("format"));
                message.setFullname(resultSet.getString("fullname"));
                message.setGroupId(resultSet.getString("group_id"));
                message.setMessage(resultSet.getString("message"));
                message.setId(resultSet.getString("message_id"));
                message.setSenderId(resultSet.getString("sender_id"));
                message.setSentAt(resultSet.getString("sent_at"));
                message.setViewedAt(resultSet.getString("viewed_at"));

                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement != null) {
                statement.close();
            }
        }

        return messages;
    }

    public List<Message> getMessageMedia(String groupId, String senderId) throws SQLException {
        String sql = "SELECT users.*, messages.*, messages.id as message_id, (SELECT viewed_at FROM viewed_messages WHERE viewed_messages.message_id = messages.id LIMIT 1) as viewed_at, href, name, size\n" +
                "FROM messages\n" +
                "INNER JOIN users ON messages.sender_id = users.id\n" +
                "INNER JOIN file_messages ON messages.id = file_messages.message_id\n" +
                "WHERE group_id = ? AND format != 'text' AND messages.id NOT IN (SELECT message_id FROM deleted_messages WHERE user_id = ?)";

        List<Message> messages = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, groupId);
            statement.setString(2, senderId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Message message = new Message();

                message.setAvatar(resultSet.getString("avatar"));
                message.setFormat(resultSet.getString("format"));
                message.setFullname(resultSet.getString("fullname"));
                message.setGroupId(resultSet.getString("group_id"));
                message.setMessage(resultSet.getString("message"));
                message.setId(resultSet.getString("message_id"));
                message.setSenderId(resultSet.getString("sender_id"));
                message.setSentAt(resultSet.getString("sent_at"));
                message.setViewedAt(resultSet.getString("viewed_at"));
                message.setHref(resultSet.getString("href"));
                message.setName(resultSet.getString("name"));
                message.setSize(resultSet.getString("size"));

                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement != null) {
                statement.close();
            }
        }

        return messages;
    }

    public List<Message> getFile(String groupId, String senderId) throws SQLException {
        String sql = "SELECT file_messages.*, format, sent_at, group_id, sender_id\n" +
                "FROM file_messages\n" +
                "INNER JOIN messages ON file_messages.message_id = messages.id\n" +
                "WHERE group_id = ?\n" +
                "AND message_id NOT IN (SELECT message_id FROM deleted_messages WHERE user_id = ?)";

        List<Message> messages = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, groupId);
            statement.setString(2, senderId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Message message = new Message();

                message.setId(resultSet.getString("message_id"));
                message.setFormat(resultSet.getString("format"));
                message.setGroupId(resultSet.getString("group_id"));
                message.setHref(resultSet.getString("href"));
                message.setName(resultSet.getString("name"));
                message.setSenderId(resultSet.getString("sender_id"));
                message.setSentAt(resultSet.getString("sent_at"));
                message.setSize(resultSet.getString("size"));

                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement != null) {
                statement.close();
            }
        }

        return messages;
    }

    public String insertMessage(String groupId, String senderId, String message) throws SQLException {
        String sql = "INSERT INTO `messages` (group_id, sender_id, message) VALUES (?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, groupId);
            statement.setString(2, senderId);
            statement.setString(3, message);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting group failed, no rows affected.");
            }
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                String newGroupId = rs.getString(1);
                return newGroupId;
            } else {
                throw new SQLException("Inserting group failed, no ID obtained.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            if (statement != null) {
                statement.close();
            }
        }

        return null;
    }

    public String insertMessage(String groupId, String senderId, String message, String format) throws SQLException {
        String sql = "INSERT INTO `messages` (group_id, sender_id, message, format) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, groupId);
            statement.setString(2, senderId);
            statement.setString(3, message);
            statement.setString(4, format);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting group failed, no rows affected.");
            }
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                String newGroupId = rs.getString(1);
                return newGroupId;
            } else {
                throw new SQLException("Inserting group failed, no ID obtained.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            if (statement != null) {
                statement.close();
            }
        }

        return null;
    }

    public List<String> getMessageIdsInGroup(String groupId) throws SQLException {
        String sql = "SELECT id\n" +
                "FROM messages\n" +
                "WHERE group_id = ?";

        List<String> messageIds = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, groupId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                messageIds.add(resultSet.getString("id"));
            }
            return messageIds;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement != null) {
                statement.close();
            }
        }

        return messageIds;
    }

    public void updateBatchMessageSeen(String userId, List<String> messageIds) throws SQLException {
        String sql = "INSERT IGNORE INTO viewed_messages(message_id, user_id) VALUES (?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < messageIds.size(); i++) {
                statement.setString(1, messageIds.get(i));
                statement.setString(2, userId);
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

    public List<Message> getMessageJustSeen(String groupId, String messageIdsStr) throws SQLException {

        String sql = "SELECT users.*, messages.*, messages.id as message_id,\n" +
                "(SELECT viewed_at FROM viewed_messages WHERE viewed_messages.message_id = messages.id LIMIT 1) as viewed_at\n" +
                "FROM messages\n" +
                "INNER JOIN users ON messages.sender_id = users.id\n" +
                "WHERE group_id = ? and messages.id in ("+ messageIdsStr +")";

        List<Message> messages = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, groupId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Message message = new Message();
                message.setId(resultSet.getString("message_id"));
                message.setViewedAt(resultSet.getString("viewed_at"));
                messages.add(message);
            }

            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement != null) {
                statement.close();
            }
        }

        return messages;
    }

    public Message findById(String id) throws SQLException {
        Message message = null;
        String sql = "SELECT * FROM messages WHERE id = ?";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                message = new Message();
                message.setId(resultSet.getString("id"));
                message.setSenderId(resultSet.getString("sender_id"));
                message.setMessage(resultSet.getString("message"));
                message.setSentAt(resultSet.getString("sent_at"));
                message.setGroupId(resultSet.getString("group_id"));
                message.setFormat(resultSet.getString("format"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement != null) {
                statement.close();
            }
        }

        return message;
    }

    public void deleteMessage(String messageId, String userId)  throws SQLException {
        Message message = null;
        String sql = "INSERT IGNORE INTO deleted_messages(message_id, user_id) VALUES (?, ?)";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, messageId);
            statement.setString(2, userId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public void deleteMessage(String messageId, List<String> userIds)  throws SQLException {
        Message message = null;
        String sql = "INSERT IGNORE INTO deleted_messages(message_id, user_id) VALUES (?, ?)";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < userIds.size(); i++) {
                statement.setString(1, messageId);
                statement.setString(2, userIds.get(i));
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public void deleteMessage(List<String> messageIds, String userId)  throws SQLException {
        Message message = null;
        String sql = "INSERT IGNORE INTO deleted_messages(message_id, user_id) VALUES (?, ?)";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < messageIds.size(); i++) {
                statement.setString(1, messageIds.get(i));
                statement.setString(2, userId);
                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

}
