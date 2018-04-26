package EVcharge;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainBodyTest {
	private MainBody mainBody;
	@Before
	public void setUp() throws Exception {
		mainBody = new MainBody();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindEVbyID() {
		//create 10 EV users to test
		MainBody.evUsers = new EV[10];
		MainBody.evUsers[0] = new EV(1,1,9,0,10,50,80);
		MainBody.evUsers[1] = new EV(2,2,8,30,11,0,60);
		MainBody.evUsers[2] = new EV(3,3,10,0,13,0,40);
		MainBody.evUsers[3] = new EV(4,3,9,0,11,0,70);
		MainBody.evUsers[4] = new EV(5,2,14,0,14,30,40);
		MainBody.evUsers[5] = new EV(6,2,11,0,12,30,30);
		MainBody.evUsers[6] = new EV(7,3,10,0,10,30,50);
		MainBody.evUsers[7] = new EV(8,1,9,30,10,0,60);
		MainBody.evUsers[8] = new EV(9,3,10,0,10,30,50);
		MainBody.evUsers[9] = new EV(10,1,9,30,10,0,60);
		//1.the searched id is in the evUsers, then return the correct index
		//successful test case 1
		int user_id_1 = 1;
		int result_1 = MainBody.findEVbyID(user_id_1);
		assertEquals(0, result_1);
		//successful test case 2
		int user_id_10 = 10;
		int result_10 = MainBody.findEVbyID(user_id_10);
		assertEquals(9, result_10);
		//2.the searched id is not in the evUsers, then return -1
		int user_id_0 = 0;
		int result_0 = MainBody.findEVbyID(user_id_0);
		assertEquals(-1, result_0);
		int user_id_11 = 11;
		int result_11 = MainBody.findEVbyID(user_id_11);
		assertEquals(-1, result_11);
	}

	@Test
	public void testFindChPointbyID() {
		//create 10 Charging points to test
		MainBody.chPoint = new ChPoint[10];
		MainBody.chPoint[0] = new ChPoint(1,1);
		MainBody.chPoint[1] = new ChPoint(2,4);
		MainBody.chPoint[2] = new ChPoint(3,2);
		MainBody.chPoint[3] = new ChPoint(4,3);
		MainBody.chPoint[4] = new ChPoint(5,2);
		MainBody.chPoint[5] = new ChPoint(6,4);
		MainBody.chPoint[6] = new ChPoint(7,1);
		MainBody.chPoint[7] = new ChPoint(8,2);
		MainBody.chPoint[8] = new ChPoint(9,2);
		MainBody.chPoint[9] = new ChPoint(10,3);
		//1.the searched id is in the Charging points, then return the correct index
		//successful test case 1
		int cp_id_1 = 1;
		int result_1 = MainBody.findChPointbyID(cp_id_1);
		assertEquals(0, result_1);
		//successful test case 2
		int cp_id_10 = 10;
		int result_10 = MainBody.findChPointbyID(cp_id_10);
		assertEquals(9, result_10);
		//2.the searched id is not in the Charging points, then return -1
		int user_id_0 = 0;
		int result_0 = MainBody.findChPointbyID(user_id_0);
		assertEquals(-1, result_0);
		int user_id_11 = 11;
		int result_11 = MainBody.findChPointbyID(user_id_11);
		assertEquals(-1, result_11);
	}

	@Test
	public void testMain() {
		//integration testing and black box testing
		//create all the data (from test 1 excel file) that test the function of the whole system between different modules
		//create 8 EV users to test
		MainBody.evUsers = new EV[10];
		MainBody.evUsers[0] = new EV(1,1,9,0,10,50,80);
		MainBody.evUsers[1] = new EV(2,2,8,30,11,0,60);
		MainBody.evUsers[2] = new EV(3,3,10,0,13,0,40);
		MainBody.evUsers[3] = new EV(4,3,9,0,11,0,70);
		MainBody.evUsers[4] = new EV(5,2,14,0,14,30,40);
		MainBody.evUsers[5] = new EV(6,2,11,0,12,30,30);
		MainBody.evUsers[6] = new EV(7,3,10,0,10,30,50);
		MainBody.evUsers[7] = new EV(8,1,9,30,10,0,60);
		//create 5 Charging points to test
		MainBody.chPoint = new ChPoint[10];
		MainBody.chPoint[0] = new ChPoint(1,1);
		MainBody.chPoint[1] = new ChPoint(2,4);
		MainBody.chPoint[2] = new ChPoint(3,2);
		MainBody.chPoint[3] = new ChPoint(4,3);
		MainBody.chPoint[4] = new ChPoint(5,2);
		//call the schedule algorithm
		MainBody.allocate();
		//calculate the number of charging points that can be used
		int result_chargepoints_1 = 0;
		for (int i=0;i<MainBody.evUsers.length;i++){
			if (MainBody.evUsers[i]!=null && MainBody.evUsers[i].getChargingPoint()>0){
				result_chargepoints_1++;
			}
		}
		//the expected result is 6 when the input is test 1 excel data
		assertEquals(6, result_chargepoints_1);
		
		//create all the data (from test 2 excel file) that test the function of the whole system between different modules
		//create 10 EV users to test
		MainBody.evUsers = new EV[10];
		MainBody.evUsers[0] = new EV(1,2,10,0,13,30,60);
		MainBody.evUsers[1] = new EV(2,2,9,0,12,0,50);
		MainBody.evUsers[2] = new EV(3,3,10,0,13,0,40);
		MainBody.evUsers[3] = new EV(4,3,9,0,11,0,60);
		MainBody.evUsers[4] = new EV(5,2,14,0,14,30,40);
		MainBody.evUsers[5] = new EV(6,2,11,0,12,30,30);
		MainBody.evUsers[6] = new EV(7,3,10,0,10,30,50);
		MainBody.evUsers[7] = new EV(8,1,9,30,11,30,25);
		MainBody.evUsers[8] = new EV(9,2,11,30,13,30,45);
		MainBody.evUsers[9] = new EV(10,1,10,0,11,30,80);
		//create 3 Charging points to test
		MainBody.chPoint = new ChPoint[10];
		MainBody.chPoint[0] = new ChPoint(1,1);
		MainBody.chPoint[1] = new ChPoint(2,4);
		MainBody.chPoint[2] = new ChPoint(3,2);
		//call the schedule algorithm
		MainBody.allocate();
		//calculate the number of charging points that can be used
		int result_chargepoints_2 = 0;
		for (int i=0;i<MainBody.evUsers.length;i++){
			if (MainBody.evUsers[i]!=null && MainBody.evUsers[i].getChargingPoint()>0){
				result_chargepoints_2++;
			}
		}
		//the expected result is 5 when the input is test 2 excel data
		assertEquals(5, result_chargepoints_2);
				
		//border testing
		
	}

}
