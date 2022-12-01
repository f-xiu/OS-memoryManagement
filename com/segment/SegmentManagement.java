package memoryManage.com.segment;

import java.util.*;

public class SegmentManagement {
    public static final int MEMORY_SIZE = 64 * 1024;//内存空间64k
    public static final int SEG_MAXSIZE = 8 * 1024;//每个段最大8k
    public static final int MAX_SEG_NUM = 6;//每个进程最多有6个段

    ArrayList<Segment> emptyList, memoryList;
    public HashMap<String, PCB> pcbHashMap;

    public long getTime() {
        Date d = new Date();
        return d.getTime();
    }

    public SegmentManagement() {
        pcbHashMap = new HashMap<>();
        emptyList = new ArrayList<>();
        memoryList = new ArrayList<>();
        Segment seg = new Segment();
        seg.setSegmentSize(MEMORY_SIZE);
        seg.setStart(0);
        seg.setLoadInfoInit();
        emptyList.add(seg);
        memoryList.add(seg);
    }

    public void createProcess(String name, String[] strs) {
        int[] sizes = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {
            sizes[i] = Integer.parseInt(strs[i]);
        }
        boolean flag = true;
        if (sizes.length > MAX_SEG_NUM) {
            System.out.println("分配失败！每个进程段数不能超过:" + MAX_SEG_NUM);
            return;
        }
        int size = 0;
        Segment[] segments = new Segment[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            if (sizes[i] > SegmentManagement.SEG_MAXSIZE) {
                flag = false;
                System.out.println("分配失败,段长大于设置值:" + SEG_MAXSIZE);
                break;
            }
            //创建段表
            else {
                segments[i] = new Segment(-1, sizes[i]);
                segments[i].setSegmentNum(i);
                size += sizes[i];
            }
            PCB pcb = new PCB(name, size, segments);

            //默认情况下均不载入到内存
            pcbHashMap.put(name, pcb);
            System.out.println("创建成功");
        }
    }

    public void endProcesses(String[] names) {
        for (String name : names) {
            PCB pcb = pcbHashMap.getOrDefault(name, null);
            if (pcb == null) {
                System.out.println("没有该进程");
            } else {
                for (Segment s : pcb.segments) {
                    s.setLoadInfoInit();
                    Segment seg = findSeg(s);
                    if (seg != null) {
                        emptyList.add(seg);
                        seg.setLoadInfoInit();
                        recycle(seg);
                    }
                }
            }
        }
    }

    public Segment distribute(String name, Segment segment) {
        Segment ans = null;
        int size = segment.getSegmentSize();
        //分配策略同分区管理,这里简化默认为FIRST_FIT
        emptyList.sort(Comparator.comparingInt(Segment::getStart));
        boolean flag = false;
        for (Segment s : emptyList) {
            if (s.getSegmentSize() >= size) {
                flag = true;
                //移除空闲区
                emptyList.remove(s);
                s.setLoadInfo(new LoadInfo(true, name, getTime()));
                s.setSegmentNum(segment.segmentNum);
                ans = s;
                // size小于空闲区，则产生新的空闲区
                if (s.getSegmentSize() > size) {
                    Segment segment1 = new Segment(s.getStart(), size);
                    Segment segment2 = new Segment(s.getStart() + size, s.getSegmentSize() - size);
                    segment1.setLoadInfo(new LoadInfo(true, name, getTime()));
                    segment1.setSegmentNum(segment.segmentNum);

                    memoryList.remove(s);
                    emptyList.add(segment2);
                    memoryList.add(segment1);
                    memoryList.add(segment2);
                    ans = segment1;
                }
            }
        }
        if (!flag) {
            //没有可用的空闲区
            //从现有的空闲区中按照LRU策略进行
            long leastTime = System.currentTimeMillis() + 1000000;    // 设置为现在以后的时间
            Segment tm = null;
            // 遍历所有段，找到已经载入且usedTime最小的
            for (Segment seg : memoryList) {
                LoadInfo loadInfo = seg.getLoadInfo();
                if (loadInfo.getLoad() && loadInfo.getLoadTime() < leastTime) {
                    leastTime = loadInfo.getLoadTime();
                    tm = seg;
                }
            }
            if (tm == null) {
                System.out.println("分配失败");
                return null;
            }
            recycle(tm);
            //递归调用该方法
            ans = distribute(name, segment);
        }
        return ans;
    }

    public void recycle(Segment s) {
        emptyList.sort(Comparator.comparingInt(Segment::getStart));
        int i = memoryList.indexOf(s);
        Segment lastPart;
        Segment nextPart;
        if (i < emptyList.size() - 1) {
            nextPart = emptyList.get(i + 1);
            if (s.getStart() + s.getSegmentSize() == nextPart.getStart()) {
                Segment combinePart = new Segment(s.getStart(), s.getSegmentSize() + nextPart.getSegmentSize());
                combinePart.setLoadInfoInit();
                emptyList.set(i, combinePart);
                emptyList.remove(nextPart);
                memoryList.remove(s);
                memoryList.remove(nextPart);
                memoryList.add(combinePart);
            }
        }
        s = emptyList.get(i);
        if (i > 0) {
            lastPart = emptyList.get(i - 1);
            if (lastPart.getSegmentSize() + lastPart.getStart() == s.getStart()) {
                Segment combinePart = new Segment(lastPart.getStart(), s.getSegmentSize() + lastPart.getSegmentSize());
                combinePart.setLoadInfoInit();
                emptyList.set(i - 1, combinePart);
                emptyList.remove(s);
                memoryList.remove(s);
                memoryList.remove(lastPart);
                memoryList.add(combinePart);
            }
        }
    }

    public void readAddress(String name, int segNum, int displacement) {
        PCB pcb = pcbHashMap.getOrDefault(name, null);
        if (pcb == null) {
            System.out.println("错误,该进程未添加");
        } else {
            Segment readyFind = null;
            for (Segment s : memoryList) {
                if (s.loadInfo.load && Objects.equals(s.loadInfo.loadProcess, name) && s.segmentNum == segNum) {
                    readyFind = s;
                    break;
                }
            }
            if (readyFind == null) {
                //发生缺页
                System.out.println("发生缺段");
                Segment s = distribute(name, pcb.segments[segNum]);
                if (s == null) {
                    System.out.println("分配失败");
                } else {
                    readyFind = s;
                }
            }
            int size = 0;
            memoryList.sort(Comparator.comparingInt(Segment::getStart));
            for (Segment s : memoryList) {
                if (s == readyFind) {
                    break;
                }
                size += s.getSegmentSize();
            }
            System.out.println("实际物理地址为:" + (size + displacement));
        }
    }

    //越界判断
    public void showMem() {
        int num = 1;
        memoryList.sort(Comparator.comparingInt(Segment::getStart));
        for (Segment s : memoryList) {
            System.out.printf("%1d", num);
            System.out.printf("%8d\t%5d\t", s.getStart(), s.getSegmentSize());
            if (!s.getLoadInfo().getLoad()) {
                System.out.print("Empty\t");
            } else {
                System.out.print(s.getLoadInfo().getLoadProcess() + " " + s.segmentNum);
            }
            System.out.println();
            num++;
        }
    }

    public void showEmpty() {
        System.out.println("段\t开始地址\t长度\t状态\t");
        int num = 1;
        emptyList.sort(Comparator.comparingInt(Segment::getStart));
        for (Segment s : emptyList) {
            System.out.printf("%1d", num);
            System.out.printf("%8d\t%5d\t", s.getStart(), s.getSegmentSize());
            System.out.print("Empty\t");
            System.out.println();
            num++;
        }
    }

    public void showProcess(String name) {
        PCB pcb = pcbHashMap.getOrDefault(name, null);
        if (pcb == null) {
            System.out.println("没有该进程！");
        }
        System.out.println("进程" + name + "的段如下:");
        System.out.println("段号\t大小\t是否载入\t开始地址\t");
        for (Segment s : pcb.segments) {
            System.out.printf("%4d\t%6d\t", s.segmentNum, s.segmentSize);
            Segment realSeg = findSeg(s);
            if (realSeg != null) {
                System.out.printf("YES\t%6d", realSeg.start);
            } else {
                System.out.print("NO\t");
            }
            System.out.println();
        }
    }

    public Segment findSeg(Segment s) {
        Segment ans = null;
        for (Segment seg : memoryList) {
            if (s.segmentNum == seg.segmentNum) {
                ans = seg;
                break;
            }
        }
        return ans;
    }
}
