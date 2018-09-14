package com.debate.vsys.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Motion {

	private String mId;
	private String name;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private int yeas;
	private int nays;
	private MotionStatus status;
	private MotionResult result;
	
	public Motion(String name, LocalDateTime startTime, LocalDateTime endTime,
			int yeas, int nays, MotionStatus status, MotionResult result) {
		this.mId = UUID.randomUUID().toString();
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.yeas = yeas;
		this.nays = nays;
		this.status = status;
		this.result = result;
	}
	
	public Motion(String description, LocalDateTime startTime, LocalDateTime endTime) {
		this.mId = UUID.randomUUID().toString();
		this.name = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.yeas = 0;
		this.nays = 0;
		this.status = MotionStatus.RUNNABLE;
		this.result = MotionResult.PENDING;
	}
	
	@Override
	public String toString() {
		return "Motion [mId=" + mId + ", description=" + name + ", startTime=" + startTime + ", endTime="
				+ endTime + ", yeas=" + yeas + ", nays=" + nays + ", status=" + status + ", result=" + result + "]";
	}

	public String getMId() {
		return mId;
	}
	public void setMId(String mId) {
		this.mId = mId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public MotionStatus getStatus() {
		return status;
	}
	public void setStatus(MotionStatus status) {
		this.status = status;
	}
	public MotionResult getResult() {
		return result;
	}
	public void setResult(MotionResult result) {
		this.result = result;
	}
	public int getYeas() {
		return yeas;
	}
	public void setYeas(int yeas) {
		this.yeas = yeas;
	}
	public int getNays() {
		return nays;
	}
	public void setNays(int nays) {
		this.nays = nays;
	}

}
