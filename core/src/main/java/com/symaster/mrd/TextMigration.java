package com.symaster.mrd;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class TextMigration {

    private static final String folder = "E:\\projects\\symaster\\MyRimworld2\\core\\src\\main\\java\\com\\symaster\\mrd";
    private static final String txtFile = "E:\\projects\\symaster\\MyRimworld2\\assets\\language\\ch.txt";
    private static final List<String> suffix = Collections.singletonList(".java");
    private static final String flag = "GdxText.val(";

    public static void main(String[] args) {

        Map<String, String> languageMap;
        // Map<String, String> map2;

        File file1 = new File(txtFile);
        if (!file1.isFile()) {
            File parentFile = file1.getParentFile();
            if (!parentFile.isDirectory() && !parentFile.mkdirs()) {
                throw new RuntimeException(parentFile.getAbsolutePath() + " is not a directory");
            }
            languageMap = new HashMap<>();
            // map2 = new HashMap<>();
        } else {

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(txtFile))) {
                List<String> collect = bufferedReader.lines().collect(Collectors.toList());
                languageMap = new HashMap<>();
                // map2 = new HashMap<>();

                for (String s : collect) {
                    int i = s.indexOf("=");
                    String k1 = s.substring(0, i);
                    String k2 = s.substring(i + 1);
                    languageMap.put(k1, k2);
                    // map2.put(k2, k1);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        directory(new File(folder), languageMap);

        List<String> lines = new ArrayList<>();
        for (String s : languageMap.keySet()) {
            lines.add(s + "=" + languageMap.get(s));
        }

        String collect = lines.stream().collect(Collectors.joining(System.lineSeparator()));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file1))) {
            bw.write(collect);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Integer[] searchTextRange(Integer startIndex, char[] charArray) {
        Integer[] rtn = new Integer[2];

        for (int i = startIndex + flag.length(); i < charArray.length; i++) {
            if (rtn[0] == null) {
                if (charArray[i] == '"') {
                    rtn[0] = i;
                }
            } else if (charArray[i] == '"' && charArray[i - 1] != '\\') {
                rtn[1] = i;
                break;
            }
        }
        return rtn;
    }

    private static List<Integer> searchStartIndex(String s2) {
        char[] charArray1 = flag.toCharArray();
        char[] charArray = s2.toCharArray();

        List<Integer> rtn = new ArrayList<>();

        for (int i1 = 0; i1 < charArray.length; i1++) {
            if (charArray[i1] == charArray1[0]) {
                boolean match = true;

                for (int i = 0; i < charArray1.length; i++) {
                    if (charArray[i + i1] != charArray1[i]) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    rtn.add(i1);
                }
            }
        }
        return rtn;
    }

    private static void directory(File f, Map<String, String> languageMap) {

        File[] files = f.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                directory(file, languageMap);
            } else if (file.isFile()) {
                file(file, languageMap);
            }
        }
    }

    private static void file(File f, Map<String, String> languageMap) {
        String name = f.getName();

        if (name.startsWith("TextMigration")) {
            return;
        }

        if (!isMatch(name)) {
            return;
        }

        try (FileReader fileReader = new FileReader(f);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            for (String codeLine : bufferedReader.lines().collect(Collectors.toList())) {
                if (codeLine.contains(flag)) {

                    List<Integer> startIndexList = searchStartIndex(codeLine);
                    if (startIndexList.isEmpty()) {
                        continue;
                    }

                    char[] charArray = codeLine.toCharArray();

                    for (Integer startIndex : startIndexList) {
                        Integer[] textRange = searchTextRange(startIndex, charArray);
                        if (textRange[0] == null || textRange[1] == null) {
                            continue;
                        }

                        if (textRange[0] + 1 >= textRange[1]) {
                            continue;
                        }

                        String txt = codeLine.substring(textRange[0] + 1, textRange[1]);
                        languageMap.putIfAbsent(txt, txt);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isMatch(String name) {
        boolean match = false;
        for (String s : suffix) {
            if (name.endsWith(".java")) {
                match = true;
                break;
            }
        }
        return match;
    }

}
