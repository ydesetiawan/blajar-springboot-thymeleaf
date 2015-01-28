package com.ydesetiawan.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author edys
 * @version 1.0, Jan 14, 2015
 * @since
 */
@Entity
@Table(name = "SAMPLE_Category")
public class Category implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4743504366179640526L;

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String uuid;

    private String name;

    private String description;

    public Category() {
        super();
    }

    public Category(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public Category(String uuid, String name, String description) {
        super();
        this.uuid = uuid;
        this.name = name;
        this.description = description;
    }

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid
     *            the uuid to set
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category [name=" + name + ", description=" + description + "]";
    }

}
