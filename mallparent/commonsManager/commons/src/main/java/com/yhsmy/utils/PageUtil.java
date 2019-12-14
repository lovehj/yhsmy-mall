package com.yhsmy.utils;

/**分页工具类
 * @auth 李正义
 * @date 2019/11/8 11:05
 **/
public class PageUtil<T> {

    /**
     * 当前页
     */
    private int curPageNo = 1;

    /**
     * 总页数
     */
    private int pageCount;

    /**
     * 每页大小 默认10
     */
    private int pageSize = 10;

    /**
     * 上一页
     */
    private int upPageNo;

    /**
     * 下一页
     */
    private int nextPageNo;

    /**
     * 开始页
     */
    private int startPage;

    private T t;

    public int getCurPageNo() {
        return curPageNo;
    }

    public void setCurPageNo(int curPageNo) {
        if(curPageNo <= 0) {
            this.curPageNo = 1;
        }

        if(curPageNo != 1 && curPageNo > 0) {
            upPageNo = curPageNo - 1;
        }

        nextPageNo = curPageNo + 1;
        this.curPageNo = curPageNo;
        this.startPage = (curPageNo - 1) * pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        if(pageCount % pageSize > 0) {
            this.pageCount = pageCount / pageSize + 1;
        } else {
            this.pageCount = pageCount / pageSize;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getUpPageNo() {
        return upPageNo;
    }

    public void setUpPageNo(int upPageNo) {
        this.upPageNo = upPageNo;
    }

    public int getNextPageNo() {
        return nextPageNo;
    }

    public void setNextPageNo(int nextPageNo) {
        this.nextPageNo = nextPageNo;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {

        this.startPage = startPage;
    }
}
