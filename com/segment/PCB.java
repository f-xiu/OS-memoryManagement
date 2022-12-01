package memoryManage.com.segment;

import memoryManage.com.Process;

public class PCB extends Process {

    Segment[]segments;

    public PCB(String id,int size,Segment[]segments) {
        super(id,size);
        this.segments=segments;
    }

}
