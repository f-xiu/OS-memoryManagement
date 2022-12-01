package memoryManage.com.segment;

import memoryManage.com.segment_page.PageEntry;

public class Segment {
    public int segmentNum = -1;         // ∂Œ∫≈
    public int segmentSize = -1;        // ∂Œ¥Û–°


    public int start = -1;                //  º÷∑

    public LoadInfo loadInfo;

    public void setLoadInfoInit() {
        loadInfo = new LoadInfo(false, "", -1);
    }

    public Segment() {
    }

    public LoadInfo getLoadInfo() {
        return loadInfo;
    }

    public void setLoadInfo(LoadInfo loadInfo) {
        this.loadInfo = loadInfo;
    }

    public Segment(int start, int segmentSize) {
        this.start = start;
        this.segmentSize = segmentSize;
        setLoadInfoInit();
    }

    public int getSegmentNum() {
        return segmentNum;
    }

    public void setSegmentNum(int segmentNum) {
        this.segmentNum = segmentNum;
    }

    public int getSegmentSize() {
        return segmentSize;
    }

    public void setSegmentSize(int segmentSize) {
        this.segmentSize = segmentSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
