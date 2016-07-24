package air.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vickey on 2016/7/23.
 * 临时存储数据，用于导出Excel
 */
public class Temp {

    //训练数据
    public static List<String> list_trainData = new ArrayList<>();
    //归一化训练数据
    public static List<String> list_canData = new ArrayList<>();

    //非实际航班
    public static List<String> list_opCar_N_Data = new ArrayList<>();

    //异常数据
    public static List<String> list_unusual_Data = new ArrayList<>();

    //训练数据中的数据不全
    public static List<String> list_incomplete_Data = new ArrayList<>();

}
