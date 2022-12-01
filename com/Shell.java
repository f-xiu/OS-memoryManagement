package memoryManage.com;

import memoryManage.com.partition.DynamicPart;
import memoryManage.com.partition.Info;
import memoryManage.com.segment.SegmentManagement;
import memoryManage.com.segment_page.Settings;

import java.util.Objects;

public class Shell {
    public static final String helpMess =
            "create process 进程id 各个段大小\t创建一个进程\n" +
                    "destroy process 进程id\t\t销毁一个进程\n" +
                    "show memory\t\t\t显示内存使用情况\n" +
                    "show process 进程id\t\t显示该进程驻留集、置换策略、段表、页表\n" +
                    "address 进程名 段号 段偏移\t\t将逻辑地址映射为物理地址\n" +
                    "help or h\t\t\t获取帮助\n" +
                    "quit or q\t\t\t退出\n";

    public static Settings settings;

    public static void main(String[] args) {
        System.out.println("选择内存管理方案:\n1.动态分区管理\n2.段式内存管理\n3.段页式内存管理");
        System.out.print(">>> ");
        int command = Integer.parseInt(Input.nextLine());
        if (command == 1) {
            partitionShell();
        } else if (command == 2) {
            segmentShell();
        } else if (command == 3) {
            printMessage();
            setReplacePolicy();
            System.out.println("输入help获取更多帮助信息");
            segPageShell();
        }
        Input.close();

    }
    /**
     * 1.show process
     * 2.add process pname size
     * 3.destroy process pname1 pname2...
     * 4.execute process pname1 pnam2...
     * 5.show empty
     * 6.show memory
     * 7.help or h
     * 8.quit or q
     */

    /*
     *create process pname segments
     *destroy process pname
     *address pname segment offset
     *show memory
     *hlep
     *  */


    /**
     * 1. create process pname segments
     * 2. destroy process pname
     * 3. show memory
     * 4. show process pname
     * 5. help or h
     * 6. quit or q
     * 8. address pname sgementNum segmentOffset
     */
    public static void partitionShell() {
        DynamicPart test1 = new DynamicPart();
        test1.readFile("test/processes.txt");
        test1.init("com/partition/partition.txt");
        //指令
        int comm = -1;

        System.out.print(Info.PART_SELECT_ALGORITHM_MENU);
        System.out.print(">>> ");
        comm = Integer.parseInt(Input.nextLine());
        test1.setAlgorithmKind(comm);

        System.out.println("输入help获取更多帮助信息");
        System.out.print(">>> ");
        while (true) {
            String command = Input.nextLine();
            if (command == null || command.trim().equals("")) {

                System.out.print(">>> ");
                continue;
            }
            command = command.trim();
            String[] words = command.split(" ");

            try {
                if (words.length >= 3) {
                    if ("add".equals(words[0]) && "process".equals(words[1]) && words.length == 4) {
                        test1.addProcess(words[2], Integer.parseInt(words[3]));
                    } else {
                        String[] strs = new String[words.length - 2];
                        System.arraycopy(words, 2, strs, 0, words.length - 2);

                        if ("execute".equals(words[0]) && "process".equals(words[1])) {
                            test1.executeProcesses(strs);
                        } else if ("destroy".equals(words[0]) && "process".equals(words[1])) {
                            test1.endProcesses(strs);
                        } else {
                            throw new Exception();
                        }
                    }
                } else if (words.length == 2) {
                    if ("show".equals(words[0])) {
                        switch (words[1]) {
                            case "process":
                                test1.outReady();
                                break;
                            case "empty":
                                test1.showEmpty();
                                break;
                            case "memory":
                                test1.showMem();
                                break;
                            default:
                                break;
                        }
                    } else {
                        throw new Exception();
                    }
                } else if (words.length == 1) {
                    if ("help".equals(words[0]) || "h".equals(words[0])) {
                        System.out.println("1.show process\n"
                                + "2.add process pname size\n"
                                + "3.destroy process pname1 pname2...\n"
                                + "4.execute process pname1 pnam2...\n"
                                + "5.show empty\n"
                                + "6.show memory\n"
                                + "7.help or h\n"
                                + "8.quit or q"
                        );
                    } else if ("quit".equals(words[0]) || "q".equals(words[0])) {
                        break;
                    } else {
                        throw new Exception();
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("命令有误(help获取帮助)");
            }
            System.out.print(">>> ");
        }
    }


    public static void segmentShell() {
        SegmentManagement segmentManagement = new SegmentManagement();
        System.out.print(">>> ");
        while (true) {
            String command = Input.nextLine();
            if (command == null || "".equals(command.trim())) {
                System.out.print(">>> ");
                continue;
            }


            String[] words = command.split(" ");

            try {
                if (words.length >= 3) {
                    if ("create".equals(words[0]) && "process".equals(words[1])) {
                        String[] strs = new String[words.length - 3];
                        System.arraycopy(words, 3, strs, 0, words.length - 3);

                        segmentManagement.createProcess(words[2], strs);
                    } else if ("destroy".equals(words[0]) && "process".equals(words[1])) {
                        String[] strs = new String[words.length - 2];
                        System.arraycopy(words, 2, strs, 0, words.length - 2);

                        segmentManagement.endProcesses(strs);
                    } else if ("address".equals(words[0])) {
                        if (words.length != 4) {
                            throw new Exception();
                        } else {
                            segmentManagement.readAddress(words[1], Integer.parseInt(words[2]), Integer.parseInt(words[3]));
                        }
                    } else if ("show".equals(words[0]) && "process".equals(words[1])) {
                        segmentManagement.showProcess(words[2]);
                    } else {
                        throw new Exception();
                    }
                } else if (words.length == 2) {
                    if ("show".equals(words[0]) && "memory".equals(words[1])) {
                        System.out.println("段\t开始地址\t长度\t状态\t");
                        segmentManagement.showMem();
                    } else if ("show".equals(words[0]) && "empty".equals(words[1])) {
                        segmentManagement.showEmpty();
                    } else {
                        throw new Exception();
                    }
                } else if (words.length == 1) {
                    if ("help".equals(words[0]) || "h".equals(words[0])) {
                        System.out.println(
                                "create process pname segments\n" +
                                        "destroy process pname1 pname2.. \n" +
                                        "address pname segment offset\n" +
                                        "show memory\n" +
                                        "hlep");
                    } else if ("quit".equals(words[0]) || "q".equals(words[0])) {
                        break;
                    } else {
                        throw new Exception();
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("命令有误(help获取帮助)");

            }
            System.out.print(">>> ");
        }

    }

    public static void segPageShell() {
        settings = new Settings();
        System.out.print(">>> ");
        while (true) {
            String command = Input.nextLine();
            if (command == null || command.trim().equals("")) {
                System.out.print(">>> ");
                continue;
            }


            String[] words = command.split(" ");


            if (words.length >= 4 &&
                    "create".equals(words[0].trim()) &&
                    "process".equals(words[1].trim())) {
                createProcesses(words);

            } else if (words.length == 3 &&
                    "destroy".equals(words[0].trim()) &&
                    "process".equals(words[1].trim())) {
                String processId = words[2].trim();
                settings.destroyProcess(processId);
            } else if (words.length == 2 &&
                    "show".equals(words[0].trim()) &&
                    "memory".equals(words[1].trim())) {
                settings.showMemory();
            } else if (words.length == 3 &&
                    "show".equals(words[0].trim()) &&
                    "process".equals(words[1].trim())) {
                String processId = words[2].trim();
                settings.showProcess(processId);

            } else if (words.length == 1 &&
                    "help".equals(words[0].trim()) ||
                    "h".equals(words[0].trim())) {
                System.out.println(helpMess);
            } else if (words.length == 1 &&
                    "quit".equals(words[0].trim())
                    || "q".equals(words[0].trim())) {
                System.out.println("quit");
                break;
            } else if (words.length == 4 && "address".equals(words[0].trim())) {
                addressToPhysical(words);
            } else {
                System.out.println("命令有误(help获取帮助)");
            }

            System.out.print(">>> ");
        }
    }


    public static void createProcesses(String[] words) {
        String processId = words[2].trim();
        int[] segments = new int[words.length - 3];
        try {
            for (int i = 3, index = 0; i < words.length; i++, index++) {
                segments[index] = Integer.parseInt(words[i]);
                if (segments[index] <= 0) {
                    throw new Exception();
                }
            }
        } catch (Exception ex) {
            System.out.println("命令有误 段大小必须为正整数(help获取帮助)");
            System.out.print(">>> ");
        }
        settings.createProcess(processId, segments);
    }

    public static void addressToPhysical(String[] words) {
        String porcessId = words[1].trim();
        int segmentNum = 0, segmentOffset = 0;
        try {
            segmentNum = Integer.parseInt(words[2].trim());
            segmentOffset = Integer.parseInt(words[3].trim());
            if (segmentNum < 0 || segmentOffset < 0) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("命令有误 段号和段偏移必须为正整数(help获取帮助)");
            System.out.print(">>> ");
        }
        settings.toPhysicalAddress(porcessId, segmentNum, segmentOffset);
    }

    /**
     * 设置默认置换策略
     */
    public static void setReplacePolicy() {
        System.out.print(">>> 请设置置换策略(0为FIFO 1为LRU): ");
        while (true) {
            String mess = Input.nextLine().trim();
            if ("0".equals(mess)) {
                Settings.setReplacePolicy(Settings.REPLACE_POLICY.FIFO);
                System.out.println("设置置换策略为FIFO");
                break;
            } else if ("1".equals(mess)) {
                Settings.setReplacePolicy(Settings.REPLACE_POLICY.LRU);
                System.out.println("设置置换策略为LRU");
                break;
            } else {
                System.out.print(">>> 输入有误，请设置置换策略(0为FIFO 1为LRU): ");
            }
        }
    }

    /**
     * 打印一些必要信息
     */
    public static void printMessage() {
        System.out.println("内存大小64K，页框大小为1K，一个进程最多有4个段，且每个段最大为16K。一个进程驻留集最多为8页。");
        System.out.println("固定分配局部置换");
        System.out.println("页面淘汰策略：FIFO、LRU");
        System.out.println();
    }

}

