package com.cddgg.p2p.huitou.entity;

/**
 * 借贷工具，等额本息计算
 * @author Administrator
 *
 */
public class CalculateLoan {
	
	//还款期数
	private int count;
	
	//回购本金
	private double benjin;
	
	//利息
	private double lixi;
	
	//未归还本金
	private double notReturn;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getBenjin() {
		return benjin;
	}

	public void setBenjin(double benjin) {
		this.benjin = benjin;
	}

	public double getLixi() {
		return lixi;
	}

	public void setLixi(double lixi) {
		this.lixi = lixi;
	}

	public double getNotReturn() {
		return notReturn;
	}

	public void setNotReturn(double notReturn) {
		this.notReturn = notReturn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(benjin);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + count;
		temp = Double.doubleToLongBits(lixi);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(notReturn);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		CalculateLoan other = (CalculateLoan) obj;
		if (Double.doubleToLongBits(benjin) != Double
				.doubleToLongBits(other.benjin))
			return false;
		if (count != other.count)
			return false;
		if (Double.doubleToLongBits(lixi) != Double
				.doubleToLongBits(other.lixi))
			return false;
		if (Double.doubleToLongBits(notReturn) != Double
				.doubleToLongBits(other.notReturn))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CalculateLoan [benjin=" + benjin + ", count=" + count
				+ ", lixi=" + lixi + ", notReturn=" + notReturn + "]";
	}
	
	
}
