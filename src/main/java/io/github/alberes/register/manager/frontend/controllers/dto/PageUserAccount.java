package io.github.alberes.register.manager.frontend.controllers.dto;

import java.util.List;

public class PageUserAccount {

    private List<UserAccountReportDto> content;
    private Pageable pageable;
    private boolean last;
    private int totalPages;
    private int totalElements;
    private int size;
    private int number;
    private List<Sort> sort;
    private int numberOfElements;
    private boolean first;
    private boolean empty;

    // Getters e Setters
    public List<UserAccountReportDto> getContent() {
        return content;
    }

    public void setContent(List<UserAccountReportDto> content) {
        this.content = content;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Sort> getSort() {
        return sort;
    }

    public void setSort(List<Sort> sort) {
        this.sort = sort;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public int getFirstPageNumber(){
        return 0;
    }

    public int getLastPageNumber(){
        return this.getTotalPages();
    }

    public int getNextPageNumber(){
        if(this.empty){
            return 0;
        }else if(this.last){
            return totalPages;
        }
        return number + 1;
    }

    public int getPreviousPageNumber(){
        if(this.empty || this.first){
            return 0;
        }
        return number - 1;

    }

    public String getDirection(){
        if(this.sort.size() > 0) {
            return this.sort.get(0).getDirection();
        }
        return "";
    }

    public String getOrderBy(){
        if(this.sort.size() > 0) {
            return this.sort.get(0).getProperty();
        }
        return "";
    }
}
