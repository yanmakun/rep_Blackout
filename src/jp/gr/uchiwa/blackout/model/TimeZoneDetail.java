package jp.gr.uchiwa.blackout.model;

/**
 * 時間帯の明細
 * @author takuro
 *
 */
public class TimeZoneDetail {
	/** 物件No */
	private String no;
	/** 物件名 */
	private String bukkenName;
	/** サブグループ名 */
	private String subGroupName;
	/** 実施日 */
	private String doDate;
	/** 開始時刻 */
	private String startTime;
	/** 終了時刻 */
	private String endTime;
	/** 優先順位 */
	private int priority;
	/**
	 * @param no
	 * @param bukkenName
	 * @param subGroupName
	 * @param doDate
	 * @param startTime
	 * @param endTime
	 * @param priority
	 */
	public TimeZoneDetail(String no, String bukkenName, String subGroupName,
			String doDate, String startTime, String endTime, int priority) {
		super();
		this.no = no;
		this.bukkenName = bukkenName;
		this.subGroupName = subGroupName;
		this.doDate = doDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.priority = priority;
	}
	/**
	 * @return no
	 */
	public String getNo() {
		return no;
	}
	/**
	 * @param no セットする no
	 */
	public void setNo(String no) {
		this.no = no;
	}
	/**
	 * @return bukkenName
	 */
	public String getBukkenName() {
		return bukkenName;
	}
	/**
	 * @param bukkenName セットする bukkenName
	 */
	public void setBukkenName(String bukkenName) {
		this.bukkenName = bukkenName;
	}
	/**
	 * @return subGroupName
	 */
	public String getSubGroupName() {
		return subGroupName;
	}
	/**
	 * @param subGroupName セットする subGroupName
	 */
	public void setSubGroupName(String subGroupName) {
		this.subGroupName = subGroupName;
	}
	/**
	 * @return doDate
	 */
	public String getDoDate() {
		return doDate;
	}
	/**
	 * @param doDate セットする doDate
	 */
	public void setDoDate(String doDate) {
		this.doDate = doDate;
	}
	/**
	 * @return startTime
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime セットする startTime
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime セットする endTime
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @param priority セットする priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}


	/* (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TimeZoneDetail [no=" + no + ", bukkenName=" + bukkenName
				+ ", subGroupName=" + subGroupName + ", doDate=" + doDate
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", priority=" + priority + "]";
	}
	
	
	
}
