package com.bettingScanner.api.requests;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String scanUrl;
    private String displayUrl;
    private String keyword;
    private String chatId;
    private String userId;
    private Boolean finnished;
    private Boolean visibe;
    private LocalDate createdDate;
    private String tipsportCategory;
    @Lob
    private String state;
    private String requestType;

    public Request() {
    }

    public Request(String scanUrl, String displayUrl, String keyword, String chatId, String userId, Boolean finnished,
            Boolean visibe, LocalDate createdDate, String tipsportCategory, String state, String requestType) {
        this.scanUrl = scanUrl;
        this.displayUrl = displayUrl;
        this.keyword = keyword;
        this.chatId = chatId;
        this.userId = userId;
        this.finnished = finnished;
        this.visibe = visibe;
        this.createdDate = createdDate;
        this.tipsportCategory = tipsportCategory;
        this.state = state;
        this.requestType = requestType;
    }

    public Request(String url, String matchUrl, String keyword, String chatId) {
        this(url, matchUrl, keyword, chatId, null, false, true, LocalDate.now(), "", "", "NORMAL");
    }

    public Request(String url, String matchUrl, String chatId, String tipsportCategory, String inintialState) {
        this(url, matchUrl, "", chatId, null, false, true, LocalDate.now(), tipsportCategory, inintialState, "STATE");
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScanUrl() {
        return this.scanUrl;
    }

    public void setScanUrl(String scanUrl) {
        this.scanUrl = scanUrl;
    }

    public String getDisplayUrl() {
        return this.displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getChatId() {
        return this.chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean isFinnished() {
        return this.finnished;
    }

    public Boolean getFinnished() {
        return this.finnished;
    }

    public void setFinnished(Boolean finnished) {
        this.finnished = finnished;
    }

    public Boolean isVisibe() {
        return this.visibe;
    }

    public Boolean getVisibe() {
        return this.visibe;
    }

    public void setVisibe(Boolean visibe) {
        this.visibe = visibe;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getTipsportCategory() {
        return this.tipsportCategory;
    }

    public void setTipsportCategory(String tipsportCategory) {
        this.tipsportCategory = tipsportCategory;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
