package threads;

public class ThreadDemo {

    public static void main(String[] args) {

        new ThreadDemo();

    }

    public ThreadDemo(){
        Slider resource = new Slider(2);
        Thread prodThread = new Thread(new Worker1(resource));
        prodThread.start();
        Thread consThread = new Thread(new Worker2(resource));
        consThread.start();
    }
}
