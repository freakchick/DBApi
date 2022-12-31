package com.gitee.freakchicken.dbapi.basic.es;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RestClientFactory implements PooledObjectFactory<RestClient> {

    HttpHost[] httpHosts;

    public RestClientFactory(String address) {
        String[] hosts = address.split(",");
        List<HttpHost> list = Arrays.asList(hosts).stream().map(t -> {
            String[] hostPort = t.split(";");
            return new HttpHost(hostPort[0], Integer.parseInt(hostPort[1]), "http");
        }).collect(Collectors.toList());
        httpHosts = list.toArray(new HttpHost[list.size()]);
    }

    @Override
    public PooledObject<RestClient> makeObject() throws Exception {
        RestClient restClient = RestClient.builder(httpHosts).build();
        return new DefaultPooledObject<>(restClient);
    }

    @Override
    public void destroyObject(PooledObject<RestClient> pooledObject) throws Exception {
        pooledObject.getObject().close();
    }

    @Override
    public boolean validateObject(PooledObject<RestClient> pooledObject) {
        return false;
    }

    @Override
    public void activateObject(PooledObject<RestClient> pooledObject) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<RestClient> pooledObject) throws Exception {

    }
}
