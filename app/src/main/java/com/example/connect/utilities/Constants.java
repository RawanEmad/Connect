package com.example.connect.utilities;

import java.util.HashMap;

public class Constants {

    public static String KEY_ID = "userId";
    public static String KEY_FULL_NAME = "fullName";
    public static String KEY_PHONE_NO = "phoneNo";
    public static String KEY_IMAGE = "profileImage";
    public static String KEY_GENDER = "gender";
    public static String KEY_FCM_TOKEN = "fcmToken";

    public static int STORAGE_REQUEST_CODE = 1000;
    public static int RECORDING_REQUEST_CODE = 3000;

    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TYPE = "type";
    public static final String KEY_TIMESTAMP = "timestamp";

    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "LastMessage";

    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "to";

    public static HashMap<String, String> remoteMsgHeaders = null;

    public static HashMap<String, String> getRemoteMsgHeaders() {
        if (remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAuKDqtvU:APA91bHShKnV6WKuygLUZ-as_3IXSI7cm5hV2hhwj84ybOGq6TnZS04Vtfq7rvuU7ApAO0FSKnvZvWx-XW_Xt5hwqI7u2s0iShROZl3DAP_acXFDPGorKHuezLyNf8RrBLGO1yzbJ4K6"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remoteMsgHeaders;
    }
}
