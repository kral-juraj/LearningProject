package com.beekeeper.shared.entity;

import java.io.Serializable;

/**
 * Settings (Nastavenia) entity.
 * Key-value storage for application settings.
 */
public class Settings implements Serializable {

    private String key;
    private String value;
    private long updatedAt;

    public Settings() {
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

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
