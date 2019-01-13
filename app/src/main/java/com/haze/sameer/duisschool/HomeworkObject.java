package com.haze.sameer.duisschool;

public class HomeworkObject {

    String teacherNameHomework, dateHomework, submissionHomework, evaHomework, classesHomework, sectionHomework, subjectHomework;

    public HomeworkObject(String teacherNameHomework, String dateHomework, String submissionHomework,
                          String evaHomework, String classesHomework, String sectionHomework, String subjectHomework) {
        this.teacherNameHomework = teacherNameHomework;
        this.dateHomework = dateHomework;
        this.submissionHomework = submissionHomework;
        this.evaHomework = evaHomework;
        this.classesHomework = classesHomework;
        this.sectionHomework = sectionHomework;
        this.subjectHomework = subjectHomework;
    }

    public String getTeacherNameHomework() {
        return teacherNameHomework;
    }
    public void setTeacherNameHomework(String teacherNameHomework) {
        this.teacherNameHomework = teacherNameHomework;
    }
    public String getDateHomework() {
        return dateHomework;
    }
    public void setDateHomework(String dateHomework) {
        this.dateHomework = dateHomework;
    }
    public String getSubmissionHomework() {
        return submissionHomework;
    }
    public void setSubmissionHomework(String submissionHomework) {
        this.submissionHomework = submissionHomework;
    }
    public String getEvaHomework() {
        return evaHomework;
    }
    public void setEvaHomework(String evaHomework) {
        this.evaHomework = evaHomework;
    }
    public String getClassesHomework() {
        return classesHomework;
    }
    public void setClassesHomework(String classesHomework) {
        this.classesHomework = classesHomework;
    }
    public String getSectionHomework() {
        return sectionHomework;
    }
    public void setSectionHomework(String sectionHomework) {
        this.sectionHomework = sectionHomework;
    }
    public String getSubjectHomework() {
        return subjectHomework;
    }
    public void setSubjectHomework(String subjectHomework) {
        this.subjectHomework = subjectHomework;
    }

}