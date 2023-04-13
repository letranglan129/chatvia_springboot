package com.chatvia.chatapp.WS;

import com.chatvia.chatapp.Entities.*;
import com.chatvia.chatapp.Services.*;
import com.chatvia.chatapp.Ultis.Uploader;
import com.chatvia.chatapp.WS.Event.*;
import com.google.gson.*;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


public class ChatServer extends WebSocketServer {
    public Map<WebSocket, String> clients = new HashMap<>();
    public UserService userService;
    public ConversationService conversationService;
    public GroupMemberService groupMemberService;
    public MessageService messageService;
    public FileService fileService;
    Gson gson = new Gson();

    public ChatServer() {
        super(new InetSocketAddress(9002));
        userService = new UserService();
        conversationService = new ConversationService();
        messageService = new MessageService();
        groupMemberService = new GroupMemberService();
        fileService = new FileService();
    }


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();


            String querystring = handshake.getResourceDescriptor().split("\\?")[1];

            Map<String, String> querymap = new HashMap<String, String>();

            for (String param : querystring.split("&")) {
                String[] pair = param.split("=");
                querymap.put(pair[0], pair[1]);
            }

            if (querymap.containsKey("id")) {
                int userId = Integer.parseInt(querymap.get("id"));

                clients.put(conn, querymap.get("id"));

                Map<String, Object> updateConnect = new HashMap<>();

                List<User> users = null;

                users = userService.getFriends(querymap.get("id"));

                List<Conversation> conversationsPrivate = conversationService.getPrivateConversation(querymap.get("id"));
                List<Conversation> conversationsMulti = conversationService.getMultiConversation(querymap.get("id"));
                List<Message> unReadMessages = conversationService.getUnreadMessage(querymap.get("id"));

                JsonObject friendDataEvent = new JsonObject();
                JsonObject conversationDataEvent = new JsonObject();
                JsonArray usersObject = gson.toJsonTree(users).getAsJsonArray();
                JsonArray conversationsPrivateObject = gson.toJsonTree(conversationsPrivate).getAsJsonArray();
                JsonArray conversationsMultiObject = gson.toJsonTree(conversationsMulti).getAsJsonArray();
                JsonArray unReadMessagesObject = gson.toJsonTree(unReadMessages).getAsJsonArray();
                friendDataEvent.addProperty("event", "onGetFriends");
                friendDataEvent.add("friends", usersObject);

                conversationDataEvent.addProperty("event", "onGetConversation");
                conversationDataEvent.add("conversation", conversationsPrivateObject);
                conversationDataEvent.add("groupConversation", conversationsMultiObject);
                conversationDataEvent.add("unreadMessages", unReadMessagesObject);

                for (WebSocket client : clients.keySet()) {
                    if (client.equals(conn)) {
                        client.send(friendDataEvent.toString());
                        client.send(conversationDataEvent.toString());
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println(conn + " has left the room!");

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        WSEventReceiver res = gson.fromJson(message, WSEventReceiver.class);
        System.out.println(conn + ": " + message);
        System.out.println(res.getEvent());
        switch (res.getEvent()) {
            case Command.SEND_START_CHAT_PRIVATE: {
                try {
                    JsonObject startChatPrivateJson = new JsonObject();
                    JsonArray messagesJson;
                    List<Message> messagesText = new ArrayList<>();
                    List<Message> messagesMedia = new ArrayList<>();
                    List<Message> files = new ArrayList<>();
                    List<Message> newMediaMessages = new ArrayList<>();

                    StartChatPrivateEvent startChatPrivateEvent = this.gson.fromJson(message, StartChatPrivateEvent.class);
                    if (startChatPrivateEvent.getSenderId() != null && startChatPrivateEvent.getReceiverId() != null) {
                        String groupId = null;
                        User sender = userService.findUserById(startChatPrivateEvent.getSenderId());
                        User receiver = userService.findUserById(startChatPrivateEvent.getReceiverId());
                        groupId = conversationService.checkDouGroupExist(startChatPrivateEvent.getSenderId(), startChatPrivateEvent.getReceiverId());

                        if (groupId != null) {
                            startChatPrivateJson.addProperty("groupId", groupId);
                            messagesText = messageService.getMessageText(groupId, Integer.toString(startChatPrivateEvent.getSenderId()));
                            messagesMedia = messageService.getMessageMedia(groupId, Integer.toString(startChatPrivateEvent.getSenderId()));
                            files = messageService.getFile(groupId, Integer.toString(startChatPrivateEvent.getSenderId()));
                        } else {
                            String newGroupId = conversationService.createPrivateConversation();
                            startChatPrivateJson.addProperty("groupId", newGroupId);

                            List<Integer> userIds = new ArrayList<>();
                            userIds.add(startChatPrivateEvent.getSenderId());
                            userIds.add(startChatPrivateEvent.getReceiverId());
                            groupMemberService.insertMember(newGroupId, userIds);

                            messagesJson = gson.toJsonTree(new ArrayList<>()).getAsJsonArray();
                        }

                        newMediaMessages = mergeMessageImage(messagesMedia);


                        BlockedUser blockedUser = conversationService.getBlock(Integer.toString(startChatPrivateEvent.getSenderId()), Integer.toString(startChatPrivateEvent.getReceiverId()));
                        if (blockedUser == null) {
                            JsonNull blockedUserObject = gson.toJsonTree(null).getAsJsonNull();
                            startChatPrivateJson.add("blocked", blockedUserObject);
                        } else {
                            JsonObject blockedUserObject = gson.toJsonTree(blockedUser).getAsJsonObject();
                            startChatPrivateJson.add("blocked", blockedUserObject);
                        }
                        messagesText.addAll(newMediaMessages);
                        JsonObject senderObject = gson.toJsonTree(sender).getAsJsonObject();
                        JsonObject receiverObject = gson.toJsonTree(receiver).getAsJsonObject();
                        JsonArray filesObject = gson.toJsonTree(files).getAsJsonArray();
                        JsonArray messagesObject = gson.toJsonTree(messagesText).getAsJsonArray();


                        startChatPrivateJson.addProperty("event", "onStartChatPrivate");
                        startChatPrivateJson.add("sender", senderObject);
                        startChatPrivateJson.add("receiver", receiverObject);
                        startChatPrivateJson.add("messages", messagesObject);
                        startChatPrivateJson.addProperty("type", "dou");
                        startChatPrivateJson.add("files", filesObject);
                        for (WebSocket client : clients.keySet()) {
                            if (client.equals(conn)) {
                                client.send(startChatPrivateJson.toString());
                            }
                        }
                    }


                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            }

            case Command.SEND_NEW_MESSAGE: {
                try {
                    SendMessageEvent sendMessageEvent = this.gson.fromJson(message, SendMessageEvent.class);
                    JsonObject newMessageObject = new JsonObject();
                    if (sendMessageEvent.getMsg() != null && !sendMessageEvent.getMsg().trim().equals("")) {
                        messageService.insertMessage(sendMessageEvent.getGroupId(), Integer.toString(sendMessageEvent.getSenderId()), sendMessageEvent.getMsg());

                        User sender = userService.findUserById(sendMessageEvent.getSenderId());
                        List<String> userIds = conversationService.getUserIdInGroup(sendMessageEvent.getGroupId());

                        newMessageObject.addProperty("event", "onNewMessage");
                        newMessageObject.addProperty("groupId", sendMessageEvent.getGroupId());
                        newMessageObject.addProperty("msg", sendMessageEvent.getMsg());
                        newMessageObject.addProperty("senderId", Integer.toString(sendMessageEvent.getSenderId()));

                        for (Map.Entry<WebSocket, String> client : clients.entrySet()) {
                            if (userIds.contains(client.getValue())) {
                                if (client.getKey() == conn) {
                                    newMessageObject.addProperty("from", "");
                                } else {
                                    newMessageObject.addProperty("from", sender.getFullname());
                                }

                                client.getKey().send(newMessageObject.toString());
                            }
                        }
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            }

            case Command.SEND_GET_CONVERSATION: {
                try {
                    GetConversationEvent getConversationEvent = this.gson.fromJson(message, GetConversationEvent.class);

                    List<Conversation> conversationsPrivate = conversationService.getPrivateConversation(getConversationEvent.getUserId());
                    List<Conversation> conversationsMulti = conversationService.getMultiConversation(getConversationEvent.getUserId());
                    List<Message> unReadMessages = conversationService.getUnreadMessage(getConversationEvent.getUserId());

                    JsonObject conversationDataEvent = new JsonObject();
                    JsonArray conversationsPrivateObject = gson.toJsonTree(conversationsPrivate).getAsJsonArray();
                    JsonArray conversationsMultiObject = gson.toJsonTree(conversationsMulti).getAsJsonArray();
                    JsonArray unReadMessagesObject = gson.toJsonTree(unReadMessages).getAsJsonArray();

                    conversationDataEvent.addProperty("event", "onGetConversation");
                    conversationDataEvent.add("conversation", conversationsPrivateObject);
                    conversationDataEvent.add("groupConversation", conversationsMultiObject);
                    conversationDataEvent.add("unreadMessages", unReadMessagesObject);

                    for (WebSocket client : clients.keySet()) {
                        if (client.equals(conn)) {
                            client.send(conversationDataEvent.toString());
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                break;
            }

            case Command.SEND_SENT_MESSAGE: {
                try {
                    SentMessageEvent sentMessageEvent = this.gson.fromJson(message, SentMessageEvent.class);
                    String tempMessageIdsString = "0";
                    JsonObject sentMessageObject = new JsonObject();

                    List<String> messageIds = messageService.getMessageIdsInGroup(sentMessageEvent.getGroupId());

                    if (messageIds.size() > 0) {
                        messageService.updateBatchMessageSeen(sentMessageEvent.getUserId(), messageIds);

                        tempMessageIdsString = String.join(", ", messageIds);
                    }

                    List<String> userIds = conversationService.getUserIdInGroup(sentMessageEvent.getGroupId());
                    List<Message> messagesJustSeen = messageService.getMessageJustSeen(sentMessageEvent.getGroupId(), tempMessageIdsString);
                    JsonArray messagesJustSeenObject = gson.toJsonTree(messagesJustSeen).getAsJsonArray();
                    sentMessageObject.addProperty("event", "onResponseSent");
                    sentMessageObject.addProperty("groupId", sentMessageEvent.getGroupId());
                    sentMessageObject.add("messages", messagesJustSeenObject);
                    System.out.println("Hello" + sentMessageObject.toString());
                    for (Map.Entry<WebSocket, String> client : clients.entrySet()) {
                        if (userIds.contains(client.getValue())) {
                            if (client.getKey() != null)
                                client.getKey().send(sentMessageObject.toString());
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                break;
            }

            case Command.SEND_START_CHAT_MULTI: {
                try {
                    JsonObject startChatMultiObject = new JsonObject();
                    StartChatMultiEvent startChatMultiEvent = this.gson.fromJson(message, StartChatMultiEvent.class);
                    List<Message> messagesText = new ArrayList<>();
                    List<Message> messagesMedia = new ArrayList<>();
                    List<Message> files = new ArrayList<>();
                    List<Message> newMediaMessages = new ArrayList<>();

                    messagesText = messageService.getMessageText(Integer.toString(startChatMultiEvent.getGroupId()), Integer.toString(startChatMultiEvent.getUserId()));
                    messagesMedia = messageService.getMessageMedia(Integer.toString(startChatMultiEvent.getGroupId()), Integer.toString(startChatMultiEvent.getUserId()));
                    files = messageService.getFile(Integer.toString(startChatMultiEvent.getGroupId()), Integer.toString(startChatMultiEvent.getUserId()));
                    Group group = conversationService.getGroupById(Integer.toString(startChatMultiEvent.getGroupId()));
                    List<User> members = conversationService.getMemberInGroup(Integer.toString(startChatMultiEvent.getGroupId()), Integer.toString(startChatMultiEvent.getUserId()));
                    group.setMembers(members);

                    newMediaMessages = mergeMessageImage(messagesMedia);

                    messagesText.addAll(newMediaMessages);

                    startChatMultiObject.addProperty("event", "onStartChatMulti");
                    startChatMultiObject.addProperty("type", "multi");
                    startChatMultiObject.addProperty("userId", startChatMultiEvent.getUserId());
                    JsonArray filesObject = gson.toJsonTree(files).getAsJsonArray();
                    JsonArray messagesObject = gson.toJsonTree(messagesText).getAsJsonArray();
                    JsonObject groupObject = gson.toJsonTree(group).getAsJsonObject();
                    startChatMultiObject.add("files", filesObject);
                    startChatMultiObject.add("groupInfo", groupObject);
                    startChatMultiObject.add("messages", messagesObject);

                    for (WebSocket client : clients.keySet()) {
                        if (client.equals(conn)) {
                            client.send(startChatMultiObject.toString());
                        }
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                break;
            }

            case Command.SEND_CREATE_GROUP: {
                try {
                    JsonObject createGroupJson = new JsonObject();
                    CreateGroupEvent createGroupEvent = this.gson.fromJson(message, CreateGroupEvent.class);
                    String[] fileTypes = {"image/jpeg", "image/png", "image/gif"};

                    String href = null;

                    if (createGroupEvent.getAvatar() != null && !createGroupEvent.getAvatar().data.equals("")) {
                        String[] parts = createGroupEvent.getAvatar().getData().split(",");
                        String extension = parts[0].split("/")[1].split(";")[0];
                        String fileName = createGroupEvent.getAvatar().getName();
                        byte[] data = Base64.getDecoder().decode(parts[1]);
                        java.io.File resourceDirectory = new java.io.File("src/main/resources");
                        String absolutePath = resourceDirectory.getAbsolutePath();
                        java.io.File file = new java.io.File(absolutePath + "/" + fileName);
                        FileOutputStream outputStream = new FileOutputStream(file);
                        outputStream.write(data);
                        outputStream.close();

                        Uploader uploader = new Uploader();
                        Map result = uploader.uploadPath(absolutePath + "/" + fileName);
                        href = (String) result.get("secure_url");

                        if (file.delete()) {
                            System.out.println("File đã được xóa.");
                        } else {
                            System.out.println("Không thể xóa file.");
                        }
                    }

                    createGroupEvent.getGroupMembers().add(createGroupEvent.getSenderId());
                    String newGroupId = conversationService.createMultiConversation(createGroupEvent.getGroupName(), createGroupEvent.getGroupDesc(), createGroupEvent.getSenderId(), href);

                    System.out.println(newGroupId);

                    groupMemberService.insertMember(newGroupId, createGroupEvent.getGroupMembers().stream().map(Integer::parseInt).collect(Collectors.toList()));
                    messageService.insertMessage(newGroupId, "0", createGroupEvent.getFullname() + " đã tạo nhóm");

                    createGroupJson.addProperty("event", "onCreateChatGroup");
                    createGroupJson.addProperty("userCreateId", createGroupEvent.getSenderId());
                    createGroupJson.addProperty("userCreateName", createGroupEvent.getFullname());
                    createGroupJson.addProperty("groupName", createGroupEvent.getGroupName());

                    List<String> userIds = conversationService.getUserIdInGroup(newGroupId);
                    for (Map.Entry<WebSocket, String> client : clients.entrySet()) {
                        if (userIds.contains(client.getValue())) {
                            if (client.getKey() != null)
                                client.getKey().send(createGroupJson.toString());
                        }
                    }

                } catch (IOException | SQLException throwables) {
                    throwables.printStackTrace();
                }

                break;
            }

            case Command.SEND_SEND_FILE: {
                try {
                    JsonObject sendFileObject = new JsonObject();
                    SendFileEvent sendFileEvent = this.gson.fromJson(message, SendFileEvent.class);
                    List<String> fileTypes = Arrays.asList("image/jpeg", "image/png", "image/gif");
                    List<File> images = new ArrayList<>();
                    List<File> others = new ArrayList<>();


                    for (int i = 0; i < sendFileEvent.getFiles().size(); i++) {
                        FileReceiver fileReceiver = sendFileEvent.getFiles().get(i);
                        String[] parts = fileReceiver.data.split(",");

                        byte[] data = Base64.getDecoder().decode(parts[1]);

                        java.io.File resourceDirectory = new java.io.File("src/main/resources");
                        String absolutePath = resourceDirectory.getAbsolutePath();
                        java.io.File file = new java.io.File(absolutePath + "/" + fileReceiver.name);

                        FileOutputStream outputStream = outputStream = new FileOutputStream(file);
                        outputStream.write(data);
                        outputStream.close();

                        Uploader uploader = new Uploader();
                        Map result = uploader.uploadPath(absolutePath + "/" + fileReceiver.name);


                        if (fileTypes.contains(fileReceiver.type)) {
                            File newFile = new File();
                            newFile.setName(result.get("original_filename") + "." + result.get("format"));
                            newFile.setSize(Integer.toString((int) result.get("bytes")));
                            newFile.setHref((String) result.get("secure_url"));
                            images.add(newFile);
                        }

                        if (!fileTypes.contains(fileReceiver.type)) {
                            String extension = fileReceiver.name.substring(fileReceiver.name.lastIndexOf(".") + 1);

                            if (result.containsKey("format")) {
                                extension = (String) result.get("format");
                            }

                            File newFile = new File();
                            newFile.setName(result.get("original_filename") + "." + extension);
                            newFile.setSize(Integer.toString((int) result.get("bytes")));
                            newFile.setHref((String) result.get("secure_url"));
                            others.add(newFile);
                        }
                        if (file.delete()) {
                            System.out.println("File đã được xóa.");
                        } else {
                            System.out.println("Không thể xóa file.");
                        }
                    }

                    if (images.size() > 0) {
                        String newMessageId = messageService.insertMessage(sendFileEvent.getGroupId(), sendFileEvent.getSenderId(), "[Hình ảnh]", "image");
                        fileService.insertFiles(newMessageId, images);
                    }

                    for (File other : others) {
                        String newMessageId = messageService.insertMessage(sendFileEvent.getGroupId(), sendFileEvent.getSenderId(), "[File]", "file");
                        fileService.insertFile(newMessageId, other);
                    }

                    List<String> userIds = conversationService.getUserIdInGroup(sendFileEvent.getGroupId());

                    sendFileObject.addProperty("event", "onNewMessage");
                    sendFileObject.addProperty("senderId", sendFileEvent.getSenderId());
                    sendFileObject.addProperty("groupId", sendFileEvent.getGroupId());

                    for (Map.Entry<WebSocket, String> client : clients.entrySet()) {
                        if (userIds.contains(client.getValue())) {
                            if (client.getKey() != null)
                                client.getKey().send(sendFileObject.toString());
                        }
                    }

                } catch (IOException | SQLException throwables) {
                    throwables.printStackTrace();
                }

                break;
            }

            case Command.SEND_FORWARD_MESSAGE: {
                try {
                    JsonObject forwardMessageObject = new JsonObject();
                    ForwardMessageEvent forwardMessageEvent = this.gson.fromJson(message, ForwardMessageEvent.class);
                    Message forwardMessage = messageService.findById(forwardMessageEvent.getMessageId());

                    if (forwardMessage == null) {
                        JsonObject failObject = new JsonObject();
                        failObject.addProperty("event", "onForwardMessage");
                        failObject.addProperty("result", false);
                        for (WebSocket client : clients.keySet()) {
                            if (client.equals(conn)) {
                                client.send(failObject.toString());
                            }
                        }
                    }

                    for (int i = 0; i < forwardMessageEvent.getReceiversPrivate().size(); i++) {
                        String receiverId = forwardMessageEvent.getReceiversPrivate().get(i);
                        String groupId = conversationService.checkDouGroupExist(forwardMessageEvent.getSenderId(), Integer.parseInt(receiverId));

                        if (groupId == null) {
                            groupId = conversationService.createPrivateConversation();

                            List<Integer> userIds = new ArrayList<>();
                            userIds.add(forwardMessageEvent.getSenderId());
                            userIds.add(Integer.parseInt(receiverId));
                            groupMemberService.insertMember(groupId, userIds);
                        }

                        forwardMessage.setSenderId(Integer.toString(forwardMessageEvent.getSenderId()));
                        String forwardMessageId = messageService.insertMessage(
                                groupId,
                                forwardMessage.getSenderId(),
                                forwardMessage.getMessage(),
                                forwardMessage.getFormat()
                        );

                        if (forwardMessage.getFormat().equals("image") || forwardMessage.getFormat().equals("file")) {
                            List<File> files = fileService.findByMessageId(forwardMessageEvent.getMessageId());
                            fileService.insertFiles(forwardMessageId, files);
                        }

                        JsonObject forwardPrivateObject = new JsonObject();
                        forwardPrivateObject.addProperty("event", "onNewMessage");
                        forwardPrivateObject.addProperty("groupId", groupId);
                        forwardPrivateObject.addProperty("result", true);

                        for (Map.Entry<WebSocket, String> client : clients.entrySet()) {
                            if (client.getValue().equals(receiverId)) {
                                if (client.getKey() != null)
                                    client.getKey().send(forwardPrivateObject.toString());
                            }
                        }
                    }

                    for (int i = 0; i < forwardMessageEvent.getReceiversGroup().size(); i++) {
                        String groupId = forwardMessageEvent.getReceiversGroup().get(i);
                        forwardMessage.setSenderId(Integer.toString(forwardMessageEvent.getSenderId()));
                        String forwardMessageId = messageService.insertMessage(
                                groupId,
                                forwardMessage.getSenderId(),
                                forwardMessage.getMessage(),
                                forwardMessage.getFormat()
                        );

                        if (forwardMessage.getFormat().equals("image") || forwardMessage.getFormat().equals("file")) {
                            List<File> files = fileService.findByMessageId(forwardMessageEvent.getMessageId());
                            fileService.insertFiles(forwardMessageId, files);
                        }

                        JsonObject forwardMultiObject = new JsonObject();
                        forwardMultiObject.addProperty("event", "onNewMessage");
                        forwardMultiObject.addProperty("groupId", groupId);
                        forwardMultiObject.addProperty("result", true);

                        List<String> userIds = conversationService.getUserIdInGroup(groupId);

                        for (Map.Entry<WebSocket, String> client : clients.entrySet()) {
                            if (userIds.contains(client.getValue())) {
                                if (client.getKey() != null)
                                    client.getKey().send(forwardMultiObject.toString());
                            }
                        }
                    }

                    forwardMessageObject.addProperty("event", "onForwardMessage");
                    forwardMessageObject.addProperty("result", true);
                    for (WebSocket client : clients.keySet()) {
                        if (client.equals(conn)) {
                            client.send(forwardMessageObject.toString());
                        }
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                break;
            }

            case Command.SEND_DELETE_MESSAGE: {
                try {
                    JsonObject deleteMessageObject = new JsonObject();
                    DeleteMessageEvent deleteMessageEvent = this.gson.fromJson(message, DeleteMessageEvent.class);
                    List<String> userIds = conversationService.getUserIdInGroup(Integer.toString(deleteMessageEvent.getGroupId()));

                    if(deleteMessageEvent.getType().equals("all")) {
                        messageService.deleteMessage(Integer.toString(deleteMessageEvent.getMessageId()), userIds);
                    } else {
                        messageService.deleteMessage(Integer.toString(deleteMessageEvent.getMessageId()), Integer.toString(deleteMessageEvent.getUserId()));
                    }

                    deleteMessageObject.addProperty("event", "onDeleteMessage");
                    deleteMessageObject.addProperty("messageId", deleteMessageEvent.getMessageId());
                    deleteMessageObject.addProperty("groupId", deleteMessageEvent.getGroupId());
                    deleteMessageObject.addProperty("userId", deleteMessageEvent.getUserId());
                    deleteMessageObject.addProperty("type", deleteMessageEvent.getType());
                    deleteMessageObject.addProperty("result", true);

                    for (Map.Entry<WebSocket, String> client : clients.entrySet()) {
                        if (userIds.contains(client.getValue())) {
                            if (client.getKey() != null)
                                client.getKey().send(deleteMessageObject.toString());
                        }
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                break;
            }

        }
    }

    private List<Message> mergeMessageImage(List<Message> messagesMedia) {
        List<Message> newMediaMessages = new ArrayList<>();
        for (Message mediaMessage : messagesMedia) {
            int key = -1;
            for (int i = 0; i < newMediaMessages.size(); i++) {
                Message newMediaMessage = newMediaMessages.get(i);
                if (newMediaMessage.getId().equals(mediaMessage.getId())) {
                    key = i;
                    break;
                }
            }
            if (key == -1) {
                Message newMediaMessage = new Message();
                newMediaMessage.setId(mediaMessage.getId());
                newMediaMessage.setSenderId(mediaMessage.getSenderId());
                newMediaMessage.setSentAt(mediaMessage.getSentAt());
                newMediaMessage.setFormat(mediaMessage.getFormat());
                newMediaMessage.setAvatar(mediaMessage.getAvatar());
                newMediaMessage.setFullname(mediaMessage.getFullname());
                newMediaMessage.setGroupId(mediaMessage.getGroupId());
                newMediaMessage.setMessage(mediaMessage.getMessage());
                newMediaMessage.setViewedAt(mediaMessage.getViewedAt());

                List<File> filesTmp = new ArrayList<>();
                File file = new File();
                file.setHref(mediaMessage.getHref());
                file.setName(mediaMessage.getName());
                file.setSize(mediaMessage.getSize());
                filesTmp.add(file);
                newMediaMessage.setFiles(filesTmp);
                newMediaMessages.add(newMediaMessage);
            } else {
                Message newMediaMessage = newMediaMessages.get(key);
                List<File> filesTmp = newMediaMessage.getFiles();
                File file = new File();
                file.setHref(mediaMessage.getHref());
                file.setName(mediaMessage.getName());
                file.setSize(mediaMessage.getSize());
                filesTmp.add(file);
                newMediaMessage.setFiles(filesTmp);
                newMediaMessages.set(key, newMediaMessage);
            }
        }

        return newMediaMessages;
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        System.out.println(conn + ": " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }
}