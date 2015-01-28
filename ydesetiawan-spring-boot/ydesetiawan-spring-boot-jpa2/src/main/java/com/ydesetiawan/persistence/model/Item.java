package com.ydesetiawan.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author edys
 * @version 1.0, Jan 14, 2015
 * @since
 */
@Entity
@Table(name = "SAMPLE_Item")
public class Item implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6927466332279172157L;

    @Id
    private String uuid;

    @ManyToOne
    private Category category;

    private String item;

    private int qty;

    private BigDecimal price;

    private String description;

    public Item() {
        super();
    }

    public Item(String uuid, Category category, String item, int qty,
            BigDecimal price, String description) {
        super();
        this.uuid = uuid;
        this.category = category;
        this.item = item;
        this.qty = qty;
        this.price = price;
        this.description = description;
    }

    public Item(Category category, String item, int qty, BigDecimal price,
            String description) {
        super();
        this.category = category;
        this.item = item;
        this.qty = qty;
        this.price = price;
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
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return the item
     */
    public String getItem() {
        return item;
    }

    /**
     * @param item
     *            the item to set
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     * @return the qty
     */
    public int getQty() {
        return qty;
    }

    /**
     * @param qty
     *            the qty to set
     */
    public void setQty(int qty) {
        this.qty = qty;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
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
        return "Item [category=" + category.getName() + ", item=" + item
                + ", qty=" + qty + ", price=" + price + ", description="
                + description + "]";
    }

}
