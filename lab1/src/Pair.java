public class Pair {
    private long time;
    private double ans;
    private int n;
    public Pair(int n,long time,double ans){
        this.time=time;
        this.ans=ans;
        this.n=n;
    }
    public long getTime(){return this.time;}
    public double getAns(){return this.ans;}
    public int getTCount(){return this.n;}
}
