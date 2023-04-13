package com.chatvia.chatapp.Services;

import com.chatvia.chatapp.Entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GroupMemberService {
    private Connection connection;

    public GroupMemberService() {
        this.connection = DB.getConnection();
    }

    public void insertMember(String groupId, List<Integer> userIds) throws SQLException {
        String sql = "INSERT INTO group_members (group_id, user_id) VALUES (?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < userIds.size(); i++) {
                statement.setString(1, groupId);
                statement.setString(2, Integer.toString(userIds.get(i)));
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
}
