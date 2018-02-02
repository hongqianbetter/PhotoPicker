package net.hongqianfly.photopicker;

/**
 * Created by HongQian.Wang on 2018/2/1.
 */

public class Image {
    private long _id;
    private String path;
    private long dateTime;
    private long size;

    public Image(long _id, String path, long dateTime, long size) {
        this._id = _id;
        this.path = path;
        this.dateTime = dateTime;
        this.size = size;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Image{" +
                "_id=" + _id +
                ", path='" + path + '\'' +
                ", dateTime=" + dateTime +
                ", size=" + size +
                '}';
    }
}
