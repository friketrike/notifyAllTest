package notifyAllTest;

public class MyMonitorTestAll {
	private static Object mutex = new Object();
	
	public void myWait(){
		synchronized(mutex){
			try{mutex.wait();}catch(InterruptedException e){}
		}
	}
	public void myNotifyAll() {
		synchronized(mutex){
			mutex.notifyAll();
		}
	}
	public void setBool2False(NotifyAllThread caller){
		synchronized(mutex){
			caller.print(NotifyAllThread.Info.CHANGE, 118);
			NotifyAllThread.someBool = false;
		}
	}
}
