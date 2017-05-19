package com.zhiyuninfo.dm.client;

public class PLCAddress {
	String beginAddress;
	int plcNo;
	int plcReadFunction;
	int plcNumber;
	
	PLCAddress(String beginAddress,int plcNo,int plcReadFunction,int plcNumber){
		this.beginAddress=beginAddress;
		this.plcNo=plcNo;
		this.plcReadFunction=plcReadFunction;
		this.plcNumber=plcNumber;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beginAddress == null) ? 0 : beginAddress.hashCode());
		result = prime * result + plcNo;
		result = prime * result + plcNumber;
		result = prime * result + plcReadFunction;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PLCAddress other = (PLCAddress) obj;
		if (beginAddress == null) {
			if (other.beginAddress != null)
				return false;
		} else if (!beginAddress.equals(other.beginAddress))
			return false;
		if (plcNo != other.plcNo)
			return false;
		if (plcNumber != other.plcNumber)
			return false;
		if (plcReadFunction != other.plcReadFunction)
			return false;
		return true;
	}
	
	
}
