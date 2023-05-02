package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTransferDaoTest extends BaseDaoTests {
    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }
    @Test
    public void testSendingMoneyAndSeeTransfer() {
        Transfer transfer = new Transfer();
        transfer.setUsernameTo("Kevin");
        transfer.setAmount(100);
        sut.sendMoney("Quang", transfer);
        Assert.assertEquals(1100, sut.getBalance("Kevin"), .001);
        Transfer transfer2 = new Transfer();
        transfer2.setUsernameTo("Quang");
        transfer2.setAmount(200);
        sut.sendMoney("Kevin", transfer2);
        Assert.assertEquals(1100, sut.getBalance("Quang"), .001);
        Assert.assertEquals(2, sut.seeTransfer("Quang").size());

    }
    @Test
    public void testApproveRequestAndSeePending(){
        Transfer transfer = new Transfer();
        transfer.setUsernameFrom("Kevin");
        transfer.setAmount(100);
        int newId = sut.requestMoney("Quang", transfer).getTransferId();
        Assert.assertEquals(1, sut.seePending("Kevin").size());
        sut.approveRequest("Kevin", newId, "Approved");
        Assert.assertEquals(900, sut.getBalance("Kevin"), .001);
    }
    @Test
    public void testRejectRequest(){
        Transfer transfer = new Transfer();
        transfer.setUsernameFrom("Kevin");
        transfer.setAmount(100);
        int newId = sut.requestMoney("Quang", transfer).getTransferId();
        sut.approveRequest("Kevin", newId, "Rejected");
        Assert.assertEquals(1000, sut.getBalance("Kevin"), .001);

    }
    @Test
    public void testFindTransferById(){
        Transfer transfer = new Transfer();
        transfer.setUsernameTo("Kevin");
        transfer.setAmount(100);
        int newId = sut.sendMoney("Quang", transfer).getTransferId();
        Transfer transfer2 = sut.findTransferByTransferId("Quang", newId);
        Assert.assertEquals("Approved", transfer2.getStatus());
        Assert.assertEquals("Quang", transfer2.getUsernameFrom());
        Assert.assertEquals(100, transfer2.getAmount(), .001);
        Assert.assertEquals("Kevin", transfer2.getUsernameTo());
    }


}
