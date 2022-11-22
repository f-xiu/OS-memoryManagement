package memoryManage.com.partition;

public class Partition {

	// 记录分区开始地址
	private int start;
	// 分区大小
	private int size;
	// 分区的状态，也即被哪一进程所使用
	private String owner;

	public Partition(int start, int size) {
		this.start = start;
		this.size = size;
		owner = "Empty";
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getStart() {
		return start;
	}

	public int getSize() {
		return size;
	}

	@Override
	public String toString() {
		return "开始地址:" + start + " 长度:" + size + " 状态:" + owner;
	}

}
