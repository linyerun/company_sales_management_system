package com.lin.company_sales_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.company_sales_management_system.dto.client.ClientPostData;
import com.lin.company_sales_management_system.entity.Client;
import com.lin.company_sales_management_system.mapper.ClientMapper;
import com.lin.company_sales_management_system.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 林叶润-20软卓1班
 * @since 2022-11-26
 */
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService {

    @Override
    public List<Client> getClients(ClientPostData clientInfo) {

        String phoneNumber = clientInfo.getPhoneNumber();
        String email = clientInfo.getEmail();
        String clientName = clientInfo.getClientName();

        LambdaQueryWrapper<Client> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(!Objects.isNull(phoneNumber)&&phoneNumber.length()!=0,Client::getPhoneNumber,phoneNumber)
                .eq(!Objects.isNull(email)&&email.length()!=0,Client::getEmail,email)
                .like(!Objects.isNull(clientName)&&clientName.length()!=0,Client::getClientName, clientName);

        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Map<String, Object> updateClientById(Long id, ClientPostData clientInfo) {
        Client client = new Client();

        client.setId(id);
        client.setClientName(clientInfo.getClientName());
        client.setEmail(clientInfo.getEmail());
        client.setPhoneNumber(clientInfo.getPhoneNumber());

        int returnValue = baseMapper.updateById(client);

        Map<String, Object> res = new HashMap<>();
        if (returnValue != 1) {
            res.put("code", 500);
            res.put("err", "系统繁忙,更新客户信息失败,请稍后再进行尝试!");
            return res;
        }

        return null;
    }

    @Override
    public Map<String, Object> addClient(ClientPostData clientInfo) {

        Client client = new Client();
        client.setClientName(clientInfo.getClientName());
        client.setEmail(clientInfo.getEmail());
        client.setPhoneNumber(clientInfo.getPhoneNumber());

        int returnValue = baseMapper.insert(client);

        Map<String, Object> res = new HashMap<>();
        if (returnValue != 1) {
            res.put("code", 500);
            res.put("err", "系统繁忙,新增客户信息失败,请稍后再进行尝试!");
            return res;
        }

        return null;
    }
}
