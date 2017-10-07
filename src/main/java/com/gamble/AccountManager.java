package com.gamble;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccountManager {

    public static void main(String[] args) throws IOException, InterruptedException {
        String path = "/Users/lingda/Downloads/bjracingcar";
        Integer expireYear = 2017;
        Integer expireMonth = 11;
        Integer expireDay = 5;
        String bossEmail = "fwj0331@yahoo.com";

        Map<String, String> accountMap = new HashMap<>();
//        accountMap.put("dear001", "master");
//        accountMap.put("dd9902", "135-6810");
//        accountMap.put("dd9903", "147-258");
//        accountMap.put("dd9905", "147-369");
        accountMap.put("dd1002", ModeEnum.BASIC.getMode());
        accountMap.put("dd1003", ModeEnum.MODE_246_579.getMode());
//        accountMap.put("dd1005", "basic");
//        accountMap.put("dd1006", "basic");
//        accountMap.put("dd1007", "basic");

        List<String> dirList = new ArrayList<>();

        for (Map.Entry<String, String> account : accountMap.entrySet()) {
            String accountDir = createDirectory(path, account.getKey(), account.getValue());
            dirList.add(accountDir.substring(accountDir.lastIndexOf("/") + 1));
//          bjracingcar
            System.out.println("[git clone https://github.com/darlingtld/BeijingRacingCar.git] in " + accountDir);
            Process process = Runtime.getRuntime().exec("git clone -b cloudapex https://github.com/darlingtld/BeijingRacingCar.git", null, new File(accountDir));
            process.waitFor();

            System.out.println("[rewrite application.properties] ");
            Map<String, String> replaceMap = new HashMap<>();
            replaceMap.put("account", account.getKey());
            replaceMap.put("gamble.expiration.year", expireYear.toString());
            replaceMap.put("gamble.expiration.month", expireMonth.toString());
            replaceMap.put("gamble.expiration.day", expireDay.toString());
            replaceMap.put("gamble.notification.email", bossEmail);

            replaceMap.put("gamble.bet.mimic", "false");
            replaceMap.put("server.port", "26000");
            replaceMap.put("spring.data.mongodb.port", "27000");
            rewriteApplicationProperties(String.valueOf(Paths.get(accountDir, "BeijingRacingCar", "src", "main", "resources", "application.properties")), replaceMap);

            System.out.println("[gradle build -x test] in " + String.valueOf(Paths.get(accountDir, "BeijingRacingCar")));
            Process process1 = Runtime.getRuntime().exec("gradle build -x test", null, new File(String.valueOf(Paths.get(accountDir, "BeijingRacingCar"))));
            process1.waitFor();

            System.out.println("[cp build/libs/BeijingRacingCar-0.0.1-SNAPSHOT.jar ../] in " + String.valueOf(Paths.get(accountDir, "BeijingRacingCar")));
            Process process2 = Runtime.getRuntime().exec("cp build/libs/BeijingRacingCar-0.0.1-SNAPSHOT.jar ../", null, new File(String.valueOf(Paths.get(accountDir, "BeijingRacingCar"))));
            process2.waitFor();

            System.out.println("[cp /Users/lingda/IdeaProjects/GambleAccountManager/src/main/resources/" + account.getValue() + "/GambleConsole.jar ../] in " + String.valueOf(Paths.get(accountDir, "BeijingRacingCar")));
            Process process3 = Runtime.getRuntime().exec("cp /Users/lingda/IdeaProjects/GambleAccountManager/src/main/resources/" + account.getValue() + "/GambleConsole.jar ../", null, new File(String.valueOf(Paths.get(accountDir, "BeijingRacingCar"))));
            process3.waitFor();

            System.out.println("[cp /Users/lingda/IdeaProjects/GambleAccountManager/src/main/resources/chromedriver.exe ../] in " + String.valueOf(Paths.get(accountDir, "BeijingRacingCar")));
            Process process4 = Runtime.getRuntime().exec("cp /Users/lingda/IdeaProjects/GambleAccountManager/src/main/resources/chromedriver.exe ../", null, new File(String.valueOf(Paths.get(accountDir, "BeijingRacingCar"))));
            process4.waitFor();

            System.out.println("[write start_1_db] in " + String.valueOf(Paths.get(accountDir, "BeijingRacingCar")));
            writeStartDb(accountDir);

            System.out.println("[write start_2_app] in " + String.valueOf(Paths.get(accountDir, "BeijingRacingCar")));
            writeStartApp(accountDir);

            System.out.println("[write start_3_console] in " + String.valueOf(Paths.get(accountDir, "BeijingRacingCar")));
            writeStartConsole(accountDir);

            System.out.println("[delete BeijingRacingCar source] in " + String.valueOf(Paths.get(accountDir, "BeijingRacingCar")));
            deleteDirectory(new File(String.valueOf(Paths.get(accountDir, "BeijingRacingCar"))));


        }
        String zipCommand = "zip -r " + String.format("%s_%s.zip ", String.join("-", accountMap.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList())), String.format("%04d%02d%02d", expireYear, expireMonth, expireDay) + "到期") + String.join(" ", dirList);
        System.out.println("[create zip file] " + zipCommand);
        Process process5 = Runtime.getRuntime().exec(zipCommand, null, new File(path));
        process5.waitFor();
    }

    private static void writeStartDb(String accountDir) throws IOException {
        String line = "\"C:\\Program Files\\MongoDB\\Server\\3.4\\bin\\mongod.exe\" --dbpath \"C:\\Users\\USER\\Downloads\\db\" --port 27000";
        BufferedWriter bw = new BufferedWriter(new FileWriter(String.valueOf(Paths.get(accountDir, "start_1_db.bat"))));
        bw.write(line);
        bw.flush();
        bw.close();
    }

    private static void writeStartApp(String accountDir) throws IOException {
        String line = "\"C:\\Program Files\\Java\\jdk1.8.0_144\\bin.\\java.exe\" -jar -Djava.net.preferIPv4Stack=true \"C:\\Users\\USER\\Downloads\\BeijingRacingCar-0.0.1-SNAPSHOT.jar\" C:\\Users\\USER\\Downloads\\chromedriver.exe";
        BufferedWriter bw = new BufferedWriter(new FileWriter(String.valueOf(Paths.get(accountDir, "start_2_app.bat"))));
        bw.write(line);
        bw.flush();
        bw.close();
    }

    private static void writeStartConsole(String accountDir) throws IOException {
        String line = "\"C:\\Program Files\\Java\\jdk1.8.0_144\\bin.\\java.exe\" -jar -Djava.net.preferIPv4Stack=true \"C:\\Users\\USER\\Downloads\\GambleConsole.jar\"";
        BufferedWriter bw = new BufferedWriter(new FileWriter(String.valueOf(Paths.get(accountDir, "start_3_console.bat"))));
        bw.write(line);
        bw.flush();
        bw.close();
    }

    private static void rewriteApplicationProperties(String s, Map<String, String> replaceMap) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(s));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            String[] kv = line.split("=");
            if (kv.length == 2) {
                if (replaceMap.containsKey(kv[0])) {
                    sb.append(kv[0] + "=" + replaceMap.get(kv[0])).append("\r\n");
                } else {
                    sb.append(line).append("\r\n");
                }
            } else {
                sb.append(line).append("\r\n");
            }
        }
        br.close();
        BufferedWriter bw = new BufferedWriter(new FileWriter(s));
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    private static String createDirectory(String path, String account, String mode) {
        File accountFile = new File(String.valueOf(Paths.get(path, account + "-" + mode)));
        System.out.println("create directory " + accountFile.getAbsolutePath());
        if (accountFile.exists()) {
            deleteDirectory(accountFile);
        }
        accountFile.mkdir();
        return accountFile.getAbsolutePath();
    }

    private static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }
        return (directory.delete());
    }
}
