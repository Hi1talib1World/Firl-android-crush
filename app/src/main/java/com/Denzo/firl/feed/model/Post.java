package com.Denzo.firl.feed.model;

import com.Denzo.firl.feed.enums.ItemType;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Post implements Serializable, LazyLoading {

    private String id;
    private String title;
    private String description;
    private long createdDate;
    private String imagePath;
    private String imageTitle;
    private String authorId;
    private long commentsCount;
    private long likesCount;
    private long watchersCount;
    private boolean hasComplain;
    private ItemType itemType;

    public Post() {
        this.createdDate = new Date().getTime();
        itemType = ItemType.ITEM;
    }

    public Post(ItemType itemType) {
        this.itemType = itemType;
        setId(itemType.toString());
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public long getWatchersCount() {
        return watchersCount;
    }

    public void setWatchersCount(long watchersCount) {
        this.watchersCount = watchersCount;
    }

    public boolean isHasComplain() {
        return hasComplain;
    }

    public void setHasComplain(boolean hasComplain) {
        this.hasComplain = hasComplain;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("title", title);
        result.put("description", description);
        result.put("createdDate", createdDate);
        result.put("imagePath", imagePath);
        result.put("imageTitle", imageTitle);
        result.put("authorId", authorId);
        result.put("commentsCount", commentsCount);
        result.put("likesCount", likesCount);
        result.put("watchersCount", watchersCount);
        result.put("hasComplain", hasComplain);
        result.put("createdDateText", FormatterUtil.getFirebaseDateFormat().format(new Date(createdDate)));

        return result;
    }

    @Override
    public ItemType getItemType() {
        return itemType;
    }

    @Override
    public void setItemType(ItemType itemType) {

    }
}