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
 * @version 1.0, Jan 21, 2017
 * @since
 */
@Entity
@Table(name = "user_search_params", uniqueConstraints = @UniqueConstraint(columnNames = {
		"user", "key" }))
public class UserSearchParams implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8179776947035433129L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(length = 36, nullable = false, updatable = false)
	private String uuid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = false, updatable = false)
	private User user;

	@Column(name = "key", length = 50, nullable = false)
	private String key;

	@Column(name = "value", length = 50, nullable = false)
	private String value;

	public UserSearchParams() {
		super();
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
