package top.byteinfo.base;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-28 13:33:00
 */
public class BaseTest {
    public static final List<String> stringList = new ArrayList<>();

    public static void main(String[] args) {
        stringList.add("1");
        stringList.add("11");
        stringList.add("111");


//        stringList = new ArrayList<>();


        O1 o1 = new O1();

        System.out.println(stringList);
    }
}
