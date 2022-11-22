package memoryManage.com;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author 28597
 */
public class Processes {
    public HashMap<String, Process> getRequestList() {
        return requestList;
    }

    public void setRequestList(HashMap<String, Process> requestList) {
        this.requestList = requestList;
    }



    public Processes(){
        this.requestList=new HashMap<>();
    }
    protected HashMap<String, Process> requestList;

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


}
