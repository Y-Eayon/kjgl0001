package com.zt.common;

public class PageUtil {
	private int pageSize=1;// 每页显示多少条
	private int totalSize;// 共多少条
	private int totalPage;// 总页数
	private int currentPage;// 当前页
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalSize) {
		if(totalSize%pageSize==0){
			this.totalPage = totalSize/pageSize;
		}else{
			this.totalPage = totalSize/pageSize+1;
		}
		
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	

}
