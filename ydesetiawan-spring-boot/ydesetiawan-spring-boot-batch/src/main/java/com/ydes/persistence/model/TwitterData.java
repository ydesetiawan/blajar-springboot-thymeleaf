package com.ydes.persistence.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author edys
 * @version 1.0, Feb 4, 2015
 * @since
 */
@Entity
@Table(name = "twitter_data")
public class TwitterData implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2435918692524217525L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String uuid;

    @Column(name = "profile_name", length = 100, nullable = false)
    private String profileName;

    @Column(name = "profile_img_url", length = 250, nullable = false)
    private String profileImgUrl;

    @Column(name = "posting_date", nullable = false)
    private Date postingDate;

    @Column(name = "text", length = 250, nullable = false)
    private String text;

    public TwitterData() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public Date getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Date postingDate) {
        this.postingDate = postingDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TwitterData [uuid=" + uuid + ", profileName=" + profileName
                + ", profileImgUrl=" + profileImgUrl + ", postingDate="
                + postingDate + ", text=" + text + "]";
    }

}
