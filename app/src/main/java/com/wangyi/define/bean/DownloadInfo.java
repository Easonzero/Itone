package com.wangyi.define.bean;

import com.wangyi.define.EventName;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by maxchanglove on 2016/2/24.
 */
@Table(name = "download", onCreated = "CREATE UNIQUE INDEX index_name ON download(label,fileSavePath)")
public class DownloadInfo {

    public DownloadInfo() {
    }

    @Column(name = "id", isId = true)
    private long id;

    @Column(name = "uid")
    private String uid;

    @Column(name = "state")
    private int state = EventName.Download.STOPPED;

    @Column(name = "url")
    private String url;

    @Column(name = "label")
    private String label;

    @Column(name = "fileSavePath")
    private String fileSavePath;

    @Column(name = "progress")
    private int progress = 0;

    @Column(name = "fileLength")
    private long fileLength;

    @Column(name = "autoResume")
    private boolean autoResume;

    @Column(name = "autoRename")
    private boolean autoRename;

    private boolean select = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public boolean isAutoResume() {
        return autoResume;
    }

    public void setAutoResume(boolean autoResume) {
        this.autoResume = autoResume;
    }

    public boolean isAutoRename() {
        return autoRename;
    }

    public void setAutoRename(boolean autoRename) {
        this.autoRename = autoRename;
    }

    public void setSelect(boolean select){
        this.select = select;
    }

    public void setSelect(){
        select = !select;
    }

    public boolean getSelect(){
        return select;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DownloadInfo)) return false;

        DownloadInfo that = (DownloadInfo) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
