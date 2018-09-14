package com.debate.vsys.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Vote {
	// vote identity
	private String vId;
	private String uId;
	private String mId;
	private LocalDateTime time;
	private boolean val;
	private VoteStatus status;
	
	public Vote(String mId, String uId, boolean val) {
		this.vId = UUID.randomUUID().toString();
		this.uId = uId;
		this.mId = mId;
		this.val = val;
		this.time = LocalDateTime.now();
		this.status = VoteStatus.QUEUING;
	}
	
	@Override
	public String toString() {
		return "Vote [vId=" + vId + ", uId=" + uId + ", mId=" + mId + ", time=" + time + ","
				+ " val=" + val + ", status=" + status + "]";
	}

	public UMId getUMId() {
		return new UMId(uId, mId);
	}

	public String getVId() {
		return vId;
	}

	public void setVId(String vId) {
		this.vId = vId;
	}

	public String getUId() {
		return uId;
	}

	public void setUId(String uId) {
		this.uId = uId;
	}
	
	public String getMId() {
		return mId;
	}

	public void setMId(String mId) {
		this.mId = mId;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public boolean isVal() {
		return val;
	}

	public void setVal(boolean val) {
		this.val = val;
	}

	public VoteStatus getStatus() {
		return status;
	}

	public void setStatus(VoteStatus status) {
		this.status = status;
	}
	
}
