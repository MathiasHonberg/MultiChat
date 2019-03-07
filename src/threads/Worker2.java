package threads;

import java.util.Random;

public class Worker2 implements Runnable{

        private Slider resource;

        public Worker2(Slider res){

            resource = res;
        }
    @Override
    public void run() {

        int newLevel=0;

        while(true){

            newLevel = resource.addOne(); // sover muligvis i Res objektet
            System.out.println("Producer: "+newLevel);
            int pause = (int)(Math.random()*2000);
            try
            {
                Thread.sleep(pause);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }
}
