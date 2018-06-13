package beans;

public class Word {

    /**
     * 每个字的数据结构
     */
    private String word = "N";
    private double x = 0.0;
    private double y = 0.0;
    private double xend = 0.0;
    private double yend = 0.0;

    public Word(String word,double x,double y,double xend,double yend){
        this.word = word;
        this.x = x;
        this.y = y;
        this.xend = xend;
        this.yend = yend;
    }

    public Word(String word,float x,float xend){
        this.word = word;
        this.x = (double)x;
        this.xend = (double)xend;
    }

    public Word(String word,double x,double xend){
        this.word = word;
        this.x =x;
        this.xend = xend;
    }

    public String getWord() {
        return word;
    }

    public double getX() {
        return x;
    }

    public double getEndX() {
        return xend;
    }

}
