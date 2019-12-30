package com.feng.home.common.support;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * create by FengZiyu
 * 2019/12/30
 */
public class BatchOperateResult<T> {
    private List<OperateResult<T>> operateResultList = new LinkedList<>();

    public void addFail(T t, String msg){
        operateResultList.add(OperateResult.ofFail(t, msg));
    }

    public void addSuccess(T t){
        operateResultList.add(OperateResult.ofSuccess(t));
    }

    public List<OperateResult<T>> getOperateResult(){
        return operateResultList;
    }

    public long getSuccessNum(){
        return operateResultList.stream().filter(OperateResult::isSuccess).count();
    }

    public long getFailNum(){
        return operateResultList.stream().filter(OperateResult::isFail).count();
    }

    public List<T> getSuccessResult(){
        return operateResultList.stream().filter(OperateResult::isSuccess).map(OperateResult::getResult).collect(Collectors.toList());
    }

    public List<OperateResult<T>> getSuccessOperateResult(){
        return operateResultList.stream().filter(OperateResult::isSuccess).collect(Collectors.toList());
    }

    public List<T> getFailResult(){
        return operateResultList.stream().filter(OperateResult::isFail).map(OperateResult::getResult).collect(Collectors.toList());
    }

    public List<OperateResult<T>> getFailOperateResult(){
        return operateResultList.stream().filter(OperateResult::isFail).collect(Collectors.toList());
    }
}
