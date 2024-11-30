package com.markepost.page.generic;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page<T> {
	private List<T> items = new ArrayList<>();
	private int totalCount;
	@Setter(value = AccessLevel.NONE)
	private int totalPages;
	private int nowPage;
	private int pageSize;
	
	// 요소 추가
    public void add(T element) {
        this.items.add(element);
    }

    // 요소 가져오기
    public T get(int index) {
        return this.items.get(index);
    }
    
    // totalCount, totalPages 세팅
    public void setTotalCount(int totalCount) {
    	this.totalCount = totalCount;
    	this.totalPages = (int) Math.ceil((double) totalCount / this.pageSize);
    }
    
    // offset 계산
    public int getOffset() {
    	return (nowPage - 1) * pageSize;
    }
}
