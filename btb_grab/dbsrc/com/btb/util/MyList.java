package com.btb.util;

import java.util.LinkedList;

public class MyList<T> extends LinkedList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void addFirst(T e) {
		// TODO Auto-generated method stub
		super.addFirst(e);
		if (this.size()>10) {
			super.removeLast();
		}
	}
}
