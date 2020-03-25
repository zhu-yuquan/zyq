package com.zyq.frechwind.base;

public class KeyValue<K,V> {
    private K key;
    private V value;
    private String remark;

    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean equals(Object obj){
        KeyValue kv = (KeyValue)obj;
        if(obj == null){
            return false;
        }
        return this.getKey().equals(kv.getKey()) && (this.getValue() != null && this.getValue().equals(kv.getValue()));
    }
}