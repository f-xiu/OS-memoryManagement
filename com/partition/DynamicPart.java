package memoryManage.com.partition;

import memoryManage.com.Process;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * @author 28597
 */
public class DynamicPart {


    // 使用Partition数组来表示空闲分区队列和内存总的分区队列
    protected ArrayList<Partition> emptyList, memoryList;
    protected HashMap<String, Process> requestList;
    private int algorithmKind = 0;

    public int getAlgorithmKind() {
        return algorithmKind;
    }

    public void setAlgorithmKind(int algorithmKind) {
        this.algorithmKind = algorithmKind;
    }


    Comparator<Partition> compStartAsc = Comparator.comparingInt(Partition::getStart);
    Comparator<Partition> compSizeAsc = Comparator.comparingInt(Partition::getSize);
    Comparator<Partition> compSizeDesc = new Comparator<Partition>() {
        @Override
        public int compare(Partition o1, Partition o2) {
            return o2.getSize() - o1.getSize();
        }
    };


    public DynamicPart() {
        memoryList = new ArrayList<>();
        emptyList = new ArrayList<>();
        this.requestList = new HashMap<>();
    }

    public void init(String filename) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            int initSize=Integer.parseInt(bufferedReader.readLine());
            Partition temp = new Partition(0, initSize);
            emptyList.add(temp);
            memoryList.add(temp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeProcesses(String[] strs){
        for(String str:strs){
            Partition select = selectPartition(requestList.get(str).getSize());
            if (select == null) {
                System.out.println(str+"分配失败！退出!");
            } else {
                distribute(str, select);
                System.out.println(str+"分配成功！");
            }
        }
        showEmpty();
        showMem();
    }
    public void endProcesses(String[] strs){
        for(String str:strs){
            end(str);
        }
        showEmpty();
        showMem();
    }


    public Partition selectPartition(int size) {
        ArrayList<Partition> copyEmptyList = new ArrayList<>(emptyList);
        Partition partition = null;
        switch (algorithmKind) {
            //first
            case 1:
                copyEmptyList.sort(compStartAsc);
                for (Partition p : copyEmptyList) {
                    if (p.getSize() >= size) {
                        partition = p;
                        break;
                    }
                }
                break;
            //best
            case 2:
                copyEmptyList.sort(compSizeAsc);
                for (Partition p : copyEmptyList) {
                    if (p.getSize() >= size) {
                        partition = p;
                        break;
                    }
                }
                break;
            //worst
            case 3:
                copyEmptyList.sort(compSizeDesc);
                if (copyEmptyList.get(0).getSize() >= size) {
                    partition=copyEmptyList.get(0);
                }
                break;
            default:
                break;
        }
        return partition;
    }

    public void distribute(String id, Partition part) {
        //移除空闲区
        emptyList.remove(part);
        int size = requestList.get(id).getSize();
        part.setOwner(id);
        // size小于空闲区，则产生新的空闲区
        if (size < part.getSize()) {
            Partition part1 = new Partition(part.getStart(), size);
            Partition part2 = new Partition(part.getStart() + size, part.getSize() - size);
            part1.setOwner(id);
            memoryList.remove(part);
            emptyList.add(part2);
            memoryList.add(part1);
            memoryList.add(part2);
        }
        requestList.remove(id);
    }


    public void end(String id) {
        // memoryList始终是按照起始地址升序排列
        memoryList.sort(compStartAsc);
        // 遍历memoryList,找到要完成的进程，置为Empty，加入emptyList，requestList
        for (Partition part : memoryList) {
            if (part.getOwner().equals(id)) {
                part.setOwner("Empty");
                emptyList.add(part);
                requestList.put(id,new Process(id,part.getSize()));
                recycle(part);
                break;
            }
        }
    }

    //每次判断释放空间分区的上下是否可以合并
    public void recycle(Partition part) {
        emptyList.sort(compStartAsc);
        int i=emptyList.indexOf(part);
        Partition lastPart;
        Partition nextPart;
        if(i<emptyList.size()-1){
            nextPart=emptyList.get(i+1);
            if(part.getStart()+ part.getSize()==nextPart.getStart()){
                Partition combinePart=new Partition(part.getStart(),part.getSize()+nextPart.getSize());
                emptyList.set(i,combinePart);
                emptyList.remove(nextPart);
                memoryList.remove(part);
                memoryList.remove(nextPart);
                memoryList.add(combinePart);
            }
        }
        part=emptyList.get(i);
        if(i>0){
            lastPart=emptyList.get(i-1);
            if(lastPart.getSize()+lastPart.getStart()==part.getStart()){
                Partition combinePart=new Partition(lastPart.getStart(),part.getSize()+lastPart.getSize());
                emptyList.set(i-1,combinePart);
                emptyList.remove(part);
                memoryList.remove(part);
                memoryList.remove(lastPart);
                memoryList.add(combinePart);
            }
        }
    }

    public void outReady(){
        System.out.println("可选择的进程如下:");
        int i = 1;
        for (Map.Entry<String, Process> entry : requestList.entrySet()) {
            Process p = entry.getValue();
            System.out.println(p.getId() + ":" + p.getSize() + "kb");
            i++;
        }
    }

    public void outRunning(){
        System.out.println("可选择的进程如下:");
        for(Partition p:memoryList){
            if(!"Empty".equals(p.getOwner())){
                System.out.print(p.getOwner()+":");
                System.out.println("开始地址:"+p.getStart()+" 长度:"+p.getSize());
            }
        }
    }

    public void addProcess(String name,Integer size){
        if (requestList.containsKey(name)) {
            System.out.println("进程名重复，添加失败");
        } else {
            System.out.println("添加成功");
            requestList.put(name, new Process(name, size));
        }
    }
    public void showMem() {
        memoryList.sort(compStartAsc);
        System.out.println("内存情况");
        for (Partition partition : memoryList) {
            System.out.println(partition);
        }
    }

    public void showEmpty() {
        emptyList.sort(compStartAsc);
        System.out.println("空闲列表");
        for (Partition part : emptyList) {
            System.out.println(part);
        }
    }

    public void input() {
        Scanner input = new Scanner(System.in);
        System.out.print("请输入进程数：");
        int amount = input.nextInt();
        for (int i = 0; i < amount; i++) {

            System.out.print("请输入进程名：");
            String str1 = input.next();
            System.out.print("请输入进程大小：");
            int size = input.nextInt();
            requestList.put(str1, new Process(str1, size));
        }
    }

    public void readFile(String filename) {

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            int amount = Integer.parseInt(bufferedReader.readLine());
            for (int i = 0; i < amount; i++) {
                String str = bufferedReader.readLine();
                String[] strs = str.split(" ");
                requestList.put(strs[0], new Process(strs[0], Integer.parseInt(strs[1])));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public ArrayList<Partition> getEmptyList() {
        return emptyList;
    }

    public void setEmptyList(ArrayList<Partition> emptyList) {
        this.emptyList = emptyList;
    }

    public ArrayList<Partition> getMemoryList() {
        return memoryList;
    }

    public void setMemoryList(ArrayList<Partition> memoryList) {
        this.memoryList = memoryList;
    }

    public HashMap<String, Process> getRequestList() {
        return requestList;
    }

    public void setRequestList(HashMap<String, Process> requestList) {
        this.requestList = requestList;
    }

}

