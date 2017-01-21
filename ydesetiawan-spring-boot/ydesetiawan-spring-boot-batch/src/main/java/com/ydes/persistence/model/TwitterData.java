package com.ydes.persistence.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author edys
 * @version 1.0, Jan 21, 2017
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
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "profile_name", length = 100, nullable = false)
    private String profileName;

    @Column(name = "profile_img_url", length = 255, nullable = false)
    private String profileImgUrl;

    @Column(name = "posting_date", nullable = false)
    private Date postingDate;

    @Lob
    @Column(name = "text", length = 512)
    private String text;

    public TwitterData() {
        super();
        // TODO Auto-generated constructor stub
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        return "TwitterData [uuid=" + id + ", profileName=" + profileName
                + ", profileImgUrl=" + profileImgUrl + ", postingDate="
                + postingDate + ", text=" + text + "]";
    }

}
