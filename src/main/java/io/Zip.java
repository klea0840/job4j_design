package io;

import java.io.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.Objects.isNull;

public class Zip {
    public static void packFiles(List<File> sources, File target, String parentDir) {

        if (isNull(sources)) {
            throw new IllegalArgumentException("Source does not exist");
        }

        String entryPath;
        System.out.println("Zipping files");
        try (ZipOutputStream zips = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target)))) {
            for (var file: sources) {

                // Находим относительный путь (путь к файлу относительно родительской папки)
                // Необходимо для корректной архивации
                if (file.getPath().startsWith(parentDir)) {
                    entryPath = file.getPath().substring(parentDir.length());
                } else {
                    entryPath = file.getPath();
                }

                zips.putNextEntry(new ZipEntry(entryPath));
                try (BufferedInputStream outs = new BufferedInputStream(new FileInputStream(file))) {
                    zips.write(outs.readAllBytes());
                    zips.closeEntry();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void packSingleFile(File source, File target) {
        try (ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target)))) {
            zip.putNextEntry(new ZipEntry(source.getPath()));
            try (BufferedInputStream out = new BufferedInputStream(new FileInputStream(source))) {
                zip.write(out.readAllBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ArgsName check = ArgsName.of(args);
        System.out.println(check.get("d"));
        System.out.println(check.get("o"));
//        String[] end = check.get("e").split("\\.");
//        System.out.println(Arrays.toString(end));
        Predicate<Path> condition = p -> p.toFile().getName().endsWith(check.get("e"));
        Predicate<Path> noNcondition = condition.negate();

        List<Path> sources = new ArrayList<>();

        try {
            sources.addAll(Search.search(Paths.get(check.get("d")),
                    noNcondition));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("sources: " + sources);

        List<File> files = new ArrayList<>();
        for (var path: sources) {
            files.add(path.toFile());
        }
        File target = new File(check.get("o"));
//
        packFiles(files, target, check.get("d"));
//        packSingleFile(new File("pom.xml"), new File("output111.zip"));
    }
}
