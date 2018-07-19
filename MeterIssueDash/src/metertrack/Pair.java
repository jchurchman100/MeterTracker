package metertrack;
	
	/**
	 * Java Helper Class implemented as a standard Pair object for mapping flag names with flag counts
	 * @param <L> Key, used for flag name
	 * @param <R> Value, used for flag count
	 */
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
