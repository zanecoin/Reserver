package com.application.reserver;

import java.util.HashMap;
import java.util.Map;

public class User {
    /* local variables */
    private String uid;
    private String email;
    private String password;
    private String name;
    private String phone;
    /* public user method */
    public User(String uid, String email, String password, String name, String phone) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }
    /* user info methods */
    public String getUid() {
        return uid;
    }

    public String getEmail() { return email; }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    /* method to create map of user data */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("email", email);
        map.put("name", name);
        map.put("phone", phone);

        return map;
    }

    /* method to get user data from the map */
    public static User fromMap(String uid, String password, Map<String, Object> map) {
        String email = null;
        String name = null;
        String phone = null;

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            switch (entry.getKey()) {
                case "email": {
                    email = (String) entry.getValue();
                    break;
                }

                case "name": {
                    name = (String) entry.getValue();
                    break;
                }

                case "phone": {
                    phone = (String) entry.getValue();
                    break;
                }
            }
        }

        return new User(uid, email, password, name, phone);
    }
}
