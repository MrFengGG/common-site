package com.feng.home.common.jdbc.base;


import java.util.List;
import java.util.Optional;

/**
 * create by FengZiyu
 * 2019/11/01
 */
public abstract class BaseMappingDao extends BaseDao{

    public <T> Optional<T> findById(Integer id, Class<T> modelClass){
        return this.findById(id, modelClass, this.getTable());
    }

    public <T> Optional<T> findBy(String key, Object value, Class<T> modelClass){
        return this.findBy(key, modelClass, this.getTable(), value);
    }

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

    public void removeBy(String column, Object value){
        this.remove(column, value, this.getTable());
    }

    public void removeById(Object value){
        this.removeBy("id", value);
    }

    public Optional<DaoMapping> getThisDaoMapping(){
        return Optional.ofNullable(this.getClass().getAnnotation(DaoMapping.class));
    }

    public String getTable(){
        return getThisDaoMapping().map(DaoMapping::logicTable).orElseThrow(() -> new UnsupportedOperationException("缺少DaoMapping注解的类不支持该类查询"));
    }
}
