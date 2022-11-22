package memoryManage.test;

import memoryManage.com.Processes;
import memoryManage.com.Info;
import memoryManage.com.partition.DynamicPart;

import java.util.Scanner;

public class PartitionTest {


    public static void main(String[] args) {
        Processes processes=new Processes();
        Scanner input = new Scanner(System.in);
//        System.out.print("请设置内存大小：");
//        int initSize = input.nextInt();
        processes.readFile("test/processes.txt");

//        //输入进程
//        processes.input();

        DynamicPart test1 = new DynamicPart(processes.getRequestList());
        test1.init("com/partition/partition.txt");
        //指令
        int command = -1;
        while (command != 0) {
            System.out.println(Info.PART_SELECT_ALGORITHM_MENU);
            command = input.nextInt();
            test1.setAlgorithmKind(command);
            test1.run();
        }
        input.close();
    }
}


