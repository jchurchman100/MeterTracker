package metertrack;

	public class Pair<L,R> {
	    private L key;
	    private R value;
	    public Pair(L l, R r){
	        this.key = l;
	        this.value = r;
	    }
	    public L getKey(){ 
	    	return key; 
	    }
	    public R getValue(){ 
	    	return value; 
	    }
	    public void setL(L l){ 
	    	this.key = l; 
	   	}
	    public void setR(R r){
	    	this.value = r;
	   	}
}
