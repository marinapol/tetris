package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "grid")
public class Grid extends AbstractEntity {

    @Range(min = 5, max = 20)
    private Integer height;

    @Range(min = 5, max = 10)
    private Integer width;

    public Grid() {
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Transient
    public String getGridSizeString() {
        return String.format("%s x %s", getWidth().toString(), getHeight().toString());
    }
}
