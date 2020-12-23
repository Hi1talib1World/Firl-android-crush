package com.Denzo.firl.common;

public class SizedTreeSet<E> extends TreeSet<E> {

    private int maxSize ;

    public SizedTreeSet(int maxSize){
        super();
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(E e) {
        if(size() >= maxSize && !contains(e)){
            E last = null;
            Iterator<E> iterator = iterator();
            for( ; iterator.hasNext(); ){
                last = iterator.next();
            }
            remove(last);
        }
        return super.add(e);
    }

}
