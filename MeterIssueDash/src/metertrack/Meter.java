package metertrack;

/**
 * Class to represent metering data extracted from the database. The fields are arbitrary and can be replaced to represent
 * the information desired from each meter.
 * @author Jared Churchman
 * @version 1.0
 */
public class Meter {
	private int meterId;
	private String program;
	private String form;
	private String date;
	private String info;
	private double volA;
	private double volB;
	private double volC;
	private double curA;
	private double curB;
	private double curC;
	
	/**
	 * 
	 * @param id Meter ID number
	 * @param meterProgram Program ID
	 * @param form_id Form ID
	 * @param e_info Describes the meter flag type
	 * @param voltageA Phase A voltage
	 * @param voltageB Phase B voltage
	 * @param voltageC Phase C voltage
	 * @param currentA Phase A current
	 * @param currentB Phase B current
	 * @param currentC Phase C current
	 */
	public Meter(int id, String meterProgram, String form_id, String e_info, 
			double voltageA, double voltageB, double voltageC,
			double currentA, double currentB, double currentC) {
		this.meterId = id;
		this.program = meterProgram;
		this.form = form_id;
		this.info = e_info;
		this.volA = voltageA;
		this.volB = voltageB;
		this.volC = voltageC;
		this.curA = currentA;
		this.curB = currentB;
		this.curC = currentC;
	}

	public int getMeterId() {
		return meterId;
	}

	public void setMeterId(int meterId) {
		this.meterId = meterId;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public double getVolA() {
		return volA;
	}

	public void setVolA(double volA) {
		this.volA = volA;
	}

	public double getVolB() {
		return volB;
	}

	public void setVolB(double volB) {
		this.volB = volB;
	}

	public double getVolC() {
		return volC;
	}

	public void setVolC(double volC) {
		this.volC = volC;
	}

	public double getCurA() {
		return curA;
	}

	public void setCurA(double curA) {
		this.curA = curA;
	}

	public double getCurB() {
		return curB;
	}

	public void setCurB(double curB) {
		this.curB = curB;
	}

	public double getCurC() {
		return curC;
	}

	public void setCurC(double curC) {
		this.curC = curC;
	}
	
	
}
