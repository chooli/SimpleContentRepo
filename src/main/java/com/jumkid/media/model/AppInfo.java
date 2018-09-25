package com.jumkid.media.model;

import java.util.Date;

/**
 * Created at Sep2018$
 *
 * @author chooliyip
 **/
public class AppInfo {

    private String name;

    private String version;

    private String lastBuild;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLastBuild() {
        return lastBuild;
    }

    public void setLastBuild(Date lastUpdated) {
        this.lastBuild = lastBuild;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
