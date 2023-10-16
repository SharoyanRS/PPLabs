import java.math.*;
import java.util.Comparator;

public class Main {

    static double h = Math.pow(10,-7);
    //2sin(x)
    static double a = 0;
    static double b = 4*Math.PI;

    public static void main(String[] args) {

        int threadCount = 14;

        for (int i = 1;i<=threadCount;i++) new Worker(a,b,h,i);

       /* while ( true ){
            if (Worker.results.size() == threadCount){
                break;
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }*/

        Worker.results.sort(Comparator.comparing(Pair::getTime));
        Worker.results.forEach(pair -> System.out.println(pair.getTCount()+" потоков выполнили за "+pair.getTime()+ " результат "+pair.getAns()));
    }
}