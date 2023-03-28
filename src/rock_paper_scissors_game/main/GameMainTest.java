package rock_paper_scissors_game.main;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GameMainTest extends GameMain {

//	@Test
//	void test_random10000() throws Exception {
//		int i = 0;
//		while (i == 0) {
//			int test = get_random(10000);
//			if (test == 9999) {
//				assertEquals(9999, test);
//				i++;
//			}
//		}
//	}
//
//	@Test
//	void test_random0() throws Exception {
//		int i = 0;
//		while (i == 0) {
//			int test = get_random(0);
//			if (test == 0) {
//				assertEquals(0, test);
//				i++;
//			}
//		}
//	}
//
//	@Test
//	void test_random_1() throws Exception {
//		int i = 0;
//		while (i == 0) {
//			int test = get_random(-1);
//			if (test == -1) {
//				assertEquals(-1, test);
//				i++;
//			}
//		}
//	}

//	@Test
//	void test_opponent_selection_rock() throws Exception {
//		int i = 0;
//		while (i == 0) {
//			int select = opponent_selection();
//			if (select == 1) {
//				assertEquals(1, select);
//				i++;
//			}
//		}
//	}
//
//	@Test
//	void test_opponent_selection_scissors() throws Exception {
//		int i = 0;
//		while (i == 0) {
//			int select = opponent_selection();
//			if (select == 2) {
//				assertEquals(2, select);
//				i++;
//			}
//		}
//	}
//
//	@Test
//	void test_opponent_selection_paper() throws Exception {
//		int i = 0;
//		while (i == 0) {
//			int select = opponent_selection();
//			if (select == 3) {
//				assertEquals(3, select);
//				i++;
//			}
//		}
//	}

//	@Test
//	void test_string_conversion() throws Exception {
//		String select = string_conversion(1);
//		assertEquals("グー", select);
//		String select2 = string_conversion(2);
//		assertEquals("チョキ", select2);
//		String select3 = string_conversion(3);
//		assertEquals("パー", select3);
////		String select4 = string_conversion(0);
////		assertEquals("", select4);
//	}
//	
//	@Test
//	void test_remove_players()throws Exception {
//		List<String> data_list = new ArrayList<>();
//		data_list.add("田中");
//		data_list.add("太郎");
//		System.out.println(String.format("削除前%s", data_list));
//		remove_players("田中", "太郎", data_list);
//		System.out.println(String.format("削除後%s", data_list));
//		assertEquals(0, data_list.size());
//	}
//	
	@Test
	void test_match_result() throws Exception{
		 int ROCK = 1; 			// グー
		 int SCISSORS = 2; 		// チョキ
		 int PAPER = 3; 		// パー
		assertEquals("勝利", match_result(ROCK,SCISSORS));
		assertEquals("勝利", match_result(SCISSORS,PAPER));
		assertEquals("勝利", match_result(PAPER, ROCK));
		assertEquals("敗北", match_result(SCISSORS, ROCK));
		assertEquals("敗北", match_result(PAPER, SCISSORS));
		assertEquals("敗北", match_result(ROCK, PAPER));
		assertEquals("あいこ", match_result(ROCK, ROCK));
		assertEquals("あいこ", match_result(PAPER, PAPER));
		assertEquals("あいこ", match_result(SCISSORS, SCISSORS));
		
		assertEquals("敗北", match_result(0, ROCK)); // 片側異常系
		assertEquals("あいこ", match_result(0, 0));  // 両側異常系
		assertEquals("敗北", match_result(0, 4));    // 両側異常系
	}
}
