package top.byteinfo.base;

/**
 * @author ieeiec@outlook.com
 * @Description TODO
 * @createTime 2022-06-28 13:35:00
 */
public class O1 {
    private final String os1 = "os1-1";
    private String os11 = "os1-11";
    private String os111 = "os1-111";

    public O1() {

    }

    public String getOs1() {
        return os1;
    }

    public String getOs11() {
        return os11;
    }

    public String getOs111() {
        return os111;
    }

    public void setOs11(String os11) {
        this.os11 = os11;
    }


    public void setOs111(String os111) {
        this.os111 = os111;
    }

    public O1(String os11, String os111) {
        this.os11 = os11;
        this.os111 = os111;
    }
}
