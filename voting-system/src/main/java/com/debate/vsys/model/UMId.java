package com.debate.vsys.model;

public class UMId {
	private String uId;
	private String mId;

	public UMId(String uId, String mId) {
		this.uId = uId;
		this.mId = mId;
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

	@Override
	public int hashCode() {
		int hash = (uId + mId).hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof UMId) {
			return ((UMId) obj).hashCode() == this.hashCode();
		}
		return false;
	}

}
