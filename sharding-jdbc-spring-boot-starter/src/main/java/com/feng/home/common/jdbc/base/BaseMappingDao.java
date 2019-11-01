package com.feng.home.common.jdbc.base;


import java.util.List;
import java.util.Optional;

/**
 * create by FengZiyu
 * 2019/11/01
 */
public abstract class BaseMappingDao extends BaseDao{

    public <T> int updateBean(String column, T bean){
        return this.updateBean(column, bean, this.getTable());
    }

    public <T> int updateById(T bean){
        return this.updateById(bean, this.getTable());
    }

    public <T> void saveBean(T bean){
        this.saveBean(bean, getTable());
    }

    public <T> void saveBeanList(List<T> beanList){
        this.saveBeanList(beanList, getTable());
    }

    public Optional<DaoMapping> getThisDaoMapping(){
        return Optional.ofNullable(this.getClass().getAnnotation(DaoMapping.class));
    }

    public String getTable(){
        return getThisDaoMapping().map(DaoMapping::logicTable).orElseThrow(() -> new UnsupportedOperationException("缺少DaoMapping注解的类不支持该类查询"));
    }
}
