package lu.pata.fsync.api;

public class DataRequest {
    private String filename;
    private long offset;
    private long length;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
