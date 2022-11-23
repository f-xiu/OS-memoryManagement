package memoryManage.com.segment_page;

/**
 * @author 28597
 */ /* Page Table Entry */
public class PageEntry {
    public int pageNum;                    // 页号
    public boolean load;                // 该页是否载入
    public int frameNum;                // 该页载入的页框号。如果load为false，则该字段无意义
    /**
     * 该页最近一次被访问的时间。如果load为false，则此字段无意义。
     * 该字段用于实现页面置换策略LRU，当该页被载入内存或被访问时，重置该时间
     */
    public long usedTime;

    /**
     * 创建一个指定页号、未载入的页
     */
    public PageEntry(int pageNum) {
        this.pageNum = pageNum;
        setUnload();
    }

    /**
     * 设置该页载入到页框号为frameNum的页框中
     */
    public void setLoad(int frameNum) {
        this.load = true;
        this.frameNum = frameNum;
        usedTime = System.currentTimeMillis();
    }

    /**
     * 将该页载出内存
     */
    public void setUnload() {
        this.load = false;
        this.frameNum = -1;
        usedTime = -1;
    }
}
