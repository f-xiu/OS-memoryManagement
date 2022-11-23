package memoryManage.com.segment_page;

/* Segment Table Entry */
public class SegmentEntry {
    public int segmentNum = -1;            // �κ�
    public int segmentSize = -1;        // �δ�С
    public PageEntry[] PTable = null;            // ��Ӧ��ҳ��

    public SegmentEntry(int segmentNum, int segmentSize) {
        this.segmentNum = segmentNum;
        this.segmentSize = segmentSize;

        // ҳ��Ĵ�С
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
