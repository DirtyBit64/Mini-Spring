package com.minis.beans;

import java.util.*;

public class ArgumentValues {
    private final Map<Integer, ArgumentValue> indexedArgumentValues = new HashMap<>(0);
    private final List<ArgumentValue> genericArgumentValues = new LinkedList<>();

    /**
     * 添加ArgumentValue
     * @param key
     * @param newValue 待添加的Arg属性
     */
    private void addArgumentValue(Integer key, ArgumentValue newValue) {
        this.indexedArgumentValues.put(key, newValue);
    }

    /**
     * 是否包含当前index
     * @param index
     * @return
     */
    public boolean hasIndexedArgumentValue(int index) {
        return this.indexedArgumentValues.containsKey(index);
    }

    /**
     * 从map中拿到value对象
     * @param index
     * @return
     */
    public ArgumentValue getIndexedArgumentValue(int index) {
        return this.indexedArgumentValues.get(index);
    }

    /**
     * 新属性对象，创建并送入集合
     * @param value
     * @param type
     */
    public void addGenericArgumentValue(Object value, String type) {
        this.genericArgumentValues.add(new ArgumentValue(value, type));
    }

    /**
     * 往集合添加创建好的ArgumentValue对象
      * @param newValue
     */
    private void addGenericArgumentValue(ArgumentValue newValue) {
        if (newValue.getName() != null) {
            // 遍历参数集合, 如果当前属性在集合中已存在，则删除，送入新创建好的属性
            for (Iterator<ArgumentValue> it = this.genericArgumentValues.iterator(); it.hasNext(); ) {
                ArgumentValue currentValue = it.next();
                if (newValue.getName().equals(currentValue.getName())) {
                    it.remove();
                }
            }
        }
        this.genericArgumentValues.add(newValue);
    }

    /**
     * 根据属性名从集合获取ArgumentValue对象
     * @param requiredName
     * @return
     */
    public ArgumentValue getGenericArgumentValue(String requiredName) {
        for (ArgumentValue valueHolder : this.genericArgumentValues) {
            if (valueHolder.getName() != null && !valueHolder.getName().equals(requiredName)) {
                continue;
            }
            // 若当前对象name空或name相等
            return valueHolder;
        }
        return null;
    }

    /**
     * 获取当前集合中属性对象的数目
     * @return
     */
    public int getArgumentCount() {
        return this.genericArgumentValues.size();
    }
    public boolean isEmpty() {
        return this.genericArgumentValues.isEmpty();
    }
}