package tmp;

/**
 * Created by coral on 2017/7/17.
 */
public class TestThread extends Thread {

    Syc s = new Syc();

    public static void main(String args[]) {
        for (int i = 0; i < 4; i++) {
            Thread tt = new TestThread();
            tt.start();
        }

    }

    public void run() {

        s.print();
    }

}

class Syc {
    static Integer index = 0;
    String k = "33";

    public void print() {
//        synchronized (k) {
            synchronized (index){
            System.out.println(index);
            try {
                Thread.sleep(10);//insure thread execute in turn
            } catch (Exception e) {
                e.printStackTrace();
            }
            index++;
            System.out.println(index);
        }
    }
}
