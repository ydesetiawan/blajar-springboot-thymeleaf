package com.ydes.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author edys
 * @version 1.0, Feb 4, 2015
 * @since
 */
@Entity
@Table(name = "user_role",
        uniqueConstraints = @UniqueConstraint(columnNames = { "role", "user" }))
public class UserRole implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1920583714607590726L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false, updatable = false)
    private User user;

    @Column(name = "role", length = 50, nullable = false)
    private String role;

    public UserRole() {
        //
    }

    public UserRole(User user, String role) {
        this.user = user;
        this.role = role;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
