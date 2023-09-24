package com.lin.company_sales_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.company_sales_management_system.dto.client.ClientPostData;
import com.lin.company_sales_management_system.entity.Client;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
public interface ClientService extends IService<Client> {

    List<Client> getClients(ClientPostData clientInfo);

    Map<String, Object> updateClientById(Long id, ClientPostData clientInfo);

    Map<String, Object> addClient(ClientPostData clientInfo);
}
