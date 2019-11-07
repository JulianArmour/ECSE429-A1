package ca.mcgill.ecse429;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class CopyFile {
    public static void main(String[] args) throws IOException {
        Files.copy(new File("C:\\Users\\Julian\\Desktop\\test.java").toPath(),
                new File("C:\\Users\\Julian\\Desktop\\test-cpy.java").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }
}
