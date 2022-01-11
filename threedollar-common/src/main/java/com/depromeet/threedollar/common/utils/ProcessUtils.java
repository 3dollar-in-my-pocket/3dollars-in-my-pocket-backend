package com.depromeet.threedollar.common.utils;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProcessUtils {

    private static final int LOWEST_PORT = 10000;
    private static final int HIGHEST_PORT = 65535;

    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isRunningPort(int port) throws IOException {
        return isRunning(executeGrepProcessCommand(port));
    }

    public static int findAvailableRandomPort() throws IOException {
        for (int port = LOWEST_PORT; port <= HIGHEST_PORT; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }
        throw new InternalServerException(String.format("사용가능한 포트를 찾을 수 없습니다. (%s ~ %s)", LOWEST_PORT, HIGHEST_PORT));
    }

    private static Process executeGrepProcessCommand(int port) throws IOException {
        // 윈도우일 경우
        if (isWindows()) {
            String command = String.format("netstat -nao | find \"LISTEN\" | find \"%d\"", port);
            String[] shell = {"cmd.exe", "/y", "/c", command};
            return Runtime.getRuntime().exec(shell);
        }
        String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
        String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }

    private static boolean isWindows() {
        return OS.contains("win");
    }

    private static boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        } catch (Exception e) {
            throw new InternalServerException("포트를 사용 여부를 확인 중 에러가 발생하였습니다.");
        }
        return StringUtils.hasLength(pidInfo.toString());
    }

}
