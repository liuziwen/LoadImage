package com.example.liuziwen587.loadimage;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuziwen on 16/9/15.
 */
public class MyThreadQueue implements BlockingQueue<Runnable> {
    @Override
    public boolean add(Runnable runnable) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Runnable> collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean offer(Runnable runnable) {
        return false;
    }

    @Override
    public Runnable remove() {
        return null;
    }

    @Override
    public Runnable poll() {
        return null;
    }

    @Override
    public Runnable element() {
        return null;
    }

    @Override
    public Runnable peek() {
        return null;
    }

    @Override
    public void put(Runnable runnable) throws InterruptedException {

    }

    @Override
    public boolean offer(Runnable runnable, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Runnable take() throws InterruptedException {
        return null;
    }

    @Override
    public Runnable poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public int remainingCapacity() {
        return 0;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] array) {
        return null;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean equals(Object object) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @NonNull
    @Override
    public Iterator<Runnable> iterator() {
        return null;
    }

    @Override
    public int drainTo(Collection<? super Runnable> c) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super Runnable> c, int maxElements) {
        return 0;
    }
}
