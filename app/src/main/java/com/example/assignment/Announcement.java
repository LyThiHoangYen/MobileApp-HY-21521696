package com.example.assignment;

import com.google.firebase.Timestamp;

public class Announcement {
    private String id;
    private String title;
    private String content;
    private String authorId;
    private String authorName;
    private Timestamp timestamp;

    // Empty constructor needed for Firestore
    public Announcement() {
    }

    public Announcement(String title, String content, String authorId, String authorName, Timestamp timestamp) {
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
} 