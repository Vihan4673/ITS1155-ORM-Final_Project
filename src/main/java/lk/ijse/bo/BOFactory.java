package lk.ijse.bo;


import lk.ijse.bo.custom.impl.*;


public class BOFactory {

    public enum BOType{
    PROGRAM, STUDENT, DASHBOARD, ADDPROGRAM, VIEWALL, ADDPAYMENT, SIGNUP, LOGIN, SETTING,COURSE,INSTRUCTOR,LESSON,PAYMENT
    }

    public static SuperBO getBO(BOType boType){
        return (SuperBO) switch (boType) {
            case PROGRAM -> new CourseBOImpl();
            case STUDENT -> new StudentBOImpl();
            case DASHBOARD -> new DashboardBOImpl();
            case INSTRUCTOR -> new InstructorBOImpl();
            case VIEWALL -> new ViewAllBOImpl();
            case LESSON -> new LessonBOImpl();
            case SIGNUP -> new SignUpBOImpl();
            case LOGIN -> new LoginBOImpl();
            case SETTING -> new SettingBOImpl();
            case COURSE -> new CourseBOImpl();
            case PAYMENT -> new PaymentBOImpl();
            default -> null;
        };
    }
}
