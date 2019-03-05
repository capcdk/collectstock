//package business.leetcode;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 9.正则表达式匹配
// */
//class _10_Question {
//
//    static char pTimes = '*';
//    static char pAny = '.';
//    String s;
//    int sLength;
//
//    public static void main(String[] args) {
//        _10_Question question = new _10_Question();
//        System.out.println(question.isMatch("mississiippi", "mis*is.*ip*."));
//    }
//
//    public boolean isMatch(String s, String p) {
//        this.s = s;
//        this.sLength = s.length();
//
//        List<Pattern> patternList = new ArrayList<>();
//
//        int pLength = p.length();
//        char tmp;
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < pLength; i++) {
//            tmp = p.charAt(i);
//            if (tmp == pTimes) {
//                tmp = sb.charAt(sb.length() - 1);
//                sb.deleteCharAt(sb.length() - 1);
//                patternList.add(new Pattern(PatternChar.pTerm, sb.toString()));
//                patternList.add(new Pattern(PatternChar.pTimes, Character.toString(tmp)));
//                sb = new StringBuilder();
//            } else {
//                sb.append(tmp);
//            }
//        }
//        patternList.add(new Pattern(PatternChar.pTerm, sb.toString()));
//
//        int si = 0;
//        Pattern pattern;
//        for (int i = 0; i < patternList.size(); i++) {
//        }
//        return true;
//    }
//
//    private boolean isTerms(List<Pattern> patternList, int si) {
//        Pattern pattern;
//        for (int i = 0; i < patternList.size(); i++) {
//            pattern = patternList.get(i);
//            switch (pattern.pattern) {
//                case pTimes: {
//                    isTerms()
//                    break;
//                }
//                case pTerm: {
//                    if (isTerms(pattern, si) == null) {
//                        return false;
//                    }
//                    break;
//                }
//            }
//        }
//    }
//
//    private Integer isTerms(Pattern pattern, int si) {
//        String patternContent = pattern.content;
//        int pcLength = patternContent.length();
//
//        boolean isTimes = pattern.pattern == PatternChar.pTimes;
//        if (pcLength <= 0) {
//            return si;
//        }
//        if (isTimes) {
//            char c = patternContent.charAt(0);
//            while (si < sLength) {
//                if (s.charAt(si++) != c) {
//                    return --si;
//                }
//            }
//            return si;
//        } else {
//            for (int pi = 0; pi < pcLength; pi++) {
//                if (si >= sLength || (patternContent.charAt(pi) != pAny && patternContent.charAt(pi) != s.charAt(si++))) {
//                    return null;
//                }
//            }
//        }
//        return si;
//    }
//
//    enum PatternChar {
//        pTimes, pTerm
//    }
//
//    static class Pattern {
//        PatternChar pattern;
//        String content;
//
//        Pattern(PatternChar pattern, String content) {
//            this.pattern = pattern;
//            this.content = content;
//        }
//    }
//}