package com.suraj.graphql.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public final class FileUtils {
    private static FileUtils INSTANCE;

    private FileUtils() {
    }

    public static synchronized FileUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileUtils();
        }
        return INSTANCE;
    }

    public void cleanAndRecreateOutputDir(final File outputDir, final String packageName) throws IOException {
        // during validation, we ensure that the output directory exists
        // if output directory is empty,  create the package
        String[] fileNames = outputDir.list();
        String packageWithSystemDirFormat = packageWithDirectoryFormat(packageName);
        File packageDirectory = new File(outputDir, packageWithSystemDirFormat);
        if (fileNames == null || fileNames.length == 0) {
            //create package and return
            createPackageDirectory(packageName, packageDirectory);
            return;
        }
        // if output directory has contents, check whether package exists, else do nothing
        // if package exists, delete all files inside the package
        if (!packageDirectory.exists()) {
            createPackageDirectory(packageName, packageDirectory);
        } else {
            File[] filesInPackageDir = packageDirectory.listFiles();
            if (filesInPackageDir != null) {
                Arrays.stream(filesInPackageDir).forEach(File::delete);
            }
            // todo: delete the entire package from output dir

        }
    }

    private void createPackageDirectory(final String packageName, final File packageDirectory) throws IOException {
        if (!(packageDirectory.mkdirs() && packageDirectory.exists())) {
            throw new IOException("Unable to create package : " + packageName);
        }
    }

    private String packageWithDirectoryFormat(final String packageName) {
        return Collections.list(new StringTokenizer(packageName, ".")).stream()
                .map(token -> (String) token)
                .filter(t -> t.trim().isEmpty())
                .collect(Collectors.joining(File.separator));
    }
}
