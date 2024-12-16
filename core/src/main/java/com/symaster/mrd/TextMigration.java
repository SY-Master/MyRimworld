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
    private static final String txtFile = "E:\\projects\\symaster\\MyRimworld2\\assets\\ch\\text.txt";
    private static final List<String> suffix = Arrays.asList(".java");
    private static final String flag = "GdxText.val(";
    private static final String flagTo = "GdxText.get(";

    public static void main(String[] args) {

        Map<String, String> map;
        Map<String, String> map2;

        File file1 = new File(txtFile);
        if (!file1.isFile()) {
            File parentFile = file1.getParentFile();
            if (!parentFile.isDirectory() && !parentFile.mkdirs()) {
                throw new RuntimeException(parentFile.getAbsolutePath() + " is not a directory");
            }
            map = new HashMap<>();
            map2 = new HashMap<>();
        } else {

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(txtFile));) {
                List<String> collect = bufferedReader.lines().collect(Collectors.toList());
                map = new HashMap<>();
                map2 = new HashMap<>();

                for (String s : collect) {
                    int i = s.indexOf("=");
                    String k1 = s.substring(0, i);
                    String k2 = s.substring(i + 1);
                    map.put(k1, k2);
                    map2.put(k2, k1);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        directory(new File(folder), map, map2);

        List<String> lines = new ArrayList<>();
        for (String s : map.keySet()) {
            lines.add(s + "=" + map.get(s));
        }

        String collect = lines.stream().collect(Collectors.joining(System.lineSeparator()));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file1));) {
            bw.write(collect);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Integer[] readRange(Integer startIndex, char[] charArray) {
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

    private static List<Integer> findIndex(String s2) {
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

    private static void directory(File f, Map<String, String> map, Map<String, String> map2) {

        File[] files = f.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                directory(file, map, map2);
            } else if (file.isFile()) {
                file(file, map, map2);
            }
        }
    }

    private static void file(File f, Map<String, String> map, Map<String, String> map2) {
        String name = f.getName();

        if (name.startsWith("TextMigration")) {
            return;
        }

        if (!isMatch(name)) {
            return;
        }

        try (FileReader fileReader = new FileReader(f);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            List<String> collect = bufferedReader.lines().collect(Collectors.toList());

            boolean match = false;
            for (int iii = 0; iii < collect.size(); iii++) {
                String e = collect.get(iii);
                if (e.contains(flag)) {
                    List<Integer> indexList = findIndex(e);
                    if (indexList.isEmpty()) {
                        continue;
                    }

                    char[] charArray = e.toCharArray();

                    String s2 = e;
                    for (Integer i : indexList) {
                        Integer[] integers = readRange(i, charArray);
                        if (integers[0] == null || integers[1] == null) {
                            continue;
                        }

                        if (integers[0] + 1 >= integers[1]) {
                            continue;
                        }

                        String t1 = s2.substring(0, integers[0] + 1);
                        String t2 = s2.substring(integers[1]);
                        String txt = s2.substring(integers[0] + 1, integers[1]);

                        String key = map2.get(txt);
                        if (key == null) {
                            String string = UUID.randomUUID().toString();
                            map2.put(txt, string);
                            map.put(string, txt);
                            s2 = t1 + string + t2;
                        } else {
                            s2 = t1 + key + t2;
                        }
                    }

                    String replace = s2.replace(flag, flagTo);

                    collect.set(iii, replace);

                    match = true;
                }
            }

            if (match) {
                String collect1 = collect.stream()
                        .collect(Collectors.joining(System.lineSeparator()));

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(f));) {
                    bw.write(collect1);
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
