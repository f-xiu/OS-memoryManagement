package memoryManage.com;

import java.util.Scanner;

/**
 * ���ж��Scanner����ʱ������close()��������˳��ر�System.in�����������������Scanner����ķ����������java.util.NoSuchElementException�쳣<br/>
 * <p>
 * �����Ƕ�Scanner�ļ򵥷�װ������ͳһ��ȡ����͹ر�Scanner����
 */
public class Input {
    private static Scanner input = new Scanner(System.in);

    public static void close() {
        input.close();
    }

    public static String nextLine() {
        return input.nextLine();
    }
}
