package com.github.surajcm.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * Utility class for file system operations related to POJO generation.
 *
 * <p>This class provides methods for managing the output directory structure,
 * including cleaning existing generated files and creating package directories.</p>
 *
 * <p>This class follows the singleton pattern. Use {@link #getInstance()} to obtain
 * an instance.</p>
 *
 * @author Suraj Muraleedharan
 * @since 1.0.0
 */
public final class FileUtils {

    private static FileUtils instance;

    private FileUtils() {
    }

    /**
     * Returns the singleton instance of FileUtils.
     *
     * @return the FileUtils singleton instance
     */
    public static synchronized FileUtils getInstance() {
        if (instance == null) {
            instance = new FileUtils();
        }
        return instance;
    }

    /**
     * Cleans and recreates the output directory for the specified package.
     *
     * <p>This method performs the following operations:</p>
     * <ol>
     *     <li>If the output directory is empty, creates the package directory structure</li>
     *     <li>If the package directory already exists, deletes all files within it</li>
     *     <li>Cleans up empty parent directories up to the output directory</li>
     *     <li>Creates the package directory structure</li>
     * </ol>
     *
     * @param outputDir the root output directory where generated files will be placed
     * @param packageName the Java package name (e.g., "com.example.generated")
     * @throws IOException if directory creation fails or file deletion encounters an error
     */
    public void cleanAndRecreateOutputDir(final File outputDir,
                                          final String packageName) throws IOException {
        // if output directory is empty,  create the package
        String packageWithSystemDirFormat = packageWithDirectoryFormat(packageName);
        File packageDirectory = new File(outputDir, packageWithSystemDirFormat);
        // if output directory has contents, check whether package exists, else do nothing
        // if package exists, delete all files inside the package
        if (!isEmptyDirectory(outputDir) && packageDirectory.exists()) {
            deleteFilesInPackageDirectory(packageDirectory);
            deletePackage(outputDir, packageDirectory);
        }
        createPackageDirectory(packageName, packageDirectory);
    }

    private void deleteFilesInPackageDirectory(final File packageDirectory) {
        File[] filesInPackageDir = packageDirectory.listFiles();
        if (filesInPackageDir != null) {
            // log in debug mode about the files being deleted, with file names
            Arrays.stream(filesInPackageDir).forEach(File::delete);
        }
    }

    private boolean isEmptyDirectory(final File oneDirectory) {
        String[] fileNames = oneDirectory.list();
        return fileNames == null || fileNames.length == 0;
    }

    // todo: delete the entire package from output dir
    private void deletePackage(final File outputDir, final File packageDirectory) {
        File currentDirectory = packageDirectory;
        boolean flag = true;
        while (isSubDirectory(outputDir, currentDirectory)
                && !isSameDirectory(outputDir, currentDirectory)
                && flag) {
            currentDirectory = packageDirectory.getParentFile();
            if (isEmptyDirectory(currentDirectory)) {
                deleteFilesInPackageDirectory(currentDirectory);
            } else {
                flag = false;
            }
        }
    }

    private boolean isSubDirectory(final File parentDir, final File childDir) {
        boolean isSubdirectory = false;
        try {
            isSubdirectory = parentDir.getCanonicalPath().contains(childDir.getCanonicalPath());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return isSubdirectory;
    }

    private boolean isSameDirectory(final File outputDir, final File packageDirectory) {
        boolean isSameDirectory = false;
        try {
            isSameDirectory = packageDirectory.getCanonicalPath().equalsIgnoreCase(
                    outputDir.getCanonicalPath());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return isSameDirectory;
    }

    private void createPackageDirectory(final String packageName, final File packageDirectory) throws IOException {
        if (!(packageDirectory.mkdirs() && packageDirectory.exists())) {
            throw new IOException("Unable to create package : " + packageName);
        }
    }

    private String packageWithDirectoryFormat(final String packageName) {
        return Collections.list(new StringTokenizer(packageName, "."))
                .stream()
                .map(token -> (String) token)
                .filter(t -> !t.trim().isEmpty())
                .collect(Collectors.joining(File.separator));
    }
}
