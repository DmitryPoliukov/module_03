package com.epam.esm.repository.entity;

import org.springframework.hateoas.RepresentationModel;

import java.util.Collection;

public class PageData<T> extends RepresentationModel<PageData<T>> {

    private int currentPage;
    private int numberOfElements;
    private int numberOfPages;
    private Collection<T> content;

    public PageData(
            int currentPage, int numberOfElements, int numberOfPages, Collection<T> content) {
        this.currentPage = currentPage;
        this.numberOfElements = numberOfElements;
        this.numberOfPages = numberOfPages;
        this.content = content;
    }
    public PageData() {}

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public Collection<T> getContent() {
        return content;
    }

    public void setContent(Collection<T> content) {
        this.content = content;
    }
}
