package krypt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CryptCaesarApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите вариант режима: ");
        System.out.println("1 - Шифрование");
        System.out.println("2 - Расшифровка");
        System.out.println("3 - Криптоанализ методом brute force");

        int mode = scanner.nextInt();
        scanner.nextLine();

        if (mode == 1) {
            System.out.println("Введите текст для шифрования: ");
            String text = scanner.nextLine();

            System.out.println("Введите ключ для шифрования (целое число): ");
            int key = scanner.nextInt();

            String encryptText = encrypt(text, key);
            System.out.println("Зашифрованный текст " + encryptText);

            saveToFile(encryptText,"encrypted.txt");

        } else if (mode == 2) {
            System.out.println("Введите зашифрованный текст: ");
            String encryptedText = scanner.nextLine();

            System.out.println("Введите ключ, который указывали для зашифрованного текста: ");
            int key = scanner.nextInt();

            String decrypt = decrypt(encryptedText, key);
            System.out.println("Расшифрованный текст " + decrypt);

            saveToFile(decrypt, "decrypted.txt");

        } else if (mode == 3) {
            List<String> dictionary = List.of("молоко", "карта", "море", "корабль");

            System.out.println("Введите имя файла с зашифрованным текстом: ");
            String fileName = scanner.nextLine();
            String encryptedText = readToFile(fileName);
            bruteForceDecryptAndCheck(encryptedText, dictionary);

        }

        }

    public static List<Character> createRusAlphabet() {
        List<Character> rusAlphabet = new ArrayList<>();

        for (char ch = 'а'; ch <= 'я' ; ch++) {
            rusAlphabet.add(ch);
            rusAlphabet.add(Character.toUpperCase(ch));
        }
        char[] punctuation = {'.', ',', '\"', ':', '-', '!', ' '};

        for (char punct : punctuation) {
            rusAlphabet.add(punct);
        }
        return rusAlphabet;
    }

    public static String encrypt(String text, int key) {
        List<Character> cryptAlphabet = createRusAlphabet();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (cryptAlphabet.contains(ch)) {
                int index = cryptAlphabet.indexOf(ch);
                char shiftCh = cryptAlphabet.get((index + key) % cryptAlphabet.size());
                result.append(shiftCh);
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    public static String decrypt(String text, int key) {
        List<Character> cryptoAlphabet = createRusAlphabet();
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            int index = cryptoAlphabet.indexOf(ch);
            if (index != -1) {
                int decryptedIndex = (index - key + cryptoAlphabet.size()) % cryptoAlphabet.size();
                char decryptedChar = cryptoAlphabet.get(decryptedIndex >=0 ? decryptedIndex :
                                                        decryptedIndex + cryptoAlphabet.size());
                str.append(decryptedChar);

            } else {
                str.append(ch);
            }
        }
        return str.toString();
    }

    public static boolean checkForWords(String text, List<String> dictionary) {
        for (String word : dictionary) {
            if (text.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static void bruteForceDecryptAndCheck(String encryptedText, List<String> dictionary) {
        List<String> decriptions = new ArrayList<>();
        List<Character> cryptAlphabet = createRusAlphabet();
        for (int key = 1; key < cryptAlphabet.size(); key++) {
            String decryptedText = decrypt(encryptedText, key);
            if (checkForWords(decryptedText, dictionary)) {
                decriptions.add("Ключ" + key + " слово " + decryptedText);
            }
        }
        if (!decriptions.isEmpty()) {
            System.out.println("Результаты: ");
            for (String decryption : decriptions) {
                System.out.println(decryption);
            }
        } else {
            System.out.println("Совпадений со словарем не найдено");
        }
        saveToFile(decriptions, "brute force.txt");
    }

    public static void saveToFile(String text, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(text);
            System.out.println("Текс сохранен в файл " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении текста");
        }


    }
    public static void saveToFile(List<String> decrypt, String fileName) {
        try (PrintWriter printWriter = new PrintWriter(fileName)) {
            for (String line : decrypt) {
                printWriter.println(line);
            }
            System.out.println("Результаты записаны в файл");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении файла " + e.getMessage());
        }
    }
    public static String readToFile(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();

        try (Reader reader = new FileReader(fileName);
             BufferedReader buf = new BufferedReader(reader)) {
            while (buf.ready()){
                stringBuilder.append(buf.readLine());
            }
        } catch (IOException e) {
            System.out.println("Файл не найден");
        }
        return stringBuilder.toString();
    }
}