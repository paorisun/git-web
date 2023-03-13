public class Main1 {
    final static String[] CHO = {"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ",
            "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};

    final static String[] JOONG = {"ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ",
            "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ"};

    final static String[] JONG = {"", "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ", "ㄺ", "ㄻ", "ㄼ",
            "ㄽ", "ㄾ", "ㄿ", "ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};

    public static void main(String[] args) {
        //추천할 문장의 개수
        int suggestions = 3;
        //추천된 문장의 인덱스
        int[] suggested = new int[suggestions];
        //추천문장 후보
        String[] sentences = {
                "한글 타자연습 문장입니다.",
                "타자 연습은 지루하다.",
                "범도 제 말하면 온다.",
                "단단한 땅에 물이 괸다.",
                "온",
        };
        //추천문장에서 찾은 오타의 개수
        int[] found_mistakes = new int[sentences.length];
        //오타 배열
        String[] first_mistakes = {"", "", "", "ㅁ", "ㅈ"};
        String[] mid_mistakes = {"", "", "", "ㅜ", "ㅏ"};
        String[] last_mistakes = {"ㄴ", "", "", "ㄴ", "ㅇ"};
        //추천 문장 순회
        for (int i = 0; i < sentences.length; i++) {
            //추천 문장 분해
            int mistakes = 0;
            String sentence = sentences[i];
            for (int j = 0; j < sentence.length(); j++) {
                char sentence_char = sentence.charAt(j);
                //문자가 한글이 아닌 경우 무시
                if (sentence_char < '가' || sentence_char > '힣') continue;
                //추천문장 분해
                int sentence_base = sentence_char - 0xAC00;
                String sentence_first = CHO[(char) (sentence_base / 28 / 21)];
                String sentence_mid = JOONG[(char) (sentence_base / 28 % 21)];
                String sentence_last = JONG[(char) (sentence_base % 28)];
                //오타 배열 순회
                //오타 배열에서 중복되는 값은 무시해야됨
                boolean first_added = false;
                boolean mid_added = false;
                boolean last_added = false;
                for (int k = 0; k < first_mistakes.length; k++) {
                    //오타 개수 증가
                    if (!first_added && sentence_first.equals(first_mistakes[k])) {
                        mistakes++;
                        first_added = true;
                    }
                    if (!mid_added && sentence_mid.equals(mid_mistakes[k])) {
                        mistakes++;
                        mid_added = true;
                    }
                    if (!last_added && sentence_last.equals(last_mistakes[k])) {
                        mistakes++;
                        last_added = true;
                    }
                }
                //총 오타개수 저장
                found_mistakes[i] = mistakes;
            }
        }

        //추천 문장에서 오타개수가 많은 n개를 찾는다
        int count = 0;
        while (count < suggestions) {
            int max_mistakes = 0;
            int max_index = -1;
            Outer:
            for (int i = 0; i < found_mistakes.length; i++) {
                //이미 추천된 문장인지 확인.
                for (int j = 0; j < count; j++) {
                    if (suggested[j] == i) continue Outer;
                }
                //이미 추천된 문장 제외한 최댓값 찾기.
                int mistakes = found_mistakes[i];
                if (mistakes >= max_mistakes) {
                    max_mistakes = mistakes;
                    max_index = i;
                }
            }
            //최댓값을 못찾는 경우는 모든 문장이 추천된 경우
            if(max_index == -1) {
                break;
            }
            //추천된 문장에 추가.
            suggested[count++] = max_index;
        }

        //오타 개수 출력
        System.out.println("후보 문장 / 오타 개수");
        for(int i = 0;i < found_mistakes.length;i++) {
            System.out.printf("%s / %d\n", sentences[i], found_mistakes[i]);
        }

        //추천된 문장을 출력
        System.out.println("--------------------------------------------------");
        System.out.printf("아래는 추천된 상위 %d개의 문장입니다.\n", count);
        for (int i = 0; i < count; i++) {
            System.out.println(sentences[suggested[i]]);
        }
    }
}