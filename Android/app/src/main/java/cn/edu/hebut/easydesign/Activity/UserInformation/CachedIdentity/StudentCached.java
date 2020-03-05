package cn.edu.hebut.easydesign.Activity.UserInformation.CachedIdentity;

import java.util.LinkedList;
import java.util.List;

import cn.edu.hebut.easydesign.Session.User.School;
import cn.edu.hebut.easydesign.Session.User.Student;

public class StudentCached extends CachedIdentity {
    public Student student;
    public List<Integer> cachedOrder = new LinkedList<>();
    public List<Integer> cachedCountryPosition = new LinkedList<>();
    public List<Integer> cachedNamePosition = new LinkedList<>();

    public void addToCached(School school, int order, int countryPosition, int namePosition) {
        student.schools.add(school);
        cachedOrder.add(order);
        cachedCountryPosition.add(countryPosition);
        cachedNamePosition.add(namePosition);
    }
}
