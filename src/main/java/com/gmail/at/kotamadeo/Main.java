package com.gmail.at.kotamadeo;

import com.gmail.at.kotamadeo.game.GameProgress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    private static int counterBeforeFileCreation;
    private static int counterAfterFileCreation;
    private static Path gameDirectoryPath = Path.of("\\Module_1-Stream_API-Task_1.3.2-Install\\Games\\savegames");
    private static String zipName = "\\savegames.zip";

    public static void main(String[] args) {
        List<GameProgress> gameProgresses = new ArrayList<>();
        gameProgresses.add(new GameProgress(10, 20, 30, 2000));
        gameProgresses.add(new GameProgress(20, 10, 10, 200));
        gameProgresses.add(new GameProgress(120, 6, 48, 6200.25));
        List<String> pathsSave = new ArrayList<>();
        for (GameProgress gameProgress : gameProgresses) {
            try {
                saveGame(gameDirectoryPath, gameProgress, pathsSave);
                toZip(gameDirectoryPath, pathsSave);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        deleteFileByPath(pathsSave);
    }

    private static void saveGame(Path path, GameProgress data, List<String> pathSaves) throws IOException {
        Files.createFile(Path.of(path + "\\save" + (++counterBeforeFileCreation) + ".dat"));
        String pathForSave = path + "\\save" + (++counterAfterFileCreation) + ".dat";
        try (ObjectOutputStream objectOutputStream =
                     new ObjectOutputStream(new FileOutputStream(pathForSave))) {
            objectOutputStream.writeObject(data);
            pathSaves.add(pathForSave);
        }
    }

    private static void toZip(Path path, List<String> files) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(path + zipName))) {
            for (String file : files) {
                zipOutputStream.putNextEntry(new ZipEntry(new File(file).getName()));
                byte[] bytes = Files.readAllBytes(Paths.get(file));
                zipOutputStream.write(bytes, 0, bytes.length);
            }
        }
    }

    private static void deleteFileByPath(List<String> files) {
        for (String file : files) {
            try {
                Files.deleteIfExists(Path.of(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}