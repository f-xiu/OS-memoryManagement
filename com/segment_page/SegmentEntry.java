package memoryManage.com.segment_page;

/* Segment Table Entry */
public class SegmentEntry {
    public int segmentNum = -1;            // 段号
    public int segmentSize = -1;        // 段大小
    public PageEntry[] PTable = null;            // 对应的页表

    public SegmentEntry(int segmentNum, int segmentSize) {
        this.segmentNum = segmentNum;
        this.segmentSize = segmentSize;

        // 页表的大小
        int count = segmentSize / Settings.pageSize;
        if (segmentSize % Settings.pageSize != 0) {
            count++;
        }
        PTable = new PageEntry[count];
        for (int i = 0; i < count; i++) {
            PTable[i] = new PageEntry(i);
        }
    }
}
