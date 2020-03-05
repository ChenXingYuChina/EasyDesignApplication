package cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import java.util.List;

import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.User.School;

public class UserSchoolHelper<name extends View, diploma extends View, country extends View, publicSchool extends View> {
    protected name name;
    protected country country;
    protected publicSchool publicSchool;
    protected diploma diploma;
    protected ViewGroup cachedGroup;

    public static String[] countryNames;
    public static List<String[]> schoolNames;
    private static int[] schoolNameIds = new int[]{R.array.chinese_school, R.array.nz_school};

    public UserSchoolHelper(ViewGroup viewGroup) {
        initView(viewGroup);
    }

    public UserSchoolHelper() {
        name = null;
        country = null;
        publicSchool = null;
        diploma = null;
    }

    public void putData(ViewGroup viewGroup, School school) {
        if (cachedGroup != viewGroup) {
            initView(viewGroup);
        }
        int selectedCountry = 0;
        if (country != null) {
//            if (country instanceof Spinner) {
//                prepareResources();
//                for (int i = 0; i < countryNames.length; i++) {
//                    if (countryNames[i].equals(school.country)) {
//                        selectedCountry = i;
//                        break;
//                    }
//                }
//                ((Spinner) country).setSelection(selectedCountry);
//            } else
            if (country instanceof TextView) {
                ((TextView) country).setText(school.country);
            }
        }

        if (name != null) {
//            if (name instanceof Spinner) {
//                String[] schoolNamesForCountry = schoolNames.get(selectedCountry);
//                for (int i = 0; i < schoolNamesForCountry.length; i++) {
//                    if (schoolNamesForCountry[i].equals(school.name)) {
//                        ((Spinner) name).setSelection(i);
//                    }
//                }
//            } else
            if (name instanceof TextView) {
                ((TextView) name).setText(school.name);
            }
        }
        if (diploma != null) {
            if (diploma instanceof Spinner) {
                ((Spinner) diploma).setSelection(school.diploma);
            } else if (diploma instanceof TextView) {
                ((TextView) diploma).setText(school.getDiploma());
            }
        }
        if (publicSchool != null) {
            if (publicSchool instanceof Chip) {
                ((Chip) publicSchool).setCheckable(school.publicSchool);
            } else if (publicSchool instanceof TextView) {
                ((TextView) publicSchool).setText(school.publicSchool ? "公开" : "未公开");
            }
        }
    }


    public void putData(School school) {
        if (cachedGroup == null) {
            throw new IllegalArgumentException();
        }
        putData(cachedGroup, school);
    }

    private void initView(ViewGroup viewGroup) {
        cachedGroup = viewGroup;
        name = viewGroup.findViewById(R.id.school_name);
        diploma = viewGroup.findViewById(R.id.diploma);
        publicSchool = viewGroup.findViewById(R.id.school_public);
        country = viewGroup.findViewById(R.id.school_country);
    }

    public name getName() {
        return name;
    }

    public diploma getDiploma() {
        return diploma;
    }

    public country getCountry() {
        return country;
    }

    public publicSchool getPublicSchool() {
        return publicSchool;
    }

    private void prepareResources() {
        if (countryNames == null) {
            synchronized (UserSchoolHelper.class) {
                if (countryNames == null) {
                    Resources resources = ContextHolder.getContext().getResources();
                    countryNames = resources.getStringArray(R.array.school_countries);
                    for (int id : schoolNameIds) {
                        schoolNames.add(resources.getStringArray(id));
                    }
                }
            }
        }
    }
}
