package com.study.springStarter.util;

import java.util.Objects;

public class NoteSearchCondition {
    private int page = 1;
    private int pageSize = 10;
    private String keyword;
    private Integer userId;
    private Integer folderId;
    private Boolean isPinned;
    private String sortBy;
    private String sortOrder;
    private String startDate;
    private Integer offset;

    public NoteSearchCondition() {
        this.sortBy = "createdDate";
        this.sortOrder = "desc";
    }

    public NoteSearchCondition(String keyword, Integer folderId, Boolean isPinned, String sortBy, String sortOrder) {
        this.keyword = keyword;
        this.folderId = folderId;
        this.isPinned = isPinned;
        this.sortBy = (sortBy != null && !sortBy.isEmpty()) ? sortBy : "createdDate";
        this.sortOrder = (sortOrder != null && !sortOrder.isEmpty()) ? sortOrder : "desc";
    }

    public int getOffset() {
        return (page - 1) * pageSize;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Boolean getPinned() {
        return isPinned;
    }

    public void setPinned(Boolean pinned) {
        isPinned = pinned;
    }

    public void setPage(int page) {
        this.page = Math.max(1, page);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = Math.max(1, pageSize);
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public Boolean getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(Boolean isPinned) {
        this.isPinned = isPinned;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        if (sortBy != null && (sortBy.equals("createdDate") || sortBy.equals("updatedDate") || sortBy.equals("title"))) {
            this.sortBy = sortBy;
        } else {
            this.sortBy = "createdDate";
        }
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        if (sortOrder != null && (sortOrder.equalsIgnoreCase("asc") || sortOrder.equalsIgnoreCase("desc"))) {
            this.sortOrder = sortOrder;
        } else {
            this.sortOrder = "desc";
        }
    }

    @Override
    public String toString() {
        return "NoteSearchCondition{" +
                "keyword='" + keyword + '\'' +
                ", folderId=" + folderId +
                ", isPinned=" + isPinned +
                ", sortBy='" + sortBy + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteSearchCondition that = (NoteSearchCondition) o;
        return Objects.equals(keyword, that.keyword) &&
                Objects.equals(folderId, that.folderId) &&
                Objects.equals(isPinned, that.isPinned) &&
                Objects.equals(sortBy, that.sortBy) &&
                Objects.equals(sortOrder, that.sortOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword, folderId, isPinned, sortBy, sortOrder);
    }
}