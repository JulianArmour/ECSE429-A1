package ca.mcgill.ecse429;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class CopyFile {
    public static void main(String[] args) throws IOException {
        Path dirPath = FileSystems.getDefault().getPath("C:\\Users\\Julian\\Desktop\\someDir");
        if (Files.notExists(dirPath)) {
            Files.createDirectory(dirPath);
        }
        System.out.println(dirPath.getFileName().toString());
        Files.copy(new File("C:\\Users\\Julian\\Desktop\\test.java").toPath(),
                new File("C:\\Users\\Julian\\Desktop\\someDir\\test-cpy.java").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }
}
