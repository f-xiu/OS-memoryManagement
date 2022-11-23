package memoryManage.com;

public class Process {
	// 进程所用空间的大小
	protected String id;
	protected int size;

	public Process(String id){
		this.id=id;
		this.size=0;
	}
	public Process(String id, int size) {
		this.id=id;
		this.size = size;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public synchronized int getSize() {
		return size;
	}

}
