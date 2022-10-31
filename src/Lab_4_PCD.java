import java.util.List;
import java.util.*;
import static java.lang.Math.random;
class P_C extends Thread {
    private final Depozit depozit;
    private final String name;
    char[] array =  new char[]{'B','C','D','F','G','H','J','K','L',
             'M','N','P','Q','R','S','T','V','W','X','Y','Z'};
    P_C(Depozit depozit1, String name){
        this.depozit = depozit1;
        this.name = name;
    }
    Thread Producator = new Thread() {
        public synchronized void run() {
            while(true) {
                int elem1 = (int)(Math.random() * 21);
                int elem2 = (int)(Math.random() * 21);
                depozit.set(array[elem1],array[elem2], name);
                try {
                    sleep((int) (random() * 100));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    Thread Consumator = new Thread(){
        public synchronized void run() {
            int number_coms = 4;
            try{
                sleep((int)(Math.random()*1000));
            }catch(InterruptedException e){}
            for(int i = 0; i < number_coms; i++){
                    depozit.get(name);
                    if(i == number_coms-1){
                    System.out.print("\n"+name+" a consumat "+ number_coms + " elemente");
                    try{
                        sleep((int)(Math.random()*2000));
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    };

}
class Depozit {
    boolean plin = true;
    boolean gol = false;
    public int size_depo = 5;
    List <Character> getList = new ArrayList<Character>(size_depo);
    public synchronized void get(String name) {
        while (!plin) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n" + name + "a consumat " + getList.remove(0));
        if (getList.size() == 0) {
            plin = false;
            System.out.print("Depozitul este gol: " + getList.size() + "\n");
        }
        if (getList.size() < 5) {
            gol = false;
        }
        notifyAll();
        return ;
    }

    public synchronized void set(char t1, char t2, String name) {
        while (gol) {
            try {
                wait();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (getList.size() == 4) {
            getList.add(t1);
            System.out.println("\n" + name + " a produs " + t1);
            gol = true;
            plin = true;
            notifyAll();
        } else {
            if (getList.size() == 3) {
                getList.add(t1);
                getList.add(t2);
                System.out.println("\n" + name + " a produs " + t1 + " si " + t2);
                gol = true;
                plin = true;
                notifyAll();
            } else if (getList.size() < 4) {
                getList.add(t1);
                getList.add(t2);
                System.out.println("\n" + name + " a produs " + t1 + " si " + t2);
                gol = false;
                plin = true;
                notifyAll();
            }
        }if (getList.size() == 5) {
            System.out.println("\nDepozitul este plin!");
            System.out.println("Valoriele din depozit sunt: " + getList);
        }

    }
}


public class Lab_4_PCD {
    public static void main(String[] str) {
        Depozit depozit = new Depozit();
        Thread producer1 = new P_C(depozit, "Producator: 1 ").Producator;
        Thread producer2 = new P_C(depozit, "Producator: 2 ").Producator;
        Thread producer3 = new P_C(depozit, "Producator: 3 ").Producator;
        Thread producer4 = new P_C(depozit, "Producator: 4 ").Producator;

        Thread consumer1 = new P_C(depozit, "Consumator: 1 ").Consumator;
        Thread consumer2 = new P_C(depozit, "Consumator: 2 ").Consumator;

        producer1.start();
        producer2.start();
        producer3.start();
        producer4.start();

        consumer1.start();
        consumer2.start();

        while (true) {
            if (!consumer1.isAlive() || !consumer2.isAlive()){
                producer1.stop();
                producer2.stop();
                producer3.stop();
                producer4.stop();
                break;
            }
        }
    }
}

