package com.feng.home.zk.client;

import com.alibaba.fastjson.JSONObject;
import com.feng.home.zk.ZkConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Op;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;

/**
 * create by FengZiyu
 * 2020/01/10
 */
public class ZookeeperClient {

    private CuratorFramework client;

    @Autowired
    private ZkConfiguration configuration;

    private ConcurrentHashMap<String, TreeCache> watcherCache = new ConcurrentHashMap<>();

    public ZookeeperClient(){
        this.client = CuratorFrameworkFactory.builder().connectString(configuration.getZkHost())
                .sessionTimeoutMs(configuration.getSessionTimeoutMs()).connectionTimeoutMs(configuration.getConnectionTimeoutMs())
                .namespace(configuration.getNameSpace()).retryPolicy(configuration.getRetryPolicy().retryPolicy(configuration)).build();
    }


    public <T> T getNode(String path, Class<T> dataClass) throws Exception {
        byte[] content = client.getData().forPath(path);
        return JSONObject.parseObject(content, dataClass);
    }

    public boolean isNodeExist(String path) throws Exception {
        return client.checkExists().forPath(path) == null;
    }

    public void create(String path, CreateMode createMode) throws Exception {
        client.create().withMode(createMode).forPath(path);
    }

    public void createIfNotExist(String path) throws Exception {
        client.checkExists().creatingParentsIfNeeded().forPath(path);
    }


    public <T> void addWatcher(String path){

    }

}
