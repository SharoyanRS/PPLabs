import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

public class Worker{
    double[] answers;
    int intervalsCount;
    int incCounter;

    static LinkedList<Pair> results = new LinkedList<>();

    public Worker(double a, double b, double h, int intervalsCount){
        long startTime = System.currentTimeMillis();

        incCounter = 0;
        this.intervalsCount = intervalsCount;
        answers = new double[intervalsCount];


        double step = (b-a)/intervalsCount;
        double start = a;
        double end = start+step;


        for (int i = 0; i< intervalsCount;i++){
            int finalI = i;
            double finalStart = start;
            double finalEnd = end;
            new Thread(()->{ this.calc(finalStart, finalEnd,h,finalI);} ).start();
            //System.out.println("start "+finalStart + " end "+finalEnd);
            start = end;
            end += step;
        }

        while(true){
            if (incCounter>=intervalsCount){
                break;
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        results.add(new Pair(intervalsCount,System.currentTimeMillis()-startTime,Arrays.stream(answers).sum()));
        //System.out.println(intervalsCount+" потоков посчитали за "+ (System.currentTimeMillis()-startTime)+" резульат "+Arrays.stream(answers).sum());

        //System.out.println("ans count = "+answers.length);

    }

    void calc(double a, double b, double h, int index){
        double res = 0;
        int N = (int)((b-a)/h);
        while (N>0){
            res += 2*Math.sin(a+N*h);
            N--;
        }
        res = (h/2) * (Math.sin(a)+Math.sin(b)+res);
        //res *= h * 0.5 * (Math.sin(a)+Math.sin(b));
        answers[index]=res;
        Inc();
    }

    synchronized void Inc(){
        incCounter++;
    }

}
