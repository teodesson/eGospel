/**
 * eGospel Project Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions
 * of the license agreement by which you acquired this software. You may not use
 * this software if you have not validly acquired a license for the software
 * from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "app_config")
@XmlRootElement(name = "config")
public class ApplicationConfig {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @NotEmpty
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String label;

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String value;

    public String getId() {
        return id;
    }

    @XmlElement
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    @XmlElement
    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    @XmlElement
    public void setValue(String value) {
        this.value = value;
    }
}
