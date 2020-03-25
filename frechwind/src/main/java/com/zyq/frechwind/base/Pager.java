package com.zyq.frechwind.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiModel("通用分页对象")
@JsonIgnoreProperties(value = {"orders", "first", "orderBySetted"})
public class Pager<T> implements Serializable {
    private Map<String, Object> paraMap = new HashMap<String, Object>();
    private int totalCount = 0;
    private int pageSize = 0;
    private int totalPage = 1;
    private int currentPage = 1;
    private int first = 0;

    @ApiModelProperty(value = "返回的数据集", name = "items")
    private List<T> pageItems = new ArrayList<T>();

    private List<Integer> pageList = new ArrayList<Integer>();

    public Pager() {
        this.pageSize = 10;
    }

    public Pager clone(){
        Pager p = new Pager();
        p.totalCount = totalCount;
        p.pageSize = pageSize;
        p.totalPage = totalPage;
        p.currentPage = currentPage;
        p.first = first;
        p.pageItems = pageItems;
        p.pageList = pageList;

        return p;
    }

    public Pager(Integer currentPage, Integer pageSize) {
        if (currentPage == null) {
            currentPage = 1;
        }
        this.currentPage = currentPage;
        if (pageSize == null) {
            pageSize = 10;
        }
        this.pageSize = pageSize;
        this.first = (currentPage - 1) * pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage == 0 ? 1 : totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        int totalPages = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        this.setTotalPage(totalPages);
        if (currentPage <= 0) {
            setCurrentPage(1);
            setFirst(first);
        } else {
            setFirst((currentPage - 1) * pageSize);
        }
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getFirst() {
        return first;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage <= 0 ? 1 : currentPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<T> getPageItems() {
        return pageItems;
    }

    public void setPageItems(List<T> pageItems) {
        this.pageItems = pageItems;
    }

    public List<Integer> getPageList() {
        return pageList;
    }

    public void setPageList(List<Integer> pageList) {
        this.pageList = pageList;
    }

    @Override
    public String toString() {
        return "Pager [totalCount=" + totalCount + ", first=" + first + ", pageSize=" + pageSize + ", totalPage=" + totalPage + ", currentPage=" + currentPage + "]";
    }

    public static Pager<?> asPager(List<?> list) {
        Pager pager = new Pager();
        pager.setPageSize(list.size());
        pager.setTotalPage(1);
        pager.setTotalCount(list.size());
        pager.setPageItems(list);
        return pager;
    }

    public void setParaMap(Map<String, Object> paraMap) {
        this.paraMap = paraMap;
    }

    public void addPara(String name, String value) {
        this.paraMap.put(name, value);
    }

    public Object getParaValue(String name) {
        Object v = paraMap.get(name);
        if (v == null) {
            return "";
        }
        return v;
    }

    public String[] pagerPage(Integer currentPage, Integer pageSize) {

        StringBuffer sb = new StringBuffer();
        if (pageSize >= 5 && currentPage < 5) {
            for (int i = 1; i <= 5; i++) {
                sb.append(i + ",");
            }
            sb.append("...,...," + pageSize + ",");
        } else if (pageSize >= 5 && currentPage >= 5) {
            if (pageSize - currentPage > 3) {
                sb.append(1 + ",").append("...,...,").append(currentPage - 1 + ",").append(currentPage + ",").append(currentPage + 1 + ",").append("...,...,").append(pageSize + ",");
            } else {
                sb.append(1 + ",").append("...,...,").append(pageSize - 4 + ",").append(pageSize - 3 + ",").append(pageSize - 2 + ",").append(pageSize - 1 + ",").append(pageSize + ",");
            }
        } else {
            for (int i = 1; i <= pageSize; i++) {
                sb.append(i + ",");
            }
        }
        String[] strs = sb.toString().split(",");
        return strs;
    }

    /**
     * @return
     */
    public void fillPageList() {
        boolean addPrefix = false;//当前页之前的那个...
        boolean postfix = false;//当前页之后的那个...
        pageList = new ArrayList<Integer>();

        int c = currentPage;//当前页
        if (c < 3) {
            c = 3;
        }
        if (c > totalPage - 2) {
            c = totalPage - 2;
        }

        //思路：把所有页码输出，1、2 ... c-2, c-1, c, c+1, c+2 ... total-1, total,...用0代替
        for (int i = 1; i <= totalPage; i++) {
            //当前页之前的页码，且不是1/2加0，且只加1次
            if (i > 2 && i < c - 2 && !addPrefix) {
                pageList.add(0);
                addPrefix = true;//只加一次
            }

            //范围内的，加到pageList里面
            if (i <= 2 || (i >= c - 2 && i <= c + 2) || i > totalPage - 2) {
                pageList.add(i);
            }

            //当前页之后的页码，且不是total
            if (i <= totalPage && i > c + 2 && !postfix) {
                pageList.add(0);
                postfix = true;
            }
        }

        if (pageList.get(pageList.size() - 1) == 0) {
            pageList.remove(pageList.size() - 1);
        }
    }

    public static void main(String[] args) {
        Pager p = new Pager(59, 10);
        p.setTotalCount(605);

        p.fillPageList();
        for (int i = 0; i < p.pageList.size(); i++) {
            Integer n = (Integer) p.pageList.get(i);
            System.out.println(n);
        }
    }
}