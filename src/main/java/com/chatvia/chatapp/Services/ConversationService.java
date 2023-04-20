package com.chatvia.chatapp.Services;

import com.chatvia.chatapp.Entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConversationService {
    private Connection connection;

    public ConversationService() {
        this.connection = DB.getConnection();
    }

    public List<Conversation> getPrivateConversation(String id) throws SQLException {
        String sql = "SELECT group_members.group_id, `users`.id, receiver.avatar, `users`.fullname, \n" +
                "(SELECT message FROM messages WHERE group_id = group_members.group_id and messages.id not in \n" +
                "(SELECT message_id FROM deleted_messages WHERE user_id = ?) ORDER BY sent_at DESC \n" +
                "LIMIT 1) as `message`, m.fullname as sender, m.sender_id, m.sent_at, receiver.fullname as receiverName, \n" +
                "receiver.id as receiverId, (SELECT COUNT(*) FROM messages WHERE group_id = group_members.group_id AND messages.id NOT IN \n" +
                "(SELECT message_id FROM deleted_messages WHERE user_id = ? )) AS countMessage, \n" +
                "receiver.id in (SELECT blocked_user_id FROM blocked_users WHERE user_id = ? UNION SELECT user_id FROM \n" +
                "blocked_users WHERE blocked_user_id = ?) as isBlocked \n" +
                "FROM `group_members` \n" +
                "INNER JOIN `groups` ON `groups`.id = `group_members`.group_id \n" +
                "INNER JOIN `users` ON `users`.id = `group_members`.user_id \n" +
                "INNER JOIN `messages` ON `messages`.group_id = `groups`.id \n" +
                "LEFT OUTER JOIN (SELECT `messages`.*, `users`.fullname FROM `messages`, `users` WHERE `sender_id` = `users`.id ) AS m ON \n" +
                "m.id = `last_message` \n" +
                "LEFT OUTER JOIN (SELECT `u`.*, `gm`.group_id as `groupID` FROM `groups` `g`, `group_members` `gm`, `users` `u` \n" +
                "WHERE `gm`.group_id = `g`.id and `gm`.group_id = `gm`.group_id and `type` = 'dou' and `user_id` != ? \n" +
                "AND `u`.id = `gm`.user_id ) AS receiver ON receiver.groupID = `group_members`.group_id \n" +
                "WHERE `group_members`.user_id = ? and `type` = 'dou' \n" +
                "GROUP BY `group_members`.group_id, `users`.id, `users`.fullname, m.fullname, m.sender_id, m.sent_at, \n" +
                "receiver.fullname, receiver.id, receiver.avatar \n" +
                "ORDER BY `sent_at` DESC";

        List<Conversation> conversations = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            for (int i = 1; i <= 6; i++) {
                statement.setString(i, id);
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Conversation conversation = new Conversation();
                conversation.setAvatar(resultSet.getString("avatar"));
                conversation.setFullname(resultSet.getString("fullname"));
                conversation.setCountMessage(resultSet.getString("countMessage"));
                conversation.setGroupId(resultSet.getString("group_id"));
                conversation.setId(resultSet.getString("id"));
                conversation.setIsBlocked(resultSet.getString("isBlocked"));
                conversation.setMessage(resultSet.getString("message"));
                conversation.setReceiverId(resultSet.getString("receiverId"));
                conversation.setReceiverName(resultSet.getString("receiverName"));
                conversation.setSender(resultSet.getString("sender"));
                conversation.setSenderId(resultSet.getString("sender_id"));
                conversation.setSentAt(resultSet.getString("sent_at"));

                conversations.add(conversation);
            }
            return conversations;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return conversations;
    }

    public List<Conversation> getMultiConversation(String id) throws SQLException {
        String sql = "SELECT groups.id AS groupId, groups.name AS groupName, \n" +
                "    (SELECT message FROM messages \n" +
                "    WHERE group_id = group_members.group_id \n" +
                "    AND messages.id NOT IN (SELECT message_id FROM deleted_messages WHERE user_id = ?) \n" +
                "    ORDER BY sent_at DESC  \n" +
                "    LIMIT 1) AS `message`, \n" +
                "    type, `desc`, groups.avatar AS groupAvatar, \n" +
                "    m.sent_at, m.sender_id, m.fullname, users.avatar AS userAvatar, \n" +
                "    (SELECT COUNT(*) FROM messages WHERE group_id = group_members.group_id \n" +
                "    AND messages.id NOT IN (SELECT message_id FROM deleted_messages WHERE user_id = ?)) AS countMessage\n" +
                "FROM group_members\n" +
                "INNER JOIN `groups` ON groups.id = group_members.group_id\n" +
                "INNER JOIN users ON users.id = group_members.user_id\n" +
                "INNER JOIN messages ON messages.group_id = groups.id\n" +
                "LEFT OUTER JOIN \n" +
                "    (SELECT messages.*, users.fullname \n" +
                "    FROM messages, users \n" +
                "    WHERE sender_id = users.id ) AS m \n" +
                "ON m.id = last_message\n" +
                "WHERE user_id = ? AND `groups`.id = group_members.group_id AND type = 'multi'\n" +
                "GROUP BY groupId, groupName, message, type, `desc`, groupAvatar, m.sent_at, m.sender_id, m.fullname, userAvatar\n" +
                "ORDER BY sent_at DESC";

        List<Conversation> conversations = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            for (int i = 1; i <= 3; i++) {
                statement.setString(i, id);
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Conversation conversation = new Conversation();
                conversation.setCountMessage(resultSet.getString("countMessage"));
                conversation.setDesc(resultSet.getString("desc"));
                conversation.setFullname(resultSet.getString("fullname"));
                conversation.setGroupAvatar(resultSet.getString("groupAvatar"));
                conversation.setGroupMultiId(resultSet.getString("groupId"));
                conversation.setGroupName(resultSet.getString("groupName"));
                conversation.setMessage(resultSet.getString("message"));
                conversation.setSenderId(resultSet.getString("sender_id"));
                conversation.setSentAt(resultSet.getString("sent_at"));
                conversation.setType(resultSet.getString("type"));

                conversations.add(conversation);
            }
            return conversations;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return conversations;
    }

    public List<Message> getUnreadMessage(String id) throws SQLException {
        String sql = "SELECT messages.*\n" +
                "FROM messages\n" +
                "INNER JOIN group_members ON messages.group_id = group_members.group_id\n" +
                "WHERE group_members.user_id = ? AND sender_id != ? \n" +
                "AND NOT EXISTS (\n" +
                "    SELECT * FROM viewed_messages \n" +
                "    WHERE message_id = messages.id AND user_id = ?\n" +
                ")";

        List<Message> messages = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            for (int i = 1; i <= 3; i++) {
                statement.setString(i, id);
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Message message = new Message();
                message.setId(resultSet.getString("id"));
                message.setSenderId(resultSet.getString("sender_id"));
                message.setMessage(resultSet.getString("message"));
                message.setSentAt(resultSet.getString("sent_at"));
                message.setGroupId(resultSet.getString("group_id"));
                message.setFormat(resultSet.getString("format"));

                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return messages;
    }

    public String checkDouGroupExist(Integer senderId, Integer receiverId) throws SQLException {
        String sql = "SELECT group_members.group_id\n" +
                "FROM group_members\n" +
                "JOIN `groups` ON groups.id = group_members.group_id\n" +
                "WHERE user_id IN (?, ?) AND type = 'dou'\n" +
                "GROUP BY group_members.group_id\n" +
                "HAVING COUNT(DISTINCT user_id) = 2\n" +
                "ORDER BY group_id DESC\n" +
                "LIMIT 1";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, senderId);
            statement.setInt(2, receiverId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getString("group_id") != null) {
                    return resultSet.getString("group_id");
                }
                return null;
            }
        } catch (SQLException e) {
//            throw new SQLException(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return null;
    }

    public String createPrivateConversation() throws SQLException {
        String sql = "INSERT INTO `groups` (name, type) VALUES ('', 'dou')";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return null;
    }

    public BlockedUser getBlock(String senderId, String receiverId) throws SQLException {
        String sql = "SELECT user_id as userId, blocked_user_id as blockedUserId\n" +
                "FROM blocked_users\n" +
                "WHERE (user_id = ? AND blocked_user_id = ?) OR (user_id = ? AND blocked_user_id = ?) LIMIT 1";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, senderId);
            statement.setString(2, receiverId);
            statement.setString(3, receiverId);
            statement.setString(4, senderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                BlockedUser blockedUser = new BlockedUser();
                blockedUser.setUserId(resultSet.getString("userId"));
                blockedUser.setBlockedUserId(resultSet.getString("blockedUserId"));
                return blockedUser;
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

    public List<String> getUserIdInGroup(String groupId) throws SQLException {
        String sql = "SELECT id\n" +
                "FROM users\n" +
                "WHERE id IN (\n" +
                "SELECT user_id\n" +
                "FROM `groups`\n" +
                "JOIN group_members ON group_id = `groups`.id\n" +
                "WHERE `groups`.id = ?)\n";

        List<String> userIds = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, groupId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userIds.add(resultSet.getString("id"));
            }
            return userIds;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return userIds;
    }

    public Group getGroupById(String groupId) throws SQLException {
        String sql = "SELECT  *" +
                "FROM `groups`\n" +
                "WHERE id = ?";

        Group group = new Group();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, groupId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                group.setAvatar(resultSet.getString("avatar"));
                group.setId(resultSet.getString("id"));
                group.setName(resultSet.getString("name"));
                group.setType(resultSet.getString("type"));
                group.setLastMessage(resultSet.getString("last_message"));
                group.setDesc(resultSet.getString("desc"));
                group.setOwner(resultSet.getString("owner"));
            }
            return group;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return null;
    }

    public List<User> getMemberInGroup(String groupId, String userId) throws SQLException {
        String sql = "SELECT users.*,\n" +
                "(SELECT `status` FROM friends WHERE\n" +
                "(friends.user_id = ? AND users.id = friends.friend_id) \n" +
                "OR (friends.friend_id = ? AND friends.user_id = users.id) \n" +
                ") AS `status` \n" +
                "FROM group_members, users \n" +
                "WHERE group_id = ? AND user_id = users.id";

        List<User> users = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, userId);
            statement.setString(2, userId);
            statement.setString(3, groupId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFullname(resultSet.getString("fullname"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setPhone(resultSet.getString("phone"));
                user.setConnectId(resultSet.getInt("connectid"));
                user.setAvatar(resultSet.getString("avatar"));
                user.setStatus(resultSet.getString("status"));
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

    public String createMultiConversation(String name, String desc, String owner, String avatar) throws SQLException {
        String sql = "INSERT INTO `groups`(`name`, `desc`, `owner`, `type`, `avatar`) VALUES (?, ?, ?, 'multi', ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, name);
            statement.setString(2, desc);
            statement.setString(3, owner);
            if (avatar == null)
                statement.setNull(4, Types.VARCHAR);
            else
                statement.setString(4, avatar);

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
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return null;
    }

    public int outConversation(String groupId, String userId)  throws SQLException {
        String sql = "DELETE FROM group_members WHERE group_id = ? AND user_id = ?";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, groupId);
            statement.setString(2, userId);

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

    public void deleteGroup(String groupId, String ownerId)  throws SQLException {
        String sql = "DELETE FROM `groups` WHERE id = ? and owner = ?";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, groupId);
            statement.setString(2, ownerId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement != null) {
                statement.close();
            }
        }
    }


    public void updateOwner(String groupId, String ownerId)  throws SQLException {
        String sql = "UPDATE `groups` SET owner = ? WHERE id = ?";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, ownerId);
            statement.setString(2, groupId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement != null) {
                statement.close();
            }
        }
    }


}
