package rock_paper_scissors_game.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class GameMain {

	private Scanner sc = new Scanner(System.in); // スキャナー機能

	/**
	 * 定数
	 * */
	private final String WIN = "勝利"; 		// 勝利
	private final String LOSE = "敗北"; 	// 敗北
	private final String AIKO = "あいこ"; 	// あいこ
	private final int ROCK = 1; 			// グー
	private final int SCISSORS = 2; 		// チョキ
	private final int PAPER = 3; 			// パー

	public static void main(String[] args) {

		
		String player = ""; 		 // プレイヤー名初期化
		boolean end_trigger = false; // 終了の変数

		try {
			GameMain gm = new GameMain(); // インスタンス

			// プレイヤー名入力処理 2022/12/8追加
			player = gm.get_input_name();

			// 最後の選択でループ続行か終了をする。 2022/12/8追加
			while (end_trigger == false) {

				List<String> data_list = new ArrayList<>(); 		//参加者全員を入れるリスト
				List<String> wins_list = new ArrayList<>(); 		//１試合毎に勝者のみ入れるリスト
				List<List<String>> rank_list = new ArrayList<>(); 	// ランキングリスト
				//参加者入力
				int number_of_participants = gm.participant_input_reception();

				// 入力された値を受け取り名前を自動生成する
				data_list = gm.create_participant(number_of_participants, player, data_list);

				// 参加者の生成人数と結果を表示
				int allmember = number_of_participants + 1;
				System.out.println(String.format("\n全参加者は%d名です。", allmember));

				// トーナメント開始
				gm.tournament_style(player, data_list, wins_list, rank_list);

				// ランキング結果発表処理
				gm.set_ranking(rank_list);

				// 優勝者発表
				String winner = wins_list.get(0);
				System.out.println(String.format("\n優勝は%sさんです！", winner));

				// 終了か続行を選択する処理 2022/12/8追加
				end_trigger = gm.get_end_trigger();

			}

			// コンソール入力を終了する
			gm.sc.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	/** 2022/12/8追加
	 *  終了か続行の選択処理
	 * */
	public boolean get_end_trigger() throws Exception {
		String end_trigger = ""; // 終了または続行が入る
		boolean answer = false;  // 選択の結果

		// 入力されるまでループ
		while (end_trigger.equals("")) {

			// プレイヤーのじゃんけん選択
			System.out.println("\nもう一度トーナメントを開始しますか？");
			System.out.println("1:終了する  2:続行する");

			end_trigger = sc.next();

			//全角を半角にする処理をかませることで、大量なことにも対応できる。
			//正規表現も視野に入れておく。臨機応変に。
			
			// 入力規制処理
			switch (end_trigger) {
			case "1":
			case "１":
				System.out.println("終了します");
				answer = true;
				break;
			case "2":
			case "２":
				System.out.println("続行します");
				answer = false;
				break;
			default:
				System.out.println("1か2の数字を入力してください");
				end_trigger = "";
				break;
			}
		}
		return answer;
	}

	/** 2022/12/8追加
	 *  ランキング結果を表示する
	 *  引数	rank_list：ランキングリスト
	 * */
	public void set_ranking(List<List<String>> rank_list) throws Exception {
		// ランキングのグループを取り出す
		for (int i = 0; i < rank_list.size(); i++) {
			int rank = rank_list.size() - i;
			System.out.println(String.format("\n%d位", rank));
			
			// グループに所属してる名前を1件づつ表示
			for (int j = 0; j < rank_list.get(i).size(); j++) {
				String name = rank_list.get(i).get(j);
				System.out.println(String.format("%sさん ", name));
			}
		}
	}

	/** 2022/12/8追加
	 * 名前入力機能
	 **/
	public String get_input_name() throws Exception {
		String player_name = ""; // プレイヤー名初期化

		// 入力されるまでループ
		while (player_name.equals("")) {

			System.out.println("プレイヤーの名前を入力してください");

			// 入力された文字を変数に格納
			player_name = sc.nextLine();

			// 入力された文字列を文字配列化する
			char[] judgement = player_name.toCharArray();

			// 文字配列分For分を回す
			for (int i = 0; i < judgement.length; i++) {

				// その中で数値にあたるものがある場合は再入力を促す
				if (Character.isDigit(judgement[i]) == true) {

					// 入力されたものに数字があった場合初期化する
					player_name = "";
					// 入力受付処理
					System.out.println("数値を含まず入力してください");
					break;
				}
			}
			judgement = null; // 判定を終えたら初期化
		}
		return player_name;
	}

	/**
	 * ランダム処理 
	 * 引数　elementary : ランダム関数の結果に掛ける値
	 * 0~0.99999...までなので１が出てこないので10000が来ても最大は9999になる
	 * */
	public int get_random(int elementary) throws Exception {
		int ramdom = (int) Math.floor(Math.random() * elementary);
		return ramdom;
	}

	/**
	 * トーナメント式
	 * 引数		player_name		: プレイヤーの名前
	 * 			data_list		: 参加者リスト
	 * 			wins_list		: 勝者リスト
	 * 			rank_list		: ランキングリスト
	 * */
	public void tournament_style(String player_name, List<String> data_list, List<String> wins_list,
			List<List<String>> rank_list) throws Exception {

		// 優勝者が決まるまでループする。
		while (wins_list.size() < 1 && data_list.size() != 0) {
			List<String> loses_list = new ArrayList<>(); //試合毎に敗者を順に入れるリスト 2022/12/8追加

			// 対戦開始
			match_start(player_name, data_list, wins_list, loses_list);

			// 対戦後の敗者のリストを配列で入れる
			rank_list.add(loses_list);

			// 参加者が奇数の場合残り1人は次回戦進出処理
			if (data_list.size() == 1) {
				wins_list.add(data_list.get(0));
				String next_member = data_list.get(0);
				System.out.println(String.format("\n繰り上がり進出は%sさんとなります！", next_member));

				// 参加者リストを初期化
				data_list.clear();
			}

			// 勝者のリストで参加者のリストを上書き処理
			for (int i = 0; i < wins_list.size(); i++) {

				// 勝者のリストが1人の場合処理中断
				if (wins_list.size() == 1) {
					rank_list.add(wins_list); // 最後に残った方をランキングリストへ追加
					break;

					// 勝者リストが1人以上の場合次回戦メンバーに登録
				} else {

					// 勝者を読み込み参加者リストに格納
					data_list.add(wins_list.get(i));
				}
			}

			// 参加者リストと勝者リストが同じなら勝者リスト表示をして勝者リストを空にする
			if (data_list.size() == wins_list.size()) {

				// 勝者のリストを表示
				int member = wins_list.size();
				System.out.println(String.format("\n次回戦進出者は%s名です！", member));
				List<String> allmember = wins_list;
				System.out.println(String.format("%s\n", allmember));

				// 勝者のリストを初期化する。
				wins_list.clear();

			}

		}
	}

	/**
	 * リストから各playerを削除
	 * 引数 left_player		: 左側のプレイヤー
	 * 		right_opponent	: 右側の対戦相手
	 * 		data_list		: 参加者リスト
	 * */
	public void remove_players(String left_player, String right_opponent, List<String> data_list) throws Exception {
		data_list.remove(left_player);
		data_list.remove(right_opponent);
	}

	/**
	 * 参加者受付処理
	 * */
	public int participant_input_reception() throws Exception {

		// 入力規制 1から3で1文字のみ入力受付する設定
		String select = "[1-9 １-９][0-9 ０-９]{0,3}";
		Pattern pattern = Pattern.compile(select);
		int participant = 0; // 参加者人数 初期化

		// 入力されるまでループ
		while (participant == 0) {
			System.out.println("参加人数を入力してください");

			// 入力規制処理
			if (sc.hasNext(pattern)) {

				// 入力受付処理
				participant = sc.nextInt();

				// 入力が0の時と数字以外は再度入力を求める
			} else {
				System.out.println("1以上の数値を入力してください");
				sc.next();
			}
		}
		return participant;
	}

	/**
	 * 対戦開始
	 * 引数 player_name : プレイヤーの名前
	 * 		data_list	: 参加者リスト
	 * 		wins_list	: 勝者リスト
	 * */
	public void match_start(String player_name, List<String> data_list, List<String> wins_list, List<String> loses_list)
			throws Exception {
		String left_player = null;
		String right_opponent = null;

		// リスト内の参加者が1人になるまでひたすらじゃんけん
		while (data_list.size() > 1) {
			left_player = data_list.get(get_random(data_list.size()));
			right_opponent = data_list.get(get_random(data_list.size()));

			// 同じ名前の対戦相手の場合再セット
			if (left_player.equals(right_opponent)) {
				continue;

				// プレイヤーがplayer2側になった場合中身を入れ替え処理
			} else if (right_opponent.equals(player_name)) {
				String copy = right_opponent;
				right_opponent = left_player;
				left_player = copy;
			}

			// 対戦後の残りの挑戦者一覧表示
			int data_wins_all = data_list.size();
			System.out.println(String.format("\n残り%d名です。", data_wins_all));
			System.out.println(data_list);

			// 勝者リストにデータがあれば表示する。
			if (wins_list.size() > 0) {
				int wins = wins_list.size();
				System.out.println(String.format("\n勝ち上がったのは%d名です！", wins));
				System.out.println(wins_list);
			}

			// 対戦者の発表
			System.out.println(String.format("\n対戦は%sさん VS %sさんです。", left_player, right_opponent));
			System.out.println("\n-----------------対戦開始------------------");

			// 対戦処理
			String match_result = match_processing(left_player, right_opponent, player_name, data_list, wins_list,
					loses_list);

			// 今回の試合結果
			System.out.println(String.format("\n%s\n", match_result));
			System.out.println("-----------------対戦終了------------------");

			// player1がゲームをしてる本人なら処理を一旦停止する。
			if (player_name.equals(left_player)) {

				// 直前の入力でエンターなどの部分が残っているため一旦ここで全部読み込む
				if(sc.hasNextLine()) {
					sc.nextLine();
				}
				
				while (true) {
					System.out.println("\n勝敗を確認出来たらエンターを押してください");
					String enter = sc.nextLine();
					if("".equals(enter)) {
						break;
					}else {
						System.out.println("エンターのみ押してください");
					}
				}
			}
		}
	}

	/**
	 * じゃんけん選択受付
	 * */
	public int selection_reception() throws Exception {
		int player_imput = 0; // じゃんけんの入力結果が入る

		// 入力規制 1から3で1文字のみ入力受付する設定
		String select = "[1-3 １-３]{1}";
		Pattern pattern = Pattern.compile(select);

		// 入力されるまでループ
		while (player_imput == 0) {

			// プレイヤーのじゃんけん選択
			System.out.println("じゃんけんの選択を数字で入力してください");
			System.out.println("1:グー  2:チョキ  3:パー");

			// 入力規制処理
			if (sc.hasNext(pattern)) {

				// 入力受付処理
				player_imput = sc.nextInt();

				// 入力されたのが1～3以外の場合入力を再度求める
			} else {
				System.out.println("1～3の数字を入力してください");
				sc.next();
			}
		}
		return player_imput;
	}

	/**
	 * 勝敗数で再度試合or終了の判定
	 * 引数		left_player 	: 左側のプレイヤー 
	 * 			right_opponent  : 右側の対戦相手
	 * 			player_name		: プレイヤーの名前
	 * 			data_list		: 参加者リスト
	 * 			wins_list		: 勝者リスト
	 * */
	public String match_processing(String left_player, String right_opponent, String player_name,
			List<String> data_list, List<String> wins_list, List<String> loses_list) throws Exception {
		String player_result = null; // 結果を格納
		int input = 0; // じゃんけんの入力結果・プレイヤーではない場合は自動で入力される
		int wins_count = 0; // 勝利数
		int loses_count = 0; // 敗北数

		// 結果が変わるまでループ
		while (player_result == null) {

			// 対戦相手のじゃんけん選択
			int npc_select = opponent_selection();

			// プレイヤーなのか判定、違う場合自動で決まる。
			if (left_player.equals(player_name)) {
				input = selection_reception();

				// プレイヤー以外は自動で、じゃんけん選択
			} else {
				input = opponent_selection();
			}

			// じゃんけんの選択結果をplayer1側に入れる。
			String my_select_string = string_conversion(input);
			System.out.println(String.format("%sさんは%sを出しました", left_player, my_select_string));

			// player2側のじゃんけん選択を表示
			String npc_select_string = string_conversion(npc_select);
			System.out.println(String.format("%sさんは%sでした", right_opponent, npc_select_string));

			// player1とplayer2のじゃんけん選択結果を比較
			String result = match_result(input, npc_select);

			if (result.equals(WIN)) {
				wins_count++;
			} else if (result.equals(LOSE)) {
				loses_count++;
			}

			// 比較した結果を表示
			System.out.println(String.format("\n%s\n", result));

			// 勝利カウント2の場合
			if (wins_count == 2) {
				player_result = left_player + "さんが「 勝利 」しました！";

				// 勝利した人だけ入れるリスト
				wins_list.add(left_player);
				// 敗者を入れるリスト
				loses_list.add(right_opponent);

				// リストから各playerを削除
				remove_players(left_player, right_opponent, data_list);

				// 敗北カウント2の場合
			} else if (loses_count == 2) {
				player_result = left_player + "さんが「 敗北 」しました";

				// 勝利した人だけ入れるリスト
				wins_list.add(right_opponent);
				// 敗者を入れるリスト
				loses_list.add(left_player);

				// リストから各playerを削除
				remove_players(left_player, right_opponent, data_list);
			}
		}
		System.out.println(String.format("勝利数は%d回  敗北数は%d回でした。", wins_count, loses_count));
		return player_result;
	}

	/**
	 * 対戦結果を出す
	 * 引数 user_input	   : プレイヤーの選択
	 * 		right_opponent : コンピューター側の選択
	 * */
	public String match_result(int user_input, int right_opponent) {

		String result = LOSE;
		if (user_input == right_opponent) {
			result = AIKO;
		} else {
			if (user_input == ROCK && right_opponent == SCISSORS) {
				result = WIN;
			} else if (user_input == SCISSORS && right_opponent == PAPER) {
				result = WIN;
			} else if (user_input == PAPER && right_opponent == ROCK) {
				result = WIN;
			} else {
				return result;
			}
		}
		return result;
	}

	/**
	 * 対戦NPC側自動ランダムじゃんけん機能
	 * */
	public int opponent_selection() throws Exception {
		int select = 0;
		int[] hands = { ROCK, SCISSORS, PAPER };
		Random random = new Random();
		select = hands[random.nextInt(3)];
		return select;
	}

	/**
	 * 相手の選択を文字列化
	 * 引数		sc : 文字列に変換する値
	 * */
	public String string_conversion(int sc) throws Exception {
		String select = null;
		String[] hands = { "グー", "チョキ", "パー" };
		select = hands[(sc - 1)];
		return select;
	}

	/**
	 * 参加人数を受け取りその分名前を生成
	 *  引数	quantity 	 : 参加者の量
	 *  		player_name	 : 名前
	 *  		data_list	 : 参加者リスト
	 **/
	public List<String> create_participant(int quantity, String player_name, List<String> data_list) throws Exception {
		// リストにまずplayerを追加する。
		data_list.add(player_name);

		// 参加者の人数を受け取ってrandomName()で1人づつ生成していく。
		for (int i = 0; i < quantity; i++) {
			data_list.add(create_randomName());
		}
		return data_list;
	}

	/**
	 * ランダムで名前を取得
	 */
	public String create_randomName() throws Exception {

		String[] myoujilist = { "佐藤", "鈴木", "高橋", "田中", "渡辺", "伊藤", "山本", "中村", "小林", "加藤", "吉田", "山田", "佐々木", "山口",
				"斎藤", "松本", "井上", "木村", "林", "清水", "山崎", "森", "阿部", "池田", "橋本", "山下", "石川", "中島", "前田", "藤田",
				"小川", "後藤", "岡田", "長谷川", "村上", "近藤", "石井", "齊藤", "坂本", "遠藤", "青木", "藤井", "西村", "福田", "太田", "三浦",
				"岡本", "松田", "中川", "中野", "原田", "小野", "田村", "竹内", "金子", "和田", "中山", "藤原", "石田", "上田", "森田", "原",
				"柴田", "酒井", "工藤", "横山", "宮崎", "宮本", "内田", "高木", "安藤", "谷口", "大野", "丸山", "今井", "高田", "藤本", "武田",
				"村田", "上野", "杉山", "増田", "平野", "大塚", "千葉", "久保", "松井", "小島", "岩崎", "桜井", "野口", "松尾", "野村", "木下",
				"菊地", "佐野", "大西", "杉本", "新井", "浜田", "菅原", "市川", "水野", "小松", "島田", "古川", "小山", "高野", "西田", "菊池",
				"山内", "西川", "五十嵐", "北村", "安田", "中田", "川口", "平田", "川崎", "飯田", "吉川", "本田", "久保田", "沢田", "辻", "関",
				"吉村", "渡部", "岩田", "中西", "服部", "樋口", "福島", "川上", "永井", "松岡", "田口", "山中", "森本", "土屋", "矢野", "広瀬",
				"秋山", "石原", "松下", "大橋", "松浦", "吉岡", "小池", "馬場", "浅野", "荒木", "大久保", "野田", "小沢", "田辺", "川村", "星野",
				"黒田", "堀", "尾崎", "望月", "永田", "熊谷", "内藤", "松村", "西山", "大谷", "平井", "大島", "岩本", "片山", "本間", "早川",
				"横田", "岡崎", "荒井", "大石", "鎌田", "成田", "宮田", "小田", "石橋", "篠原", "須藤", "河野", "大沢", "小西", "南", "高山",
				"栗原", "伊東", "松原", "三宅", "福井", "大森", "奥村", "岡", "内山", "片岡"
		};
		String[] namaelist = { "大輔", "誠", "直樹", "亮", "剛", "大介", "学", "健一", "健", "哲也", "聡", "健太郎", "洋平", "淳", "竜也",
				"崇", "翔太", "拓也", "健太", "翔", "達也", "雄太", "翔平", "大樹", "大輔", "和也", "達也", "翔太", "徹", "哲也", "秀樹", "英樹",
				"浩二", "健一", "博", "博之", "修", "大輝", "拓海", "海斗", "大輔", "大樹", "翔太", "大輝", "翼", "拓海", "直人", "康平",
				"達也", "駿", "雄大", "亮太", "拓也", "大貴", "亮太", "拓哉", "雄大", "誠", "隆", "茂", "豊", "明", "浩", "進", "勝",
				"洋子", "恵子", "京子", "幸子", "和子", "久美子", "由美子", "裕子", "美智子", "悦子", "智子", "久美子", "陽子", "理恵", "真由美",
				"香織", "恵", "愛", "優子", "智子", "裕美", "真由美", "めぐみ", "美穂", "純子", "美紀", "彩", "美穂", "成美", "沙織", "麻衣",
				"舞", "愛美", "瞳", "彩香", "麻美", "沙織", "麻衣", "由佳", "あゆみ", "友美", "麻美", "裕子", "美香", "恵美", "直美", "由美",
				"陽子", "直子", "未来", "萌", "美咲", "亜美", "里奈", "菜々子", "彩花", "遥", "美咲", "明日香", "真由", "楓", "奈々", "彩花",
				"優花", "桃子", "美咲", "佳奈", "葵", "菜摘", "桃子", "茜", "明美", "京子", "恵子", "洋子", "順子", "典子"
		};

		// ランダム関数で数字を作り、その数字にリスト内数量を掛ける。小数点以下を切り捨てた数字=配列位置の漢字が選択される。
		String myouji = myoujilist[get_random(myoujilist.length)];
		String namae = namaelist[get_random(namaelist.length)];
		String full_name = myouji + " " + namae;
		return full_name;
	}
}
