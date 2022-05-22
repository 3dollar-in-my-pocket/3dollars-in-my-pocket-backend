package com.depromeet.threedollar.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

import org.springframework.util.StringUtils;

import com.depromeet.threedollar.common.exception.model.InternalServerException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProcessUtils {

    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isRunningPort(int port) throws IOException {
        return isRunning(executeGrepProcessCommand(port));
    }

    public static int findAvailableRandomPort() {
        try (ServerSocket server = new ServerSocket(0)) {
            server.setReuseAddress(true);
            return server.getLocalPort();
        } catch (IOException e) {
            throw new InternalServerException(String.format("사용 가능한 랜덤 포트를 찾는 중 에러가 발생했습니다. message: (%s)", e.getMessage()));
        }
    }

    private static Process executeGrepProcessCommand(int port) throws IOException {
        // 윈도우일 경우
        if (isWindows()) {
            throw new InternalServerException("프로세스 실행 여부 확인 메소드는 Windows OS는 지원하지 않습니다");
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
            throw new InternalServerException(String.format("프로세스 사용 여부를 확인 중 에러가 발생하였습니다. message: (%s)", e.getMessage()));
        }
        return StringUtils.hasLength(pidInfo.toString());
    }

}
