package notifyAllTest;

public class NotifyAllThread implements Runnable {
	protected static enum Info {CREATE, CHECKITOUT, WASTE1, NOTIFYING, NOTIFIED, WAIT, CHANGE};
	protected MyMonitorTestAll myMon = new MyMonitorTestAll();
	protected static boolean someBool = true;
	@Override
	public void run() {
		// Thread 0 goes straight to wait in a while loop with a boolean=t that won't let it pass
		// Thread 1 waits and upon being notified, sets boolean=f (atomically) stalls 1 sec then notifyAll()
		// Thread 2 stalls 1 second then notifiesAll then waits
		print(Info.CREATE, 12);
		if (Thread.currentThread().getName().contains("2")){
			print(Info.WASTE1, 14);
			wasteTime(1000); // avoid sleep, drink coffee, as sleep is another brand of yielding. Active wait function
			print(Info.NOTIFYING, 16);
			myMon.myNotifyAll();
			print(Info.CHECKITOUT, 18);
			print(Info.WAIT, 19);
			myMon.myWait();
			print(Info.NOTIFIED, 21);
		}
		else{ // HERE! if 2 signals and 0 gets notified first (half of the time, you can see 1 gets notified right after
			if(Thread.currentThread().getName().contains("1")){ // thread 1 gets stalled here
				print(Info.WAIT, 29);
				myMon.myWait();
				print(Info.NOTIFIED, 27);
			}
			else{ // Thread 0 goes here
				while(Thread.currentThread().getName().contains("0") && someBool){
					print(Info.WAIT, 31);
					myMon.myWait();
					print(Info.NOTIFIED, 33);
				}
			}
		}
		
		if(Thread.currentThread().getName().contains("1")){
			myMon.setBool2False(this); // atomic modification of the boolean
			print(Info.WASTE1, 40);
			wasteTime(1000);
			print(Info.NOTIFYING, 42);
			myMon.myNotifyAll();
		}
	}
	
	// Just so the rest of the thread is a bit easier to read, put these long printlns here
	protected void print(Info status, int line){
		switch(status){
			case CREATE:		
				System.out.println(Thread.currentThread().getName()+" created @line: "+line);
				break;
			case CHECKITOUT:
				System.out.println("HERE, if 0 gets woken up first (50/50) you will see 1\n\twake up immediately with no other notify preceding it");
				break;
			case WASTE1:
				System.out.println(Thread.currentThread().getName()+" waste time, 1 sec @line: "+line);
				break;
			case NOTIFYING:
				System.out.println(Thread.currentThread().getName()+" sending notifyAll @line: "+line);
				break;
			case NOTIFIED:
				System.out.println(Thread.currentThread().getName()+" got notified @line: "+line);
				break;
			case WAIT:
				System.out.println(Thread.currentThread().getName()+" waitin' @line: "+line);
				break;
			case CHANGE:
				System.out.println(Thread.currentThread().getName()+" changed someCondition for T 0 @line: "+line);
				break;
			default:
				System.out.flush();	// make sure everything is out as fast as possible (should be by default)
		}	
	}
	protected static void wasteTime(long millis){	
		long start = System.currentTimeMillis();
		while((System.currentTimeMillis()-start)<millis);
	}
}