package threads;

public class Slider {

    private int antalBurgere;
    private final int MAX = 8;

    public Slider(int startvalue){
        antalBurgere = startvalue;
    }

    public int getAntalBurgere(){
        return antalBurgere;
    }

    public synchronized int addOne(){
        //producer én, men kun op til max
        try{

            while(antalBurgere >= MAX){
                System.out.println("Max burgere i sliden!");
                wait(); // does release the lock on this object, allowing consumer access
            }
            antalBurgere++;
            notifyAll();
        }catch (InterruptedException e){

            e.printStackTrace();
        }
        return antalBurgere;
    }

    public synchronized int takeOne(){

        try{

            while(antalBurgere == 0){
                System.out.println("Ingen burgere i sliden!");
                wait();
            }

            antalBurgere--;
            notifyAll();
        }catch (InterruptedException e){

            e.printStackTrace();
        }
        return antalBurgere;
    }
}


