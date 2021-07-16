package com.lagou.edu.service.impl;

import com.lagou.edu.annotation.CustomAutowired;
import com.lagou.edu.annotation.CustomService;
import com.lagou.edu.annotation.CustomTransactional;
import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.pojo.Account;
import com.lagou.edu.service.TransferService;


/**
 * @author 应癫
 */
@CustomService("transferService")
@CustomTransactional
public class TransferServiceImpl implements TransferService {

    // 最佳状态
    // @Autowired 按照类型注入 ,如果按照类型无法唯一锁定对象，可以结合@Qualifier指定具体的id
    @CustomAutowired
    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {

        Account from = accountDao.queryAccountByCardNo(fromCardNo);
        Account to = accountDao.queryAccountByCardNo(toCardNo);

        from.setMoney(from.getMoney()-money);
        to.setMoney(to.getMoney()+money);

        accountDao.updateAccountByCardNo(to);
        //int c = 1/0;
        accountDao.updateAccountByCardNo(from);

    }
}
