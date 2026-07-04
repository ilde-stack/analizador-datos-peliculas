package tad.stack;

import java.util.Iterator;

import tad.exceptions.EmptyStack;
import tad.list.MyArrayListIterator;

public class MyArrayStackImpl<T> implements MyStack<T> {

    public T[] stackArray;
    public int totalSize;
    public int dynamicSize;

    public MyArrayStackImpl(int totalSize) {
        this.stackArray = (T[]) new Object[totalSize];
        this.totalSize = totalSize;
        this.dynamicSize = 0;
    }

    @Override
    public void push(T value){
        stackArray[dynamicSize]=  value;
        dynamicSize++;
    }

    @Override
    public T pop() throws EmptyStack {
        if (dynamicSize < 1) {
            throw new EmptyStack();
        }
        dynamicSize--;
        T toRemove=  stackArray[dynamicSize];
        stackArray[dynamicSize] = null;
        return toRemove;
    }

    @Override
    public T top() throws EmptyStack {
        if (dynamicSize < 1) {
            throw new EmptyStack();
        }
        return stackArray[dynamicSize-1];
    }

    @Override
    public boolean isEmpty() {
        return dynamicSize == 0;
    }

    @Override
    public void makeEmpty() {
        stackArray = (T[]) new Object[totalSize];
    }

    @Override
    public int getSize() {
        return dynamicSize;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyArrayListIterator<>(stackArray, dynamicSize);
    }
}

