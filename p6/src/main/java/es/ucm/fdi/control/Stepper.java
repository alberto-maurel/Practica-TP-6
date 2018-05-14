package es.ucm.fdi.control;

public class Stepper {
	private Runnable before;
	private Runnable during;
	private Runnable after;
	
	private int steps;
	private boolean stopRequested;
	
	public Stepper(Runnable before, Runnable during, Runnable after){
		this.before = before;
		this.during = during;
		this.after = after;
		stopRequested = false;
	}
	
	public void stop(){
		stopRequested = true;
	}
	
	public Thread start (int steps, int delay){
		this.steps = steps;
		
		Thread t = new Thread(()->{
			try{
				before.run();
				while(!stopRequested && Stepper.this.steps > 0){
					during.run();
					try{
						Thread.sleep(delay);
					} catch (InterruptedException ie) {
						
					}
					Stepper.this.steps--;
				}
			} catch (Exception e) {
				
			} finally {
				
				after.run();
			}	
			
		});
		t.start();
		return t;
	}
}
