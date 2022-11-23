package memoryManage.com.segment_page;

/**
 * @author 28597
 */ /* Page Table Entry */
public class PageEntry {
    public int pageNum;                    // ҳ��
    public boolean load;                // ��ҳ�Ƿ�����
    public int frameNum;                // ��ҳ�����ҳ��š����loadΪfalse������ֶ�������
    /**
     * ��ҳ���һ�α����ʵ�ʱ�䡣���loadΪfalse������ֶ������塣
     * ���ֶ�����ʵ��ҳ���û�����LRU������ҳ�������ڴ�򱻷���ʱ�����ø�ʱ��
     */
    public long usedTime;

    /**
     * ����һ��ָ��ҳ�š�δ�����ҳ
     */
    public PageEntry(int pageNum) {
        this.pageNum = pageNum;
        setUnload();
    }

    /**
     * ���ø�ҳ���뵽ҳ���ΪframeNum��ҳ����
     */
    public void setLoad(int frameNum) {
        this.load = true;
        this.frameNum = frameNum;
        usedTime = System.currentTimeMillis();
    }

    /**
     * ����ҳ�س��ڴ�
     */
    public void setUnload() {
        this.load = false;
        this.frameNum = -1;
        usedTime = -1;
    }
}
