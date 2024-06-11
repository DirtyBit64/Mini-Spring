package com.minis.batis;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapperNode {
    String namespace; //命名空间
    String id; // 节点id即sql_id
    String parameterType;
    String resultType;
    String sql;
    String parameter;

    @Override
    public String toString(){
        return this.namespace+"."+this.id+" : " +this.sql;
    }
}