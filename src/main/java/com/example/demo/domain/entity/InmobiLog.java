package com.example.demo.domain.entity;

import java.util.Date;

public class InmobiLog {
    private Integer logId;

    private String campaignId;

    private String campaignName;

    private String adgroupId;

    private String adgroupName;

    private String siteId;

    private String siteName;

    private String requestUid;

    private Date date;

    private Date createTime;

    private Date updateTime;

    private String adImpressionsRendered;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId == null ? null : campaignId.trim();
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName == null ? null : campaignName.trim();
    }

    public String getAdgroupId() {
        return adgroupId;
    }

    public void setAdgroupId(String adgroupId) {
        this.adgroupId = adgroupId == null ? null : adgroupId.trim();
    }

    public String getAdgroupName() {
        return adgroupName;
    }

    public void setAdgroupName(String adgroupName) {
        this.adgroupName = adgroupName == null ? null : adgroupName.trim();
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId == null ? null : siteId.trim();
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName == null ? null : siteName.trim();
    }

    public String getRequestUid() {
        return requestUid;
    }

    public void setRequestUid(String requestUid) {
        this.requestUid = requestUid == null ? null : requestUid.trim();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAdImpressionsRendered() {
        return adImpressionsRendered;
    }

    public void setAdImpressionsRendered(String adImpressionsRendered) {
        this.adImpressionsRendered = adImpressionsRendered == null ? null : adImpressionsRendered.trim();
    }
}