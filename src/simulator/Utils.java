package simulator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Utils {

    public static String[] readFileToArray(File inputFile) throws IOException {
        Path filePath = inputFile.toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        return stringList.toArray(new String[]{});
    }
}
