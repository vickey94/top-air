package air.config;

import org.aopalliance.reflect.Class;

/**
 * Created by vickey on 2016/7/20.
 * 日志报表
 */
public class LOG {

    public static int total = 0;           //导入文件总数

    public static int opCar_N_Data = 0;    //非实际航班


    public static int opCar_O_Data = 0;              //实际航班数
    public static int actTrianData = 0;              //实际训练航班数

    //   private int unusual_Data ;              //异常数据
    public static int unusual_Comment = 0;           //状态异常 "取消","未知","已備降","已排班","n/a","正在滑行","途中"
    public static int unusual_ActDepTime = 0;        //提前起飞太早
    public static int unusual_DistKm_ActFlyTime = 0; //飞行速度过快
    public static int unusual_ActFlyTime = 0;        //实际飞行时间为0
    //   private int incomplete_Data ;                //数据不完整
    public static int miss_Weather = 0;              //缺失两地天气
    public static int miss_DepWeather = 0;            //缺失出发城市天气
    public static int miss_ArrWeather = 0;            //缺失到达城市天气
    public static int have_AdvFlight = 0;            //有前序航班

    public static int miss_ActTime = 0; //缺少实际出发时间，可能为测试数据


    public String toString() {
        return "文件总数为：" + total + "\n"
                + "非实际航班：" + opCar_N_Data + "\n"
                + "实际航班：" + opCar_O_Data + "\n"
                + "----------------------------------\n"
                + "实际航班中实际训练航班数为：" + actTrianData + "\n"
                + "有前序航班的航班数为：" + have_AdvFlight + "\n"
                + "缺失两地天气航班数为：" + miss_Weather + "\n"
                + "缺失出发城市天气航班数为：" + miss_DepWeather + "\n"
                + "缺失到达城市天气航班数为：" + miss_ArrWeather + "\n"
                + "----------------------------------\n"
                + "状态异常航班数为：" + unusual_Comment + "\n"
                + "提前起飞太早航班数为：" + unusual_ActDepTime + "\n"
                + "飞行速度异常航班数为：" + unusual_DistKm_ActFlyTime + "\n"
                + "实际飞行时间为0航班数为：" + unusual_ActFlyTime + "\n"
                + "----------------------------------\n"
                + "缺少实际出发时间航班数为：" + miss_ActTime + "\n"
                ;
    }
}

