package com.personal.media.common.dto;

import java.io.Serializable;

public class PhotoEventMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fileId;
    private String tempLocation;
    private long size;

    public PhotoEventMsg(String fileId, String tempLocation, long size) {
        this.fileId = fileId;
        this.tempLocation = tempLocation;
        this.size = size;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getTempLocation() {
        return tempLocation;
    }

    public void setTempLocation(String tempLocation) {
        this.tempLocation = tempLocation;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
