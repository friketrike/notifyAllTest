package notifyAllTest;

public class NotifyAllTestDriver {

	public static void main(String[] args) {
		final int NUM_THREADS = 3;
		Thread ta[] = new Thread[NUM_THREADS]; 
		for(int i = 0; i < NUM_THREADS; i++){
			ta[i] = new Thread(new NotifyAllThread(), String.format("T %d", i));
			ta[i].start();
		}
		System.out.println("Main thread waiting on children...");
		for(int i = 0; i < NUM_THREADS; i++){
			try{
				ta[i].join();
			}catch(InterruptedException e){}
		}
		System.out.println("Done");
	}

}
