package com.ite.kjgl0001.util;

public class PageUtil {
    private int pageSize=10;
    private int totalSize;
    private int totalPage;
    private int currentPage;
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        if (this.totalSize > 0) {
            if (this.pageSize > 0) {
                if (this.totalSize % this.pageSize == 0) {
                    this.totalPage = this.totalSize / this.pageSize;
                } else {
                    this.totalPage = this.totalSize / this.pageSize + 1;
                }
            } else {
                this.totalPage = 0;
            }
        }
    }
    
    public int getTotalSize() {
        return totalSize;
    }
    
    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
        if (this.pageSize > 0) {
            if (this.totalSize % this.pageSize == 0) {
                this.totalPage = this.totalSize / this.pageSize;
            } else {
                this.totalPage = this.totalSize / this.pageSize + 1;
            }
        } else {
            this.totalPage = 0;
        }
    }
    
    public int getTotalPage() {
        return totalPage;
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
    
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
