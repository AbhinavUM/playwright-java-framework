package utils;

import io.qameta.allure.Allure;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;

public class AllureUtil {

    public static void attachFile(String name, Path path) {
        try (InputStream is = new FileInputStream(path.toFile())) {
            Allure.addAttachment(name, is);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to attach: " + name);
        }
    }
}