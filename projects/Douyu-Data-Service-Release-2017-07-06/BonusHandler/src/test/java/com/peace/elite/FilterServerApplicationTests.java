package com.peace.elite;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.peace.elite.entities.BonusGift;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilterServerApplicationTests {

	@Test
	public void retieveUidCorrectlyFromSui() {
		String sui = "id@=103484412/name@=103484412/nick@=我是一颗小棋子/icon@=avanew@Sface@S201706@S19@S12@S5710c1fc1b6f8db3ca93e798a1e60fec/rg@=1/pg@=1/rt@=1484647102/bg@=0/weight@=0/cps_id@=0/ps@=1/es@=1/ver@=20150929/global_ban_lev@=0/exp@=997300/level@=18/curr_exp@=29300/up_need@=165300/gt@=0/it@=0/its@=0/cm@=0/rni@=0/hcre@=0/crei@=0/el@=eid@AA=1500000005@ASetp@AA=1@ASets@AA=1500726523@ASss@AA=0@ASsc@AA=1@ASsts@AA=1498134523@ASef@AA=0@ASsr@AA=2@AS@S/hfr@=1076903233/";
		assert(BonusGift.retrieveUid(sui) == 103484412L);
	}

}
