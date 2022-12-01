package memoryManage.com.segment;

public class LoadInfo {
    public boolean load = false;

    public String loadProcess;

    public long loadTime;

    public LoadInfo(boolean load, String loadProcess, long loadTime) {
        this.load = load;
        this.loadProcess = loadProcess;
        this.loadTime = loadTime;
    }

    public boolean getLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    public String getLoadProcess() {
        return loadProcess;
    }

    public void setLoadProcess(String loadProcess) {
        this.loadProcess = loadProcess;
    }

    public long getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(long loadTime) {
        this.loadTime = loadTime;
    }
}
