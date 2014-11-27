/**
 * eGospel Project Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions
 * of the license agreement by which you acquired this software. You may not use
 * this software if you have not validly acquired a license for the software
 * from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Desson Ariawan <teodesson@yahoo.com>
 */
@Entity
@Table(name = "dar_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 19589241915764L;
    
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    @Column(name = "menu_action")
    private String action;

    @NotNull
    @NotEmpty
    @Column(name = "label", nullable = false)
    private String label;

    @NotNull
    @Min(0)
    @Column(name = "menu_level", nullable = false)
    private Integer level = 0;

    @NotNull
    @Min(0)
    @Column(name = "menu_order", nullable = false)
    private Integer order = 0;

    @Column(name = "menu_options")
    private String options;

    @ManyToOne
    @JoinColumn(name = "id_parent")
    private Menu parent;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return the order
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * @return the options
     */
    public String getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(String options) {
        this.options = options;
    }

    /**
     * @return the parent
     */
    public Menu getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Menu parent) {
        this.parent = parent;
    }
}
