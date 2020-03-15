package cn.edu.hebut.easydesign.Session.User;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.R;

public class UserStringResources {

    private static String[] countryNames;
    private static List<String[]> schoolNames;
    private static int[] schoolNameIds = new int[]{R.array.chinese_school, R.array.nz_school};

    public static String[] getCountryNames() {
        loadSchoolResources();
        return countryNames;
    }

    public static List<String[]> getSchoolNames() {
        loadSchoolResources();
        return schoolNames;
    }

    private static void loadSchoolResources() {
        if (countryNames == null) {
            synchronized (UserStringResources.class) {
                if (countryNames == null) {
                    Resources resources = ContextHolder.getContext().getResources();
                    countryNames = resources.getStringArray(R.array.school_countries);
                    schoolNames = new ArrayList<>(countryNames.length);
                    for (int id : schoolNameIds) {
                        schoolNames.add(resources.getStringArray(id));
                    }
                }
            }
        }
    }

    private static String[] industryNames;
    private static List<String[]> positionNames;
    private static int[] positionNameIds = new int[]{R.array.position_it, R.array.position_medical};

    public static String[] getIndustryNames() {
        loadWorkResources();
        return industryNames;
    }

    public static List<String[]> getPositionNames() {
        loadWorkResources();
        return positionNames;
    }

    private static void loadWorkResources() {
        if (industryNames == null) {
            synchronized (UserStringResources.class) {
                if (industryNames == null) {
                    Resources resources = ContextHolder.getContext().getResources();
                    industryNames = resources.getStringArray(R.array.industry);
                    positionNames = new ArrayList<>(industryNames.length);
                    for (int id : positionNameIds) {
                        positionNames.add(resources.getStringArray(id));
                    }
                }
            }
        }
    }
}
