package com.study.springStarter.util;


public class PageHandler {
    private int page = 1; // 현재 페이지
    private int pageSize = 10; // 한 페이지 크기
    private int totalCnt; // 총 댓글 수
    private int naviSize = 5; // 페이지 네비게이션의 크기
    private int totalPage; // 전체 페이지의 개수
    private int beginPage; // 네비게이션의 첫번째 페이지
    private int endPage; // 네비게이션의 마지막 페이지
    private boolean showPrev; // 이전 페이지로 이동하는 링크를 보여줄 것인지의 여부
    private boolean showNext; // 다음 페이지로 이동하는 링크를 보여줄 것인지의 여부

    public int getOffset() {
        return (page - 1) * pageSize;
    }

    public PageHandler(int totalCnt, int page, int pageSize) {
        this.page = (page < 1) ? 1 : page;
        this.pageSize = (pageSize < 1) ? 10 : pageSize;
        this.totalCnt = totalCnt;
        doPaging(totalCnt);
    }

    public PageHandler() {
    }

    public void doPaging(int totalCnt) {
        totalPage = (int) Math.ceil(totalCnt / (double) pageSize); // 올림
        beginPage = (page - 1) / naviSize * naviSize + 1;
        endPage = Math.min(beginPage + naviSize - 1, totalPage);
        showPrev = beginPage != 1;
        showNext = endPage != totalPage;
    }

    public int getPage() {
        return Math.max(1, page);
    }
    // ※ setPage(), setPageSize() 후에는 doPaging()을 수동으로 반드시 다시 호출해야 함
    public void setPage(int page) {
        this.page = (page < 1) ? 1 : page;
    }

    public int getPageSize() {
        return Math.max(1, pageSize);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = (pageSize < 1) ? 10 : pageSize;
    }

    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public int getNaviSize() {
        return naviSize;
    }

    public void setNaviSize(int naviSize) {
        this.naviSize = naviSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getBeginPage() {
        return beginPage;
    }

    public void setBeginPage(int beginPage) {
        this.beginPage = beginPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public boolean isShowPrev() {
        return showPrev;
    }

    public void setShowPrev(boolean showPrev) {
        this.showPrev = showPrev;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }

    @Override
    public String toString() {
        return "PageHandler{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", totalCnt=" + totalCnt +
                ", naviSize=" + naviSize +
                ", totalPage=" + totalPage +
                ", beginPage=" + beginPage +
                ", endPage=" + endPage +
                ", showPrev=" + showPrev +
                ", showNext=" + showNext +
                '}';
    }
}