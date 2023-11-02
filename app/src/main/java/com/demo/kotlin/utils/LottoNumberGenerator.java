package com.demo.kotlin.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LottoNumberGenerator {

    public static List<Integer> generateMainNumbers() {
        List<Integer> mainNumbers = new ArrayList<>();
        for (int i = 1; i <= 35; i++) {
            mainNumbers.add(i);
        }
        Collections.shuffle(mainNumbers);
        List<Integer> selectedMainNumbers = mainNumbers.subList(0, 5);
        Collections.sort(selectedMainNumbers);
        return selectedMainNumbers;
    }

    public static List<Integer> generateSpecialNumbers() {
        List<Integer> specialNumbers = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            specialNumbers.add(i);
        }
        Collections.shuffle(specialNumbers);
        List<Integer> selectedSpecialNumbers = specialNumbers.subList(0, 2);
        Collections.sort(selectedSpecialNumbers);
        return selectedSpecialNumbers;
    }
}