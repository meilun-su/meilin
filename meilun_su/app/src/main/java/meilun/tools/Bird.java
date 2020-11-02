package meilun.tools;

/**
 * 公共工具类，获取设置菜品名、菜品图片URL、菜品价格
 */
public class Bird {

    public Bird(){

    }

    private String imageUrl;        //图片URL
    private String name;            //小吃名称
    private int number;             //购买小吃数量
    private int footid;             //小吃的id
    private int groupid;             //小吃所在分类的id
    private int price;              //小吃价钱
    private String detailed;
    private String history;

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getDetailed() {
        return detailed;
    }

    public void setDetailed(String detailed) {
        this.detailed = detailed;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public int getFootid() {
        return footid;
    }

    public void setFootid(int footid) {
        this.footid = footid;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
