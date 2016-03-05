package notifyAllTest;

public class NotifyAllThread implements Runnable {
	private static enum Info {CREATE, SLEEP15, SLEEP1, NOTIFYING, NOTIFIED, WAIT, CHANGE};
	private MyMonitorTestAll myMon = new MyMonitorTestAll();
	private static boolean someCondition = true;
	@Override
	public void run() {
		
		print(Info.CREATE, 10);
		if (Thread.currentThread().getName().contains("2")){
			print(Info.SLEEP15, 12);
			try{Thread.sleep(1500);}catch(InterruptedException e){}
			print(Info.NOTIFYING, 14);
			myMon.myNotifyAll();
			print(Info.WAIT, 16);
			myMon.myWait();
			print(Info.NOTIFIED, 18);
		}
		else{
			if(Thread.currentThread().getName().contains("1")){ // thread 1 gets stalled here
				print(Info.SLEEP1, 22);
				try{Thread.sleep(1000);}catch(InterruptedException e){}
				print(Info.WAIT, 24);
				myMon.myWait();
				print(Info.NOTIFIED, 26);
			}
			else{ // Thread 0 goes here
				while(Thread.currentThread().getName().contains("0") && someCondition){
					print(Info.WAIT, 30);
					myMon.myWait();
					print(Info.NOTIFIED, 32);
					//print(Info.NOTIFYING);
					//myMon.myNotifyAll();
				}
			}
		}
		someCondition = false;
		print(Info.CHANGE, 39);
		print(Info.NOTIFYING, 40);
		myMon.myNotifyAll();
	}
	
	private void print(Info status, int line){
		switch(status){
			case CREATE:		
				System.out.println(Thread.currentThread().getName()+" created @line: "+line);
				break;
			case SLEEP15:
				System.out.println(Thread.currentThread().getName()+" sleeping 1.5 sec so everyone else is created and waiting @line: "+line);
				break;
			case SLEEP1:
				System.out.println("T 1 will sleep so T 0, who cannot run because of someCondition, gets woken up @line: "+line);
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
	
}