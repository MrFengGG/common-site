package com.feng.home.common.jdbc.base;

import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * create by FengZiyu
 * 2019/11/01
 * 映射类
 */
@Validated
public abstract class BaseMappingDao extends BaseDao{

    /**
     * 快速根据ID查询
     * @param id
     * @param modelClass 返回值类型
     * @param <T>
     * @return
     */
    public <T> Optional<T> findById(@NotNull Integer id, @NotNull Class<T> modelClass){
        return this.findById(id, modelClass, this.getTable());
    }
    /**
     * 快速根据单个字段查询
     * @param key 字段名称
     * @param value 字段值
     * @param modelClass 返回值类型
     * @param <T>
     * @return
     */
    public <T> Optional<T> findBy(@NotBlank String key, @NotNull Object value, @NotNull Class<T> modelClass){
        return this.findBy(key, modelClass, this.getTable(), value);
    }

    /**
     * 快速根据单个字段更新bean
     * @param column
     * @param bean
     * @param <T>
     * @return
     */
    public <T> int updateBean(@NotBlank String column, @ NotNull T bean){
        return this.updateBean(column, bean, this.getTable());
    }

    /**
     * 快速根据ID更新数据库
     * @param bean 必须包含id字段
     * @param <T>
     * @return
     */
    public <T> int updateById(@NotNull T bean){
        return this.updateById(bean, this.getTable());
    }

    public <T> void saveBean(@NotNull T bean){
        this.saveBean(bean, getTable());
    }

    public <T> void saveBeanList(@NotEmpty List<T> beanList){
        this.saveBeanList(beanList, getTable());
    }

    public void removeBy(@NotBlank String column, @NotNull Object value){
        this.remove(column, value, this.getTable());
    }

    public void removeById(@NotNull Object value){
        this.removeBy("id", value);
    }

    private Optional<DaoMapping> getThisDaoMapping(){
        return Optional.ofNullable(this.getClass().getAnnotation(DaoMapping.class));
    }

    private String getTable(){
        return getThisDaoMapping().map(DaoMapping::logicTable).orElseThrow(() -> new UnsupportedOperationException("缺少DaoMapping注解的类不支持该类查询"));
    }
}
