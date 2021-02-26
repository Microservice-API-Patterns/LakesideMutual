package com.lakesidemutual.customercore.application;

import java.util.List;

/**
 * An instance of the Page class can be used to represent a subset of a specific type of resource.
 * */
public class Page<T> {
	private final List<T> elements;
	private final int offset;
	private final int limit;
	private final int size;

	public Page(List<T> elements, int offset, int limit, int size) {
		this.elements = elements;
		this.offset = offset;
		this.limit = limit;
		this.size = size;
	}

	public List<T> getElements() {
		return elements;
	}

	public int getOffset() {
		return offset;
	}

	public int getLimit() {
		return limit;
	}

	public int getSize() {
		return size;
	}
}
